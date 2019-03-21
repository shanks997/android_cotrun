package com.acotrun.utility;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NetInfoUtil {
    private static Socket sk = null;
    private static DataInputStream dis = null;
    private static DataOutputStream dos = null;
    private static boolean flag;
    // 使用域名
    private static String host = "";
    private static Lock mlock = new ReentrantLock();

    public static void connect() throws Exception {
        mlock.lock();
        sk = new Socket();

        // 腾讯云服务器 ip
        String ip = "129.204.142.254";
        SocketAddress skAddr = new InetSocketAddress(ip, 9999);
        sk.connect(skAddr, 5000);
        dis = new DataInputStream(sk.getInputStream());
        dos = new DataOutputStream(sk.getOutputStream());
    }

    private static void disConnected() {
        if (dos != null) {
            try { dos.flush(); }
            catch (Exception e) { e.printStackTrace(); }
        }
        if (dis != null) {
            try { dis.close(); }
            catch (Exception e) { e.printStackTrace(); }
        }
        if (sk != null) {
            try { sk.close(); }
            catch (Exception e) { e.printStackTrace(); }
        }
        mlock.unlock();
    }

    public static boolean isUser(String name, String password) {
        try {
            connect();
            dos.writeUTF(Constant.IS_USER + name + "<#>" + password);
            // 从服务器端的 dos.writeBoolean() 读取 Boolean 数据流
            flag = dis.readBoolean();
        } catch (Exception e) { e.printStackTrace(); }
        finally { disConnected(); }
        return flag;
    }
}
