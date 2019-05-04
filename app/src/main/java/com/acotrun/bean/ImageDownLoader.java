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

    public static String thumbnail = "Thumbnail/";
    static Handler mHandler;
    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    static int a=0;
    public ImageDownLoader() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
    }

    // 加载圆形ImageVie
    public void roundImageViewExcute(final ImageView view, final String picName) {
        if (view == null || picName == null) {
            return;
        }
        final Bitmap bm = BitmapCache.showCacheBitmap(picName);
        if (bm != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    String target=(String) view.getTag();
                    if(target==null||target.startsWith(picName))
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

                    byte[] bb = NetInfoUtil.getCacheThumbnail(picName);
                    if (bb != null) {
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(bb,
                                0, bb.length);
                        if (bitmap != null) {
                            BitmapCache.addBitmapToMemoryCache(picName, bitmap);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String target=(String) view.getTag();
                                    if(target==null||target.startsWith(picName))
                                        view.setImageBitmap(bitmap);
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    // 加载缩略图 cache File Net
    public void thumbnailExcute(final ImageView view, final String picName) {
        if (view == null || picName == null) {
            return;
        }
        final Bitmap bm = BitmapCache.showCacheBitmap(thumbnail + picName);
        if (bm != null) {					//缓存或文件中存在
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    String target=(String) view.getTag();
                    if(target==null||target.startsWith(picName))
                        view.setImageBitmap(bm);
                }
            });
            return;
        }
        if (singleThreadExecutor == null || singleThreadExecutor.isShutdown()) {
            singleThreadExecutor = Executors.newSingleThreadExecutor();
        }
        singleThreadExecutor.execute(new Runnable() {			//网络加载
            @Override
            public void run() {
                try {
                    byte[] bb = NetInfoUtil.getCacheThumbnail(picName);
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(bb, 0,
                            bb.length);
                    if (bitmap != null) {
                        BitmapCache.addBitmapToMemoryCache(thumbnail + picName,
                                bitmap);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                String target=(String) view.getTag();
                                if(target==null||target.startsWith(picName))
                                    view.setImageBitmap(bitmap);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 加载ImageView cache File Net
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
                    byte[] bb = NetInfoUtil.getCachePicture(picName);
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

    public void cancelTaskNow() {
        if (singleThreadExecutor != null) {
            singleThreadExecutor.shutdownNow();
            singleThreadExecutor = null;
        }
    }

}
