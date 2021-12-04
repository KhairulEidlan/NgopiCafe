package com.example.ngopi.apps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ngopi.R;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;

public class ProfileActivity extends AppCompatActivity {

    BubbleNavigationLinearView bubbleNavigationLinearView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bubbleNavigationLinearView = findViewById(R.id.bot_navigation);
        menu_bar();
    }
    public void menu_bar(){
        bubbleNavigationLinearView.setCurrentActiveItem(2);
        bubbleNavigationLinearView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {

                switch (position){
                    case 0:
                        Intent intent = new Intent(ProfileActivity.this ,DashboardActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        break;
                    case 1:
                        Intent intent1 = new Intent(ProfileActivity.this ,CartActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        break;
                    case 2:
                        Intent intent2 = new Intent(ProfileActivity.this ,ProfileActivity.class);
                        startActivity(intent2);
                        break;
                }
            }
        });
    }
    public void onBackPressed() {
    }
}