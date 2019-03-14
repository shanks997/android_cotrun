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
    public static Socket ss = null;
    public static DataInputStream dis = null;
    public static DataOutputStream dos = null;
    public static boolean flag;
    public static String host = "238v261d21.wicp.vip";  // 服务器域名
    static Lock mlock = new ReentrantLock();

    public static void connect() throws Exception {
        mlock.lock();
        ss = new Socket();
        // 使用用服务器 ip 地址，如果 ip 更换，下面要进行值调整
        String ip = "157.0.25.190";

//        ***************************************
//        对于本机 ip 和外网 ip（如百度）ping 不通。
//        ***************************************
//        Process p1 = Runtime.getRuntime().exec("ping -c 1 -w 100 " + "localhost");
//        int status1 = p1.waitFor();
//        if (status1 == 0) {
//            Log.d("putOutMsg", "域名能ping通，有效");
//        } else {
//            Log.d("putOutMsg", "域名ping不通");
//        }

//        ***********
//        域名解析出错
//        ***********
//        try {
//            InetAddress iaddr = InetAddress.getByName("www.baidu.com");
//            String ip = iaddr.getHostAddress();
//            Log.d("putOutMsg", "ip = " + ip);
//            SocketAddress skAddr = new InetSocketAddress("ip", 8888);
//            ss.connect(skAddr, 5000);
//            dis = new DataInputStream(ss.getInputStream());
//            dos = new DataOutputStream(ss.getOutputStream());
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//            Log.d("putOutMeg", "域名解析出错！");
//        }

        SocketAddress skAddr = new InetSocketAddress(ip, 8888);
        ss.connect(skAddr, 5000);
        dis = new DataInputStream(ss.getInputStream());
        dos = new DataOutputStream(ss.getOutputStream());
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
        if (ss != null) {
            try { ss.close(); }
            catch (Exception e) { e.printStackTrace(); }
        }
        mlock.unlock();
    }

    public static boolean isUser(String name, String passward) {
        try {
            connect();
            dos.writeUTF(Constant.IS_USER + name + "<#>" + passward);
            flag = dis.readBoolean();
        } catch (Exception e) { e.printStackTrace(); }
        finally { disConnected(); }
        return flag;
    }
}
