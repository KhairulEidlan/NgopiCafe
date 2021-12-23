package com.example.ngopi.apps.rv;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ngopi.AdminCategory;
import com.example.ngopi.EditAdminMenu;
import com.example.ngopi.R;
import com.example.ngopi.admin.AdminDashboardActivity;
import com.example.ngopi.apps.AppCategoryActivity;
import com.example.ngopi.loginsignup.LoginActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class RvAdminMenuAdapter extends RecyclerView.Adapter<RvAdminMenuAdapter.RvAdminMenuHolder> {
    private Context context;
    private ArrayList<RvMenuModel> menuItem;
    private String name;
    private int img;

    private View view;

    public RvAdminMenuAdapter(Context context, ArrayList<RvMenuModel> menuItem, String name, int img) {
        this.context = context;
        this.menuItem = menuItem;
        this.name = name;
        this.img = img;

    }
    @NonNull
    @Override
    public RvAdminMenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_admin_menu,parent,false);
        return new RvAdminMenuHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RvAdminMenuAdapter.RvAdminMenuHolder holder, int position) {
        RvMenuModel currentItem = menuItem.get(position);

        Glide.with(context).load(currentItem.getItemImage()).into(holder.imageView);
        holder.Title.setText(currentItem.getItemName());
        holder.Price.setText(String.format(Locale.getDefault(),"RM %.2f", Double.parseDouble(currentItem.getItemPrice())));

        holder.btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditAdminMenu.class);
                intent.putExtra("name",name);
                intent.putExtra("img",img);
                intent.putExtra("itemname",currentItem.getItemName());
                v.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return menuItem == null ? 0:menuItem.size();
    }

    public static class RvAdminMenuHolder extends RecyclerView.ViewHolder{
        TextView Title, Price;
        ImageView imageView, btnedit;

        public RvAdminMenuHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imgView);
            Title = itemView.findViewById(R.id.title);
            Price = itemView.findViewById(R.id.price);
            btnedit = itemView.findViewById(R.id.btnedit);
        }
    }
}


