package com.acotrun.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BitmapUtils {

    // 根据宽高，计算SampleSize值
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        int i = 0;
        while (true) {
            if ((options.outWidth >> i <= reqWidth)
                    && (options.outHeight >> i <= reqHeight)) {
                options.inSampleSize = (int) Math.pow(2.0D, i);
                break;
            }
            i += 1;
        }
        return options.inSampleSize;
    }

    // 获取本地图片合适的缩略图
    public static Bitmap decodeSampleBitmapFromFile(String pathName,
                                                    int reqWidth, int reqHeight){
        Bitmap bitmap = null;
        try {
            BufferedInputStream in = new BufferedInputStream(
                    new FileInputStream(new File(pathName)));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            options.inSampleSize = calculateInSampleSize(options, reqWidth,
                    reqHeight);
            options.inJustDecodeBounds = false;
            in = new BufferedInputStream(
                    new FileInputStream(new File(pathName)));
            bitmap = BitmapFactory.decodeStream(in, null, options);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // 获取本地图片 根据屏幕分辨率宽高
    public static Bitmap revitionImageSize(String pathName) {
        Bitmap bitmap = null;
        try {
            BufferedInputStream in = new BufferedInputStream(
                    new FileInputStream(new File(pathName)));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            options.inSampleSize = calculateInSampleSize(options,
                    Constant.ScreenWidth, Constant.ScreenHeight);
            options.inJustDecodeBounds = false;
            in = new BufferedInputStream(
                    new FileInputStream(new File(pathName)));
            bitmap = BitmapFactory.decodeStream(in, null, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // 向本地添加图片
    public static void savePic(String fileName) {
        if (FileUtils.isFileExists(fileName)
                && FileUtils.getFileSize(fileName) > 0) {
            return;
        }
        Bitmap bitmap = BitmapCache.showCacheBitmap(fileName);
        if (bitmap == null) {
            byte[] bb = NetInfoUtil.getOnLoadPicture(fileName);
            bitmap = BitmapFactory.decodeByteArray(bb, 0, bb.length);
        }
        try {
            FileUtils.savaBitmap(fileName, bitmap);
            BitmapCache.addBitmapToMemoryCache(fileName, bitmap);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    // 删除本地图片
    public static void deletePic(String picName) {
        if (!FileUtils.isFileExists(picName)) {
            return;
        }
        File dirFile = new File(FileUtils.getStorageDirectory());
        if (!dirFile.exists()) {
            return;
        }
        if (dirFile.isDirectory()) {
            new File(dirFile.getAbsolutePath() + File.separator + picName)
                    .delete();
        }
    }

}
