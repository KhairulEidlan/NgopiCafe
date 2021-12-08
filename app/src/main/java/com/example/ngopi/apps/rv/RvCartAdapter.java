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

import com.example.ngopi.R;

import java.util.ArrayList;
import java.util.Locale;

public class RvCartAdapter extends RecyclerView.Adapter<RvCartAdapter.RvCartHolder> {
    private Context context;
    private ArrayList<RvCartModel> cartItem;

    public RvCartAdapter(Context context, ArrayList<RvCartModel> cartItem) {
        this.context = context;
        this.cartItem = cartItem;
    }

    @NonNull
    @Override
    public RvCartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_cart,parent,false);
        return new RvCartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvCartHolder holder, int position) {
        RvCartModel currentItem = cartItem.get(position);

        holder.imageView.setImageResource(currentItem.getImage());
        holder.txtTitle.setText(currentItem.getItemName());
        holder.txtType.setText(currentItem.getItemType());
        holder.txtPrice.setText(String.format(Locale.getDefault(),"RM %.2f", currentItem.getItemPrice()));

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"This is button "+holder.getLayoutPosition(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItem == null ? 0:cartItem.size();
    }

    public static class RvCartHolder extends RecyclerView.ViewHolder{

        TextView txtTitle, txtType, txtPrice;
        ImageView imageView, btnRemove;

        public RvCartHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgView);
            txtTitle = itemView.findViewById(R.id.menuTitle);
            txtType = itemView.findViewById(R.id.menuType);
            txtPrice = itemView.findViewById(R.id.menuPrice);
            btnRemove = itemView.findViewById(R.id.btnDelete);
        }
    }
}
