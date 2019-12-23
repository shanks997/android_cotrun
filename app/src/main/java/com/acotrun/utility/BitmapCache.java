package com.acotrun.utility;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

public class BitmapCache {

    /**
     * 缓存Image的类，当存储Image的大小大于LruCache设定的值，系统自动释放内存
     */
    private static LruCache<String, Bitmap> mMemoryCache;
    private static LinkedHashMap<String, SoftReference<Bitmap>> softreferences = new LinkedHashMap<String, SoftReference<Bitmap>>(
            40, 0.75f, true);

    public BitmapCache() {
        initCache();
    }

    public static void initCache() {
        if (mMemoryCache == null) {
            // 获取系统分配给每个应用程序的最大内存，每个应用系统分配32M
            int maxMemory = (int) Runtime.getRuntime().maxMemory();
            int mCacheSize = maxMemory / 8;
            // 给LruCache分配1/8 4M
            mMemoryCache = new LruCache<String, Bitmap>(mCacheSize) {

                // 必须重写此方法，来测量Bitmap的大小
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getRowBytes() * value.getHeight();
                }

                // 当item被回收或者删掉时调用
                @Override
                protected void entryRemoved(boolean evicted, String key,
                                            Bitmap oldValue, Bitmap newValue) {
                    Log.v("tag", "hard cache is full , push to soft cache");
                    // lurcache会有特定的算法，当我们的缓存到到峰值时，
                    // 就将缓存中最少使用的移除缓存,这里我们将缓存会移除的东西放入软引用中
                    softreferences
                            .put(key, new SoftReference<Bitmap>(oldValue));
                }
            };
        }
    }

    // 添加Bitmap到内存缓存
    public static synchronized void addBitmapToMemoryCache(String key,
                                                           Bitmap bitmap) {
        if (!TextUtils.isEmpty(key) && getBitmapFromMemCache(key) == null
                && bitmap != null) {
            mMemoryCache.put(key, bitmap);
            softreferences.put(key, new SoftReference<Bitmap>(bitmap));
        }
    }

    public static synchronized Bitmap getBitmapFromMemCache(String key) {
        if(key==null) {
            return null;
        }
        if (mMemoryCache == null) {
            initCache();
        }
        if (mMemoryCache.get(key) != null) {
            return mMemoryCache.get(key);
        }

        SoftReference<Bitmap> bitmapReference = softreferences.get(key);
        if (bitmapReference != null) {
            final Bitmap bitmap2 = bitmapReference.get();
            if (bitmap2 != null)
                return bitmap2;
        }
        // 都没有返回null
        return null;
    }

    // 从Cache和该应用的离线图片中查找
    public static Bitmap showCacheBitmap(String url) {
        // 先从手机缓存中找，如果有就直接返回bitmap
        if (getBitmapFromMemCache(url) != null) {
            return getBitmapFromMemCache(url);
        } else if (FileUtils.isFileExists(url)
                && FileUtils.getFileSize(url) != 0) {
            // 从SD卡获取手机里面获取Bitmap
            Bitmap bitmap = FileUtils.getBitmap(url);
            // 将Bitmap 加入内存缓存
            addBitmapToMemoryCache(url, bitmap);
            return bitmap;
        }
        return null;
    }

}
