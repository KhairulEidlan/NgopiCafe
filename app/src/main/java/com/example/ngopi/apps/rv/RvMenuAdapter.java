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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ngopi.R;

import java.util.ArrayList;
import java.util.Locale;

public class RvMenuAdapter extends RecyclerView.Adapter<RvMenuAdapter.RvMenuHolder> {
    private Context context;
    private ArrayList<RvMenuModel> menuItem;

    public RvMenuAdapter(Context context, ArrayList<RvMenuModel> menuItem) {
        this.context = context;
        this.menuItem = menuItem;
    }

    @NonNull
    @Override
    public RvMenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_menu,parent,false);
        return new RvMenuHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvMenuHolder holder, int position) {
        RvMenuModel currentItem = menuItem.get(position);

        Glide.with(context).load(currentItem.getItemImage()).into(holder.imageView);
        holder.txtTitle.setText(currentItem.getItemName());
        holder.txtPrice.setText(String.format(Locale.getDefault(),"RM %.2f", currentItem.getItemPrice()));

        if(!currentItem.getItemCategory().equals("Dessert") && !currentItem.getItemName().equals("Mineral Water"))
        {
            holder.btnAdd.setOnClickListener(v -> {
                openDialog(v, currentItem);
            });
        } else {
            toDatabase(currentItem);
        }

        holder.btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItem == null ? 0:menuItem.size();
    }

    private void openDialog(View v, RvMenuModel currentItem) {

        Dialog menuDialog = new Dialog(v.getContext());
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_menu,null);

        menuDialog.setContentView(dialogView);
        menuDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(v.getContext(), R.drawable.dialog_background));
        menuDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        menuDialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextView lblItemMenu;
        ImageView imageItemMenu;
        Spinner spinnerMenuItem;

        lblItemMenu = dialogView.findViewById(R.id.lblItemMenu);
        imageItemMenu = dialogView.findViewById(R.id.imgItemMenu);
        spinnerMenuItem = dialogView.findViewById(R.id.spinnerMenuItem);

        lblItemMenu.setText(currentItem.getItemName());
        Glide.with(context).load(currentItem.getItemImage()).into(imageItemMenu);

        String[] arrayType =  new String[2];
        if(currentItem.getItemCategory().equals("Coffee")){
            arrayType = new String[]{"Small", "Regular"};
        } else if(currentItem.getItemCategory().equals("Ice Coffee")){
            arrayType = new String[]{"Regular","Tall" };
        } else if(currentItem.getItemCategory().equals("Smoothies")){
            arrayType = new String[]{"Regular","Tall" };
        } else if(currentItem.getItemCategory().equals("Beverage")){
            arrayType = new String[]{"Can","Bottle" };
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(dialogView.getContext(), android.R.layout.simple_spinner_item, arrayType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMenuItem.setAdapter(adapter);

        menuDialog.setCancelable(true);
        menuDialog.show();
    }

    private void toDatabase(RvMenuModel currentItem) {


    }

    public static class RvMenuHolder extends RecyclerView.ViewHolder{
        TextView txtTitle, txtType, txtPrice;
        ImageView imageView, btnAdd,btnFav;

        public RvMenuHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgView);
            txtTitle = itemView.findViewById(R.id.menuTitle);
            txtType = itemView.findViewById(R.id.menuType);
            txtPrice = itemView.findViewById(R.id.menuPrice);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnFav = itemView.findViewById(R.id.btnFav);
        }
    }
}
