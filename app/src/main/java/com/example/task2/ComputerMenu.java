package com.example.task2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ComputerMenu extends AppCompatActivity {

    Button btn_ComputerNovice, btn_ComputerNormal, btn_ComputerNightmare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer_menu);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        this.setTitle("COMPUTER");

        btn_ComputerNovice=findViewById(R.id.btn_ComputerEasy);
        btn_ComputerNormal=findViewById(R.id.btn_ComputerMedium);
        btn_ComputerNightmare=findViewById(R.id.btn_ComputerHard);

        btn_ComputerNovice.setText("NOVICE");
        btn_ComputerNormal.setText("NORMAL");
        btn_ComputerNightmare.setText("NIGHTMARE");

        btn_ComputerNovice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ComputerMenu.this,CustomViewActivity2.class);
                intent.putExtra("NoviceMode", true);
                intent.putExtra("NormalMode", false);
                intent.putExtra("NightmareMode", false);
                startActivity(intent);
            }
        });

        btn_ComputerNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ComputerMenu.this,CustomViewActivity2.class);
                intent.putExtra("NoviceMode", false);
                intent.putExtra("NormalMode", true);
                intent.putExtra("NightmareMode", false);
                startActivity(intent);
            }
        });

        btn_ComputerNightmare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ComputerMenu.this,CustomViewActivity2.class);
                intent.putExtra("NoviceMode", false);
                intent.putExtra("NormalMode", false);
                intent.putExtra("NightmareMode", true);
                startActivity(intent);
            }
        });

    }
}