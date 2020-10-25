package com.mei.imageloader.recycler;

import android.graphics.Bitmap;

/**
 * @author mxb
 * @date 2020/10/20
 * @desc Bitmap回收池接口
 * @desired
 */
public interface BitmapPool {


    /**
     * 保存Bitmap到图片回收池，以便服用
     */
    void put(Bitmap bitmap);

    /**
     * 获取一个可复用的Bitmap
     *
     * @param width  图片的宽度
     * @param height 图片的高度
     * @param config 图片格式信息
     */
    Bitmap get(int width, int height, Bitmap.Config config);

    void clearMemory();

    void trimMemory(int level);
}
