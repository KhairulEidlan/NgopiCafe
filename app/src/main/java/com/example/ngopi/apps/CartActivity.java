package com.example.ngopi.apps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ngopi.R;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;

public class CartActivity extends AppCompatActivity {

    BubbleNavigationLinearView bubbleNavigationLinearView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        bubbleNavigationLinearView = findViewById(R.id.bot_navigation);
        menu_bar();
    }

    public void menu_bar(){
        bubbleNavigationLinearView.setCurrentActiveItem(0);
        bubbleNavigationLinearView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {

                switch (position){
                    case 0:
                        Intent intent = new Intent(CartActivity.this ,DashboardActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(CartActivity.this ,CartActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(CartActivity.this ,ProfileActivity.class);
                        startActivity(intent2);
                        break;
                }
            }
        });
    }
    public void onBackPressed() {
    }
}