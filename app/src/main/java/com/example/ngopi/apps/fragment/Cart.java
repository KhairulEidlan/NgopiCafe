package com.example.ngopi.apps.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ngopi.R;
import com.example.ngopi.apps.RvCatAdapter;
import com.example.ngopi.apps.RvCatModel;

import java.util.ArrayList;


public class Cart extends Fragment {
    RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.rcview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));

        // Item
        ArrayList<RvCatModel> item = new ArrayList<>();
        item.add(new RvCatModel(R.mipmap.coffee_foreground,"Coffee"));
        item.add(new RvCatModel(R.mipmap.ice_coffee_foreground,"Ice Coffee"));
        item.add(new RvCatModel(R.mipmap.boba_foreground,"Smoothies"));
        item.add(new RvCatModel(R.mipmap.ice_cream_foreground,"Dessert"));
        item.add(new RvCatModel(R.mipmap.can_foreground,"Beverage"));

        recyclerView.setAdapter(new RvCatAdapter(getContext(),item));

        return view;
    }
}