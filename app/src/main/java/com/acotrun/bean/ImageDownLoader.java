package com.acotrun.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import com.acotrun.utility.BitmapCache;
import com.acotrun.utility.NetInfoUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageDownLoader {

    static Handler mHandler;
    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    static int a=0;
    public ImageDownLoader() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
    }

    public void imgExcute(final ImageView view, final String picName) {
        if (view == null || picName == null) {
            return;
        }
        final Bitmap bm = BitmapCache.showCacheBitmap(picName);
        if (bm != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    view.setImageBitmap(bm);
                }
            });
            return;
        }
        if (singleThreadExecutor == null || singleThreadExecutor.isShutdown()) {
            singleThreadExecutor = Executors.newSingleThreadExecutor();
        }
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] bb = NetInfoUtil.getOnLoadPicture(picName);
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(bb, 0,
                            bb.length);
                    if (bitmap != null) {
                        BitmapCache.addBitmapToMemoryCache(picName, bitmap);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                String target=view.getTag()+"";
                                if(target==null||target.startsWith(target))
                                {
                                    view.setImageBitmap(bitmap);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void cancelTask() {
        if (singleThreadExecutor != null) {
            singleThreadExecutor.shutdown();
            singleThreadExecutor = null;
        }
    }

}
