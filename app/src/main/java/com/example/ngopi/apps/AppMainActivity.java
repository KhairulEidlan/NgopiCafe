package com.example.ngopi.apps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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

        menuBar();
    }

    private void menuBar() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,new Home());
        fragmentTransaction.commit();
        Bundle bundle = new Bundle();
        bubbleNavigationLinearView.setNavigationChangeListener((view, position) -> {

            switch (position)
            {
                case 0:
                    bundle.putString("username", username);
                    Home home = new Home();
                    home.setArguments(bundle);
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.framelayout,home);
                    fragmentTransaction.commit();
                    break;
                case 1:
                    bundle.putString("username", username);
                    Cart cart = new Cart();
                    cart.setArguments(bundle);
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.framelayout,cart);
                    fragmentTransaction.commit();
                    break;
                case 2:
                    bundle.putString("username", username);
                    Profile profile = new Profile();
                    profile.setArguments(bundle);
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.framelayout,profile);
                    fragmentTransaction.commit();

                    break;
            }

        });
    }
}