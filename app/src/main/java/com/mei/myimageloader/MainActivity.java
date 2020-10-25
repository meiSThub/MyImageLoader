package com.mei.myimageloader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void imageList(View view) {
        startActivity(new Intent(this, ImageListActivity.class));
    }

    public void calBitmapSize(View view) {
        startActivity(new Intent(this, BitmapSizeActivity.class));
    }

    public void bigImage(View view) {
        startActivity(new Intent(this, BigImageActivity.class));
    }
}