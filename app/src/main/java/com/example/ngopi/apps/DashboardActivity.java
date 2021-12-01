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
        item.add(new StaticRvModel(R.drawable.coffee,"Coffee"));
        item.add(new StaticRvModel(R.drawable.ice_coffee,"Ice Coffee"));
        item.add(new StaticRvModel(R.drawable.boba,"Boba Tea"));
        item.add(new StaticRvModel(R.drawable.ice_cream,"Ice Cream"));
        item.add(new StaticRvModel(R.drawable.sandwish,"Sandwish"));
        item.add(new StaticRvModel(R.drawable.can,"Soda Can"));

        recyclerView = findViewById(R.id.rv_1);
        staticRvAdapter = new StaticRvAdapter(item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(staticRvAdapter);
    }
}