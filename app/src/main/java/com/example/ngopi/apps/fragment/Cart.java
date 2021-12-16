package com.example.ngopi.apps.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ngopi.R;
import com.example.ngopi.apps.AppPaymentActivity;
import com.example.ngopi.apps.rv.RvCartAdapter;
import com.example.ngopi.apps.rv.RvCartModel;

import java.util.ArrayList;


public class Cart extends Fragment {
    RecyclerView recyclerView;
    Button btnCheckout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.rcview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));

        // Item
        ArrayList<RvCartModel> item = new ArrayList<>();
        item.add(new RvCartModel(R.mipmap.coffee_foreground,"Coffee","Tall", 23.50));
        item.add(new RvCartModel(R.mipmap.ice_coffee_foreground,"Ice Coffee","Tall",13.10));
        item.add(new RvCartModel(R.mipmap.boba_foreground,"Smoothies","Tall",20.30));
        item.add(new RvCartModel(R.mipmap.ice_cream_foreground,"Dessert","Tall",33.60));
        item.add(new RvCartModel(R.mipmap.can_foreground,"Beverage","Tall",18.90));

        recyclerView.setAdapter(new RvCartAdapter(getContext(),item));

        btnCheckout = view.findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AppPaymentActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}