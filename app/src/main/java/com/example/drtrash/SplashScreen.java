package com.example.drtrash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    private ConstraintLayout layout;
    private static final int TIMER = 3000;
    private static Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        layout = findViewById(R.id.introUI);



        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                HomeUI();

            }
        },TIMER);

    }

    public void HomeUI(){

        Intent intent = new Intent(this, homeMainUI.class);
        startActivity(intent);
        finish();

    }
}