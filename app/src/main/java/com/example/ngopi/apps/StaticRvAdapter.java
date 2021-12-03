package com.example.ngopi.apps;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;

public class StaticRvAdapter  extends RecyclerView.Adapter<StaticRvAdapter.StaticRvViewHolder>{
    private ArrayList<StaticRvModel> items;
    int row_index = -1;

    public StaticRvAdapter(ArrayList<StaticRvModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public StaticRvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.static_rv_item,parent,false);
        StaticRvViewHolder staticRVViewHolder = new StaticRvViewHolder(view);
        return staticRVViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StaticRvViewHolder holder, @SuppressLint("RecyclerView") int position) {
        StaticRvModel currentItem = items.get(position);
        holder.imageView.setImageResource(currentItem.getImage());
        holder.textView.setText(currentItem.getText());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index = position;
                notifyDataSetChanged();

                if (holder.getLayoutPosition()==0){
                    Intent intent = new Intent(v.getContext() ,Coffee_Item.class);
                    v.getContext().startActivity(intent);
                }
                else if (holder.getLayoutPosition()==1){
                    Intent intent = new Intent(v.getContext() , Ice_Coffee_Item.class);
                    v.getContext().startActivity(intent);
                }
                else if (holder.getLayoutPosition()==2){
                    Intent intent = new Intent(v.getContext() , Coffee_Item.class);
                    v.getContext().startActivity(intent);
                }
                else if (holder.getLayoutPosition()==3){
                    Intent intent = new Intent(v.getContext() , Coffee_Item.class);
                    v.getContext().startActivity(intent);
                }
                else if (holder.getLayoutPosition()==4){
                    Intent intent = new Intent(v.getContext() , Coffee_Item.class);
                    v.getContext().startActivity(intent);
                }
                else if (holder.getLayoutPosition()==5){
                    Intent intent = new Intent(v.getContext() , Coffee_Item.class);
                    v.getContext().startActivity(intent);
                }
            }
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
        return items.size();
    }

    public static class StaticRvViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageView imageView;
        LinearLayout linearLayout;


        public StaticRvViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.text);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
