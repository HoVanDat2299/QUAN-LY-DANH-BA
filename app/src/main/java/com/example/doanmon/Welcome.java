package com.example.doanmon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

public class Welcome extends AppCompatActivity {



    ProgressBar pgBarLoad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();
        ChuyenActivity();
    }
        private void ChuyenActivity() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                        startActivity(new Intent(Welcome.this, MainActivity.class));
                    finish();
                }
            },3000);

        }
    }
