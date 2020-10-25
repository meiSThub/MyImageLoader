package com.mei.myimageloader;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class BitmapSizeActivity extends AppCompatActivity {

    private static final String TAG = "BitmapSizeActivity";

    private TextView mTvSize;

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap_size);

        mTvSize = findViewById(R.id.tv_size);
        mImageView = findViewById(R.id.image_view);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        Log.i(TAG, "设备: density=" + displayMetrics.density + ";densityDpi="
                + displayMetrics.densityDpi);

        BitmapDrawable drawable = (BitmapDrawable) mImageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        Log.i(TAG,
                "xml src bitmap: size=" + bitmap.getByteCount() + ";format=" + bitmap
                        .getConfig() + ";density=" + bitmap.getDensity() + ";width="
                        + bitmap.getWidth() + ";height=" + bitmap.getHeight());

        // 1. 从磁盘加载图片
        try {
            File imageFile = new File(getExternalCacheDir().getPath() + "/jpg_wh_xhdpi.jpg");
            InputStream inputStream = new FileInputStream(imageFile);

            Bitmap bitmap2 = BitmapFactory.decodeStream(inputStream);
            Log.i(TAG,
                    "adcard bitmap: size=" + bitmap2.getByteCount() + ";format=" + bitmap2
                            .getConfig() + ";density=" + bitmap2.getDensity() + ";width="
                            + bitmap2.getWidth() + ";height=" + bitmap2.getHeight());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // 2. 从assets资源加载图片
        {
            try {
                Bitmap decodeBitmap = BitmapFactory
                        .decodeStream(getResources().getAssets().open("jpg_wh_xhdpi.jpg"));
                Log.i(TAG,
                        "assets bitmap: size=" + decodeBitmap.getByteCount() + ";format="
                                + decodeBitmap
                                .getConfig() + ";density=" + decodeBitmap.getDensity() + ";width="
                                + decodeBitmap.getWidth() + ";height=" + decodeBitmap.getHeight());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 3. 从res/raw
        {
            Bitmap decodeBitmap = BitmapFactory
                    .decodeStream(getResources().openRawResource(R.raw.jpg_wh_xhdpi));
            Log.i(TAG,
                    "res/raw bitmap: size=" + decodeBitmap.getByteCount() + ";format="
                            + decodeBitmap
                            .getConfig() + ";density=" + decodeBitmap.getDensity() + ";width="
                            + decodeBitmap
                            .getWidth() + ";height=" + decodeBitmap
                            .getHeight());
        }

        // 4. 从res/drawable
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.jpg_wh_xhdpi);
        Log.i(TAG,
                "res/drawable bitmap: size=" + bitmap1.getByteCount() + ";format=" + bitmap1
                        .getConfig()
                        + ";density=" + bitmap1.getDensity() + ";width=" + bitmap1.getWidth()
                        + ";height=" + bitmap1.getHeight());
    }
}