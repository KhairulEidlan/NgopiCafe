package com.example.ngopi.apps.rv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"This is button "+currentItem.getItemId(),Toast.LENGTH_LONG).show();
            }
        });

        holder.btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"This is button "+currentItem.getItemId(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItem == null ? 0:menuItem.size();
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
