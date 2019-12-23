package com.acotrun.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;

public class BitmapUtils {


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
            e.printStackTrace();
        }
    }

}
