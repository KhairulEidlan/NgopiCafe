package com.example.ngopi.apps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.ngopi.R;
import com.example.ngopi.apps.fragment.Cart;
import com.example.ngopi.apps.fragment.Home;
import com.example.ngopi.apps.fragment.Profile;
import com.example.ngopi.loginsignup.LoginActivity;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;

public class AppMainActivity extends AppCompatActivity {
    BubbleNavigationLinearView bubbleNavigationLinearView;
    FragmentTransaction fragmentTransaction;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_app_main);

        bubbleNavigationLinearView = findViewById(R.id.bubbleNav);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        Toast.makeText(AppMainActivity.this, username,Toast.LENGTH_SHORT).show();
        menuBar();
    }

    private void menuBar() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,new Home());
        fragmentTransaction.commit();

        bubbleNavigationLinearView.setNavigationChangeListener((view, position) -> {

            switch (position)
            {
                case 0:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.framelayout,new Home());
                    fragmentTransaction.commit();
                    break;
                case 1:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.framelayout,new Cart());
                    fragmentTransaction.commit();
                    break;
                case 2:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.framelayout,new Profile());
                    fragmentTransaction.commit();
                    break;
            }

        });
    }
}