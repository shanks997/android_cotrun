package com.acotrun.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UploadUtil {

    // 上传图片
    public static String uploadPic(String path) {
        String name = null;
        try {
            FileInputStream fis = new FileInputStream(path);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int options = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            while (baos.toByteArray().length / 1024 > 100) {
                baos.reset();
                options -= 10;
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            }
            byte[] bb = baos.toByteArray();
            name = NetInfoUtil.uploadPic(bb);
            if (name != null && name.equals(Constant.NO_MESSAGE)) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

}
