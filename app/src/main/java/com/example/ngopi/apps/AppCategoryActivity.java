package com.example.ngopi.apps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.ngopi.R;
import com.example.ngopi.apps.rv.RvMenuAdapter;
import com.example.ngopi.apps.rv.RvMenuModel;

import java.util.ArrayList;

public class AppCategoryActivity extends AppCompatActivity {
    private int ids;
    private TextView cat1,cat2,cat3;
    private RecyclerView rvMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //up button

        ids = getIntent().getIntExtra("ids",0);

        cat1 = findViewById(R.id.cat1);
        cat2 = findViewById(R.id.cat2);
        cat3 = findViewById(R.id.cat3);
        rvMenu = findViewById(R.id.item);

        menuView();
    }

    private void menuView() {

        cat1.setText("All");
        if(ids == 0){
            cat2.setText("Small");
            cat3.setText("Regular");
        } else if(ids == 1){
            cat2.setText("Regular");
            cat3.setText("Tall");
        } else if(ids == 2){
            cat2.setText("Bubble Tea");
            cat3.setText("Smoothie");
        } else if(ids == 3){
            cat2.setText("Ice Cream");
            cat3.setText("Cake");
        } else if(ids == 4){
            cat2.setText("Can");
            cat3.setText("Bottle");
        }

        clickCategory();
        recyclerview();
    }

    private void clickCategory() {
        cat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat1.setTextColor(Color.parseColor("#000000"));
                cat2.setTextColor(Color.parseColor("#C8CDD1"));
                cat3.setTextColor(Color.parseColor("#C8CDD1"));
            }
        });
        cat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat1.setTextColor(Color.parseColor("#C8CDD1"));
                cat2.setTextColor(Color.parseColor("#000000"));
                cat3.setTextColor(Color.parseColor("#C8CDD1"));
                if (ids == 0){
                } else if(ids == 1){
                } else if(ids == 2){
                } else if(ids == 3){
                } else if(ids == 4){
                }
            }
        });

        cat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat1.setTextColor(Color.parseColor("#C8CDD1"));
                cat2.setTextColor(Color.parseColor("#C8CDD1"));
                cat3.setTextColor(Color.parseColor("#000000"));
                if (ids == 0){
                } else if(ids == 1){
                } else if(ids == 2){
                } else if(ids == 3){
                } else if(ids == 4){
                }
            }
        });
    }

    private void recyclerview() {

        ArrayList<RvMenuModel> menu = new ArrayList<>();
        menu.add(new RvMenuModel(R.mipmap.coffee_foreground,"Coffee","Tall", 23.50));
        menu.add(new RvMenuModel(R.mipmap.ice_coffee_foreground,"Ice Coffee","Tall",13.10));
        menu.add(new RvMenuModel(R.mipmap.boba_foreground,"Smoothies","Tall",20.30));
        menu.add(new RvMenuModel(R.mipmap.ice_cream_foreground,"Dessert","Tall",33.60));
        menu.add(new RvMenuModel(R.mipmap.can_foreground,"Beverage","Tall",18.90));

        RvMenuAdapter menuAdapter = new RvMenuAdapter(this, menu);
        rvMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        rvMenu.setAdapter(menuAdapter);
    }
}