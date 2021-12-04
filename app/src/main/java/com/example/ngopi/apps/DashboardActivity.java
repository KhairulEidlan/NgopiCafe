package com.example.ngopi.apps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.ngopi.R;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private StaticRvAdapter staticRvAdapter;

    BubbleNavigationLinearView bubbleNavigationLinearView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);

        //recyclerView assign
        recyclerView = findViewById(R.id.rv_1);
        //navigation assign
        bubbleNavigationLinearView = findViewById(R.id.bot_navigation);

        recyclerview();
        menu_bar();
    }

    public void recyclerview(){
        ArrayList<StaticRvModel> item = new ArrayList<>();
        item.add(new StaticRvModel(R.mipmap.coffee_foreground,"Coffee"));
        item.add(new StaticRvModel(R.mipmap.ice_coffee_foreground,"Ice Coffee"));
        item.add(new StaticRvModel(R.mipmap.boba_foreground,"Boba Tea"));
        item.add(new StaticRvModel(R.mipmap.ice_cream_foreground,"Ice Cream"));
        item.add(new StaticRvModel(R.mipmap.sandwich_foreground,"Sandwich"));
        item.add(new StaticRvModel(R.mipmap.can_foreground,"Soda Can"));

        staticRvAdapter = new StaticRvAdapter(item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(staticRvAdapter);

    }
    public void menu_bar(){
//        set nombor atas dia
//        bubbleNavigationLinearView.setBadgeValue(1, "3");
        bubbleNavigationLinearView.setCurrentActiveItem(0);
        bubbleNavigationLinearView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position ) {

                switch (position){
                    case 0:
                        Intent intent = new Intent(DashboardActivity.this ,DashboardActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(DashboardActivity.this ,CartActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                    case 2:
                        Intent intent2 = new Intent(DashboardActivity.this ,ProfileActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                }
            }

        });
    }

    public void onBackPressed() {
    }
}