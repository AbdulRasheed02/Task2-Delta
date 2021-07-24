package com.example.task2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StaticMenu extends AppCompatActivity {

    Button btn_StaticEasy, btn_StaticHard;
    Boolean HardMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_menu);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        this.setTitle("STATIC");

        btn_StaticEasy=findViewById(R.id.btn_StaticEasy);
        btn_StaticHard=findViewById(R.id.btn_StaticHard);

        btn_StaticEasy.setText("Easy");
        btn_StaticHard.setText("Hard");

        btn_StaticEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(StaticMenu.this,CustomViewActivity.class);
                intent.putExtra("HardMode", false);
                startActivity(intent);
            }
        });

        btn_StaticHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(StaticMenu.this,CustomViewActivity.class);
                intent.putExtra("HardMode", true);
                startActivity(intent);
            }
        });

    }

}