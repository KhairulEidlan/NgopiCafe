package com.example.ngopi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.ngopi.loginsignup.LoginActivity;

public class MainActivity extends AppCompatActivity {
    ImageView logo,bgimg;
    LottieAnimationView lottieAnimationView;
    private static int SPLASH_SCREEN=5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        logo = findViewById(R.id.logo);
        bgimg = findViewById(R.id.img);
        lottieAnimationView = findViewById(R.id.lottie);

        logo.animate().translationY(2200).setDuration(1000).setStartDelay(3000);
        lottieAnimationView.animate().translationY(2200).setDuration(1000).setStartDelay(3000);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        },SPLASH_SCREEN);
    }
}