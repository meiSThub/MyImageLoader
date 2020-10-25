package com.mei.imageloader.recycler;

import android.content.ComponentCallbacks2;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.util.LruCache;

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * @author mxb
 * @date 2020/10/20
 * @desc 基于LRu算法实现的Bitmap 复用池
 * @desired
 */
public class LruBitmapPool extends LruCache<Integer, Bitmap> implements BitmapPool {

    private static final String TAG = "LruBitmapPool";

    // 复用的图片的大小，不能大于将要创建的图片大小的倍数
    private final static int MAX_OVER_SIZE_MULTIPLE = 2;

    private NavigableMap<Integer, Integer> map = new TreeMap<>();

    // 是否正在执行获取复用图片的操作
    private boolean isRemoving;

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public LruBitmapPool(int maxSize) {
        super(maxSize);
    }

    /**
     * 将Bitmap放入复用池
     *
     * @param bitmap 图片对象
     */
    @Override
    public void put(Bitmap bitmap) {
        // 1.如果图片不允许复用,则不加入到复用池
        if (!bitmap.isMutable()) {
            return;
        }

        int byteCount;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            // 图片占用到内存大小
            byteCount = bitmap.getAllocationByteCount();
        } else {
            // 图片像素实际占用到内存大小
            byteCount = bitmap.getByteCount();
        }

        put(byteCount, bitmap);
        map.put(byteCount, 0);
    }

    /**
     * 获取符合要求的Bitmap进行复用
     *
     * @param width  图片的宽度
     * @param height 图片的高度
     * @param config 图片格式信息
     */
    @Override
    public Bitmap get(int width, int height, Bitmap.Config config) {
        if (config == null || width == 0 || height == 0) {
            return null;
        }

        // 1. 计算将要创建的图片占用的内存大小
        // 图片格式： Bitmap.Config.ARGB_8888，占用4个字节； RGB_565：占用2个字节
        int size = width * height * (config == Bitmap.Config.ARGB_8888 ? 4 : 2);
        Log.i(TAG, "get: size=" + (size / 1024 / 1024) + "MB");
        // 2. 获取到图片大小 大于等于size的图片key值
        Integer key = map.ceilingKey(size);
        Log.i(TAG, "get: key=" + (size / 1024 / 1024) + "MB");

        Bitmap bitmap = null;
        // 3. 根据key值，获取可复用的Bitmap对象，复用的图片不能比目标图片大两倍
        if (key != null && key > 0 && key <= size * MAX_OVER_SIZE_MULTIPLE) {
            isRemoving = true;
            bitmap = remove(key);
            isRemoving = false;
        }
        return bitmap;
    }

    @Override
    protected int sizeOf(Integer key, Bitmap value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return value.getAllocationByteCount();
        } else {
            return value.getByteCount();
        }
    }

    @Override
    protected void entryRemoved(boolean evicted, Integer key, Bitmap oldValue, Bitmap newValue) {
        super.entryRemoved(evicted, key, oldValue, newValue);
        map.remove(key);
        // 因为自己在移除Bitmap的时候，isRemoving设置为true，即isRemoving=true，表示是用户自己手动擅长的图片，则不回收内存，如果回收了就不能复用内存了
        if (!isRemoving) { // 如果不是自己手动移除的Bitmap，则回收内存
            oldValue.recycle();
        }
    }

    @Override
    public void clearMemory() {
        evictAll();
    }

    @Override
    public void trimMemory(int level) {
        if (level >= ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) {
            clearMemory();
        } else if (level >= ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                trimToSize(maxSize() / 2);
            }
        }
    }
}
