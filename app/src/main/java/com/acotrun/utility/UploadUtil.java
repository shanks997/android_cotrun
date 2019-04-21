package com.acotrun.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UploadUtil {

    Handler mhandler;
    Context context;
    static ExecutorService singleThreadExecutor = Executors
            .newSingleThreadExecutor();
    public UploadUtil(Context context) {
        this.context = context;
        mhandler = new Handler();
    }

    // 上传图片
    public static String uploadPic(String path) {
        String name = null;
        try {
            Bitmap bitmap = BitmapUtils.revitionImageSize(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int options = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            while (baos.toByteArray().length / 1024 > 100) {
                baos.reset();
                options -= 10;
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            }
            byte[] bb = baos.toByteArray();
            name = NetInfoUtil.uploadPic( bb);
            if (name != null && name.equals(Constant.NO_MESSAGE)) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

}
