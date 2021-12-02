package com.example.ngopi.apps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.ngopi.R;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private StaticRvAdapter staticRvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ArrayList<StaticRvModel> item = new ArrayList<>();
        item.add(new StaticRvModel(R.mipmap.coffee_foreground,"Coffee"));
        item.add(new StaticRvModel(R.mipmap.ice_coffee_foreground,"Ice Coffee"));
        item.add(new StaticRvModel(R.mipmap.boba_foreground,"Boba Tea"));
        item.add(new StaticRvModel(R.mipmap.ice_cream_foreground,"Ice Cream"));
        item.add(new StaticRvModel(R.mipmap.sandwich_foreground,"Sandwich"));
        item.add(new StaticRvModel(R.mipmap.can_foreground,"Soda Can"));

        recyclerView = findViewById(R.id.rv_1);
        staticRvAdapter = new StaticRvAdapter(item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(staticRvAdapter);
    }
}