package com.example.ngopi.apps.rv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ngopi.R;
import com.example.ngopi.apps.AppCategoryActivity;

import java.util.ArrayList;

public class RvCatAdapter extends RecyclerView.Adapter<RvCatAdapter.RvCatHolder> {
    private final ArrayList<RvCatModel> category;
    int row_index = -1;

    public RvCatAdapter(Context context,ArrayList<RvCatModel> category) {
        this.category = category;
    }

    @NonNull
    @Override
        public RvCatAdapter.RvCatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_cat,parent,false);
        return new RvCatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvCatHolder holder, @SuppressLint("RecyclerView") int position) {
        RvCatModel currentCat = category.get(position);
        holder.imageView.setImageResource(currentCat.getImage());
        holder.textView.setText(currentCat.getText());

        holder.linearLayout.setOnClickListener(v -> {
            row_index = position;

            Intent intent = new Intent(v.getContext() , AppCategoryActivity.class);
            intent.putExtra("ids",holder.getLayoutPosition());
            v.getContext().startActivity(intent);
        });
        if (row_index == position){
            holder.linearLayout.setBackgroundResource(R.drawable.static_rv_bg);

        }
        else {
            holder.linearLayout.setBackgroundResource(R.drawable.static_rv_selected_bg);
        }

    }

    @Override
    public int getItemCount() {
        return category.size();
    }

    public static class RvCatHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageView imageView;
        LinearLayout linearLayout;


        public RvCatHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.text);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}