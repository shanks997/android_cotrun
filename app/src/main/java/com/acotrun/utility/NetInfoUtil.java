package com.acotrun.utility;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NetInfoUtil {

    // 响应
    private static Socket sk = null;
    private static DataInputStream dis = null;
    private static DataOutputStream dos = null;
    private static Lock mlock = new ReentrantLock();
    // 缓冲
    public static Socket cachess = null;
    public static DataInputStream cachedin = null;
    public static DataOutputStream cachedos = null;
    static Lock cacheLock = new ReentrantLock();
    // 后台下载
    public static Socket onLoadss = null;
    public static DataInputStream onLoaddin = null;
    public static DataOutputStream onLoaddos = null;
    static Lock onLoadLock = new ReentrantLock();

    private static boolean flag;
    // 本地 ip test
//    private static final String ip = "112.86.198.226";
    // 阿里云 ip
    private static final String ip = "121.199.1.160";

    // 通信建立(界面响应)
    public static void connect() throws Exception {
        mlock.lock();
        sk = new Socket();

        // 云服务器 ip
        SocketAddress skAddr = new InetSocketAddress(ip, 9997);
        sk.connect(skAddr, 5000);
        dis = new DataInputStream(sk.getInputStream());
        dos = new DataOutputStream(sk.getOutputStream());
    }

    // 通信关闭(界面响应)
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

    // 通信建立(缓冲)
    public static void cacheConnect() throws Exception {
        cacheLock.lock();
        cachess = new Socket();// 创建一个ServerSocket对象
        SocketAddress socketAddress = new InetSocketAddress(ip, 9998); // 绑定到指定IP和端口
        cachess.connect(socketAddress, 5000);// 设置连接超时时间
        cachedin = new DataInputStream(cachess.getInputStream());// 创建新数据输入流
        cachedos = new DataOutputStream(cachess.getOutputStream());// 创建新数据输出流
    }

    // 通信关闭(缓冲)
    public static void cacheDisConnect() {
        if (cachedos != null) {
            try {
                cachedos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (cachedin != null) {
            try {
                cachedin.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (cachess != null) {
            try {
                cachess.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cacheLock.unlock();
    }

    // 通信建立(下载)
    public static void onLoadConnect() throws Exception {
        onLoadLock.lock();
        onLoadss = new Socket();// 创建一个ServerSocket对象
        SocketAddress socketAddress = new InetSocketAddress(ip, 9999); // 绑定到指定IP和端口
        onLoadss.connect(socketAddress, 5000);// 设置连接超时时间
        onLoaddin = new DataInputStream(onLoadss.getInputStream());// 创建新数据输入流
        onLoaddos = new DataOutputStream(onLoadss.getOutputStream());// 创建新数据输出流
    }

    // 通信关闭(下载)
    public static void onLoadDisConnect() {
        if (onLoaddos != null) {
            try {
                onLoaddos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (onLoaddin != null) {
            try {
                onLoaddin.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (onLoadss != null) {
            try {
                onLoadss.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        onLoadLock.unlock();
    }

    // 是否是用户
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

    // 注册
    public static boolean register(String name, String password,
                                   String avatar, String sex) {
        try {
            cacheConnect();
            cachedos.writeUTF(Constant.REGIST + name + "<#>" + password + "<#>"
                    + avatar + "<#>" + sex);
            flag = cachedin.readBoolean();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cacheDisConnect();
        }
        return flag;
    }

    public static String uploadPic(byte[] bb) {// 上传图片(后台)
        String picName = null;
        try {
            onLoadConnect();
            onLoaddos.writeUTF(Constant.INSERT_PIC);
            onLoaddos.writeInt(bb.length);
            onLoaddos.write(bb);
            onLoaddos.flush();
            picName = onLoaddin.readUTF();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            onLoadDisConnect();
        }
        return picName;
    }

    // 获取图片（按名称图片名）(缓冲)
    public static byte[] getCachePicture(String picName) {
        byte[] data = null;
        try {
            cacheConnect();
            cachedos.writeUTF(Constant.GET_IMAGE + picName);
            data = IOUtil.readBytes(cachedin);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cacheDisConnect();
        }
        return data;
    }

    // 获取缩略图 (缓冲)
    public static byte[] getCacheThumbnail(String picName) {
        byte[] data = null;
        try {
            cacheConnect();
            cachedos.writeUTF(Constant.GET_THUMBNAIL + picName);
            data = IOUtil.readBytes(cachedin);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cacheDisConnect();
        }
        return data;
    }

    // 获取图片（按名称图片名）(下载)
    public static byte[] getOnLoadPicture(String picName) {
        byte[] data = null;
        try {
            onLoadConnect();
            onLoaddos.writeUTF(Constant.GET_IMAGE + picName);
            data = IOUtil.readBytes(onLoaddin);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            onLoadDisConnect();
        }
        return data;
    }

    // 获取用户所有信息(缓冲)
    public static List<String> getUser(String sname) {
        List<String> list = new ArrayList<String>();
        try {
            cacheConnect();
            cachedos.writeUTF(Constant.GET_UID_MESSAGE + sname);
            String message = cachedin.readUTF();
            System.out.println("message" + message);
            String[] content = message.split("<#>");
            for (String s : content) {
            list.add(s);
        }
        if (list.size() > 0)
            return list;
    } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cacheDisConnect();
        }
        return null;
    }

}
