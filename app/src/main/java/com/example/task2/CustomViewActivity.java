package com.example.task2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class CustomViewActivity extends Activity{

    CustomViewLayout customViewLayout;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customViewLayout=new CustomViewLayout(this);
        setContentView(customViewLayout);

    }

    @Override
    protected void onPause() {
        super.onPause();
        customViewLayout.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        customViewLayout.resume();
    }

    public void onBackPressed() {
        startActivity(new Intent(CustomViewActivity.this, MainActivity.class));
    }
}
