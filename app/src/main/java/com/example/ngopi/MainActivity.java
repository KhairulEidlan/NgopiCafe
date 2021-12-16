package com.example.ngopi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.ngopi.admin.AdminDashboardActivity;
import com.example.ngopi.apps.AppMainActivity;
import com.example.ngopi.loginsignup.LoginActivity;
import com.example.ngopi.object.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    ImageView logo,bgimg;
    LottieAnimationView lottieAnimationView;
    private static int SPLASH_SCREEN=5000;

    User user= new User();
    private FirebaseAuth mAuth;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();

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
            mAuth = FirebaseAuth.getInstance();
            // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = mAuth.getCurrentUser();
            SharedPreferences prefs = getSharedPreferences("UserPreferences", MODE_PRIVATE);
            String username = prefs.getString("username", user.getUsername());//"No name defined" is the default value.
            String usertype = prefs.getString("usertype", "User");//"No name defined" is the default value.
            Intent intent;

            if(currentUser != null) {
                if (usertype.equals("User"))
                {
                    intent = new Intent(MainActivity.this, AppMainActivity.class);
                }
                else {
                    intent = new Intent(MainActivity.this, AdminDashboardActivity.class);
                }


            }
            else{
                intent = new Intent(MainActivity.this, LoginActivity.class);
            }
            intent.putExtra("username", username);
            startActivity(intent);
            finish();
        },SPLASH_SCREEN);

        // Initialize Firebase Auth





    }
}