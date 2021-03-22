import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server模拟服务器，接受Socket的信息，找到方法，执行方法，并且返回
 */
public class Server {
    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(8088);// 用socket模拟客户端访问服务器的方法
        while(true){
            Socket client = server.accept();
            System.out.println(client);
            process(client);// 服务器执行服务，访问方法
            client.close();
            break;
        }


    }
    static void process(Socket socket) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

        String className = ois.readUTF();
        String methodName = ois.readUTF();
        Class[] parameterTypes = (Class[]) ois.readObject();
        Object[] parameters = (Object[]) ois.readObject();// 读取客户端发送的信息


        Class myclass = User.class;// 模拟注册表查找或者 Spring 的 bean 注入找到类
        Method method = myclass.getMethod(methodName, parameterTypes);// 获取方法

        Object o = method.invoke(myclass.newInstance(), parameters);// 执行方法
        oos.writeObject(o);
        oos.flush();// 把结果发送到客户端
    }
}
