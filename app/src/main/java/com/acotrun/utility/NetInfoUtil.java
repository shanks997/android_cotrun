package com.acotrun.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static android.content.ContentValues.TAG;

public class NetInfoUtil {
    private static Socket sk = null;
    private static DataInputStream dis = null;
    private static DataOutputStream dos = null;
    private static boolean flag;
    // 服务器域名，不是很好使用
    private static String host = "238v261d21.wicp.vip";
    private static Lock mlock = new ReentrantLock();

    public static void connect() throws Exception {
        mlock.lock();
        sk = new Socket();
        String ip = "157.0.24.15"; // 本机 ip，有变动需修改
//        try {
//            InetAddress iaddr = InetAddress.getByName(host);
//            String ip = iaddr.getHostAddress();
//            Log.d("putOutMsg", "ip = " + ip);
            SocketAddress skAddr = new InetSocketAddress(ip, 9999);
            sk.connect(skAddr, 5000);
            dis = new DataInputStream(sk.getInputStream());
            dos = new DataOutputStream(sk.getOutputStream());
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//            Log.d("putOutMeg", "域名解析出错！");
//        }
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
