package com.example.task2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btn_Static, btn_Computer;
    TextView tv_HighScoreText;
    int highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        this.setTitle("MENU");

        btn_Static =findViewById(R.id.btn_Static);
        btn_Computer=findViewById(R.id.btn_Computer);

        tv_HighScoreText=findViewById(R.id.tv_HighScoreText);

        btn_Static.setText("STATIC");
        btn_Computer.setText("COMPUTER");

        btn_Static.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StaticMenu.class));
            }
        });

        btn_Computer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ComputerMenu.class));
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = this.getSharedPreferences("highScorePrefsKey", Context.MODE_PRIVATE);
        highScore = prefs.getInt("highScoreKey", 0);
        tv_HighScoreText.setText("High Score : "+highScore);

    }

    public void onBackPressed() {
        finishAffinity();
    }
}