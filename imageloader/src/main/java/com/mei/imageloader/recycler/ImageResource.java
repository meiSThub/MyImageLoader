package com.mei.imageloader.recycler;

import android.graphics.Bitmap;

/**
 * @author mxb
 * @date 2020/10/20
 * @desc
 * @desired
 */
public class ImageResource {

    private Bitmap mBitmap;

    // 资源的引用计数
    private int acquired;

    public ImageResource(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    /**
     * 引用计数-1
     */
    public void release() {
        if (--acquired == 0) {
            // listener.onResourceReleased(key, this);
        }
    }

    /**
     * 引用计数+1
     */
    public void acquire() {
        if (mBitmap != null && mBitmap.isRecycled()) {
            throw new IllegalStateException("Acquire a recycled resource");
        }
        ++acquired;
    }
}
