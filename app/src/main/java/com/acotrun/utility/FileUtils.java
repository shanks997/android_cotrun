package com.acotrun.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    /**
     * sd卡的根目录
     */
    private static String mSdRootPath = Environment
            .getExternalStorageDirectory().getPath();
    /**
     * 手机的缓存根目录
     */
    private static String mDataRootPath = null;
    /**
     * 保存Image的目录名
     */
    private final static String FOLDER_NAME = "/AndroidImage";

    public FileUtils(Context context) {
        mDataRootPath = context.getCacheDir().getPath();
    }

    // 获取储存Image的目录
    public static String getStorageDirectory() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) ? mSdRootPath + FOLDER_NAME
                : mDataRootPath + FOLDER_NAME;
    }

    // 保存Image的方法，有sd卡存储到sd卡，没有就存储到手机目录
    public static void savaBitmap(String fileName, Bitmap bitmap)
            throws IOException {
        if (bitmap == null) {
            return;
        }
        String path = getStorageDirectory();
        File folderFile = new File(path);
        if (!folderFile.exists()) {
            folderFile.mkdir();
        }
        File file = new File(path + File.separator + fileName);
        file.createNewFile();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;// 个人喜欢从80开始,
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 200) {
            baos.reset();
            options -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("savaBitmap");
    }

    // 从手机或者sd卡获取Bitmap
    public static Bitmap getBitmap(String fileName) {
        return BitmapFactory.decodeFile(getStorageDirectory() + File.separator
                + fileName);
    }

    // 判断文件是否存在
    public static boolean isFileExists(String fileName) {
        return new File(getStorageDirectory() + File.separator + fileName)
                .exists();
    }

    //获取文件的大小
    public static long getFileSize(String fileName) {
        return new File(getStorageDirectory() + File.separator + fileName)
                .length();
    }
}
