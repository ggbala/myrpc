import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * 代理类，接受一个接口参数，通过反射创建一个代理类，并且封装了远程访问服务器的一系列细节，
 * 此代理类和具体的接口无关，接口只是一个参数
 */
public class Agent {
    public static Object getObject(Class target) {
        Object result = Proxy.newProxyInstance(target.getClassLoader(), new Class[]{target}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Socket s = new Socket("127.0.0.1", 8088);

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(s.getOutputStream());
                String className = target.getName();
                String methodName = method.getName();
                Class[] parametersTypes = method.getParameterTypes();// 获得target的一些参数

                objectOutputStream.writeUTF(className);
                objectOutputStream.writeUTF(methodName);
                objectOutputStream.writeObject(parametersTypes);
                objectOutputStream.writeObject(args);
                objectOutputStream.flush();// 把获得的参数信息写到socket里面,发送给服务器


                ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());
                Object result = objectInputStream.readObject();// 从socket里面读取服务端执行返回的信息
                objectOutputStream.close();
                s.close();
                return result;// 返回结果
            }
        });
        return result;
    }
}
