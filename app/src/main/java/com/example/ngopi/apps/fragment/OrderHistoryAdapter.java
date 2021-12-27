package com.example.ngopi.apps.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OrderHistoryAdapter extends FragmentStateAdapter {
    private Bundle bundle;


    public OrderHistoryAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, Bundle bundle) {
        super(fragmentManager, lifecycle);
        this.bundle = bundle;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if (position == 1) {
            HistoryOrder historyOrder = new HistoryOrder();
            historyOrder.setArguments(bundle);
            return  historyOrder;
        }

        OngoingOrder ongoingOrder = new OngoingOrder();
        ongoingOrder.setArguments(bundle);
        return  ongoingOrder;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
