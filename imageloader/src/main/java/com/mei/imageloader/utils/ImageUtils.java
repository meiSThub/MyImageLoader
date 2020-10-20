package com.mei.imageloader.utils;

import android.graphics.BitmapFactory;

/**
 * @author mxb
 * @date 2020/10/19
 * @desc 图片处理工具类
 * @desired
 */
public class ImageUtils {

    /**
     * 计算图片的采样率
     *
     * @param options   图片参数信息
     * @param reqWidth  目标宽度，即期望的图片宽度
     * @param reqHeight 目标高度，即期望的图片高度
     * @return 返回图片的采样率 inSampleSize>=1
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, final int reqWidth,
            final int reqHeight) {
        if (options == null) {
            return 1;
        }

        return calculateInSampleSize(options.outWidth, options.outHeight, reqWidth, reqHeight);
    }

    /**
     * 计算图片的采样率
     *
     * @param originWidth  图片原始宽度
     * @param originHeight 图片原始高度
     * @param reqWidth     目标宽度，即期望的图片宽度
     * @param reqHeight    目标高度，即期望的图片高度
     * @return 返回图片的采样率 inSampleSize>=1
     */
    public static int calculateInSampleSize(final int originWidth, final int originHeight,
            final int reqWidth, final int reqHeight) {
        int inSampleSize = 1;// 默认采样率为1，即加载原图
        if (reqWidth <= 0 || reqHeight <= 0) {
            return inSampleSize;
        }
        if (originWidth > reqWidth || originHeight > reqHeight) {
            final int halfWidth = originWidth / 2;
            final int halfHeight = originHeight / 2;
            while ((halfWidth / inSampleSize) >= reqWidth
                    && (halfHeight / inSampleSize) >= reqHeight) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


}
