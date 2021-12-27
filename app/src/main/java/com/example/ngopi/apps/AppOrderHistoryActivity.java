package com.example.ngopi.apps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.ngopi.R;
import com.example.ngopi.apps.fragment.OrderHistoryAdapter;
import com.google.android.material.tabs.TabLayout;

public class AppOrderHistoryActivity extends AppCompatActivity {
    private String username;

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    OrderHistoryAdapter orderHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_app_order_history);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        Bundle bundle = new Bundle();
        bundle.putString("username", username);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager2);

        FragmentManager fragmentManager = getSupportFragmentManager();
        orderHistoryAdapter = new OrderHistoryAdapter(fragmentManager,getLifecycle(),bundle);
        viewPager2.setAdapter(orderHistoryAdapter);

        tabLayout.addTab(tabLayout.newTab().setText("Ongoing"));
        tabLayout.addTab(tabLayout.newTab().setText("History"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }
}