package com.example.ngopi.apps.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ngopi.R;
import com.example.ngopi.apps.rv.RvCatAdapter;
import com.example.ngopi.apps.model.RvCat;

import java.util.ArrayList;

public class Home extends Fragment {
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Bundle bundle = this.getArguments();
        String username = bundle.getString("username");

        recyclerView = view.findViewById(R.id.rcview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));

        // Item
        ArrayList<RvCat> item = new ArrayList<>();
        item.add(new RvCat(R.mipmap.coffee_foreground,"Coffee"));
        item.add(new RvCat(R.mipmap.ice_coffee_foreground,"Ice Coffee"));
        item.add(new RvCat(R.mipmap.boba_foreground,"Smoothies"));
        item.add(new RvCat(R.mipmap.ice_cream_foreground,"Dessert"));
        item.add(new RvCat(R.mipmap.can_foreground,"Beverage"));

        recyclerView.setAdapter(new RvCatAdapter(getContext(),username, item));

        return view;
    }
}