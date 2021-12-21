package com.example.ngopi.apps.rv;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ngopi.R;

import java.util.ArrayList;
import java.util.Locale;

public class RvAdminMenuAdapter extends RecyclerView.Adapter<RvAdminMenuAdapter.RvAdminMenuHolder> {
    private Context context;
    private ArrayList<RvMenuModel> menuItem;

    public RvAdminMenuAdapter(Context context, ArrayList<RvMenuModel> menuItem) {
        this.context = context;
        this.menuItem = menuItem;
    }
    @NonNull
    @Override
    public RvAdminMenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_admin_menu,parent,false);
        return new RvAdminMenuHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvAdminMenuAdapter.RvAdminMenuHolder holder, int position) {
        RvMenuModel currentItem = menuItem.get(position);

        Glide.with(context).load(currentItem.getItemImage()).into(holder.imageView);
        holder.Title.setText(currentItem.getItemName());
        holder.Price.setText(String.format(Locale.getDefault(),"RM %.2f", currentItem.getItemPrice()));

        holder.btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    @Override
    public int getItemCount() {
        return menuItem == null ? 0:menuItem.size();
    }

    private void toDatabase(RvMenuModel currentItem) {


    }

    public static class RvAdminMenuHolder extends RecyclerView.ViewHolder{
        TextView Title, Price;
        ImageView imageView, btnedit;
        SwitchCompat activebtn;

        public RvAdminMenuHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgView);
            Title = itemView.findViewById(R.id.title);
            Price = itemView.findViewById(R.id.price);
            btnedit = itemView.findViewById(R.id.btnedit);
        }
    }
}


