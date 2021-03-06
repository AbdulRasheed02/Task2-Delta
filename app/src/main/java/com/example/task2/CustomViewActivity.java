package com.example.task2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

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
