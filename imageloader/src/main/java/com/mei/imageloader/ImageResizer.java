package com.mei.imageloader;

import com.mei.imageloader.utils.ImageUtils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;

/**
 * @author mxb
 * @date 2020/10/19
 * @desc 图片压缩
 * @desired
 */
public class ImageResizer {

    private static final String TAG = "ImageResizer";


    /**
     * 从资源文件中加载一张图片
     *
     * @param resId     图片资源ID
     * @param reqWidth  期望的图片宽度
     * @param reqHeight 期望的图片高度
     */
    public Bitmap decodeFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        if (resId <= 0) {
            return null;
        }
        // 1. 获取图片的宽度信息
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 2. 计算图片的采样率
        int inSampleSize = ImageUtils.calculateInSampleSize(options, reqWidth, reqHeight);
        Log.i(TAG, "decodeFromResource: inSampleSize=" + inSampleSize);
        // 3. 根据指定的采样率，加载资源图片
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 从磁盘中加载图片
     *
     * @param descriptor 图片文件描述符
     * @param reqWidth   期望的图片宽度
     * @param reqHeight  期望的图片高度
     * @return 返回BitMap
     */
    public Bitmap decodeFromFile(FileDescriptor descriptor, int reqWidth, int reqHeight) {
        if (descriptor == null) {
            return null;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(descriptor, null, options);
        int inSampleSize = ImageUtils.calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeFileDescriptor(descriptor, null, options);
    }
}
