package com.mei.myimageloader;

import com.mei.myimageloader.widget.bigview.BigImageView;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * 长图，大图加载
 */
public class BigImageActivity extends AppCompatActivity {

    private ImageView mImageView;

    private BigImageView mBigImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);

        mImageView = findViewById(R.id.image_view);

        mBigImageView = findViewById(R.id.big_image_vew);

        try {
            mBigImageView.setImageInputStream(getAssets().open("long_image.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // decodeRegion();
    }

    /**
     * 加载图片指定区域的图片
     */
    private void decodeRegion() {
        try {
            InputStream inputStream = getAssets().open("ic_girl.jpg");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            Rect rect = new Rect(options.outWidth / 3, 0, options.outWidth * 2 / 3,
                    options.outHeight / 3);
            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(inputStream, false);

            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bitmap = decoder.decodeRegion(rect, options);

            // Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            mImageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}