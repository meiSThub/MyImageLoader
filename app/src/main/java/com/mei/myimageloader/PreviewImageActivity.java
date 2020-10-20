package com.mei.myimageloader;

import com.mei.imageloader.ImageLoader;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 图片预览页面
 */
public class PreviewImageActivity extends AppCompatActivity {

    private static final String KEY_URL = "image_url";

    private ImageView mImageView;

    private String imageUrl;

    public static void start(Activity activity, View View, String url) {
        Intent intent = new Intent(activity, PreviewImageActivity.class);
        intent.putExtra(KEY_URL, url);
        activity.startActivity(intent, ActivityOptions
                .makeSceneTransitionAnimation(activity, View, "previewImage").toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_image);
        mImageView = findViewById(R.id.imageView);

        imageUrl = getIntent().getStringExtra(KEY_URL);

        ImageLoader.getInstance(this).bindBitmap(imageUrl, mImageView);
    }
}