/**
 * 客户端用封装好的动态代理，获得代理类user，执行方法，代理类封装了访问网络的细节。
 */
public class Client {
    public static void main(String[] args) {
        IUserService user = (IUserService)Agent.getObject(IUserService.class);// 获得代理类
        System.out.println(user.findUserById(1));// 执行方法
    }
}
