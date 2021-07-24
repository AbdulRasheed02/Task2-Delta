package com.example.task2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class CustomViewActivity2 extends Activity{

    CustomViewLayout2 customViewLayout2;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customViewLayout2=new CustomViewLayout2(this);
        setContentView(customViewLayout2);

    }

    @Override
    protected void onPause() {
        super.onPause();
        customViewLayout2.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        customViewLayout2.resume();
    }

    public void onBackPressed() {
        startActivity(new Intent(CustomViewActivity2.this, MainActivity.class));
    }
}