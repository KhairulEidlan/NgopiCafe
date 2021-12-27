package com.example.ngopi.apps.rv;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ngopi.R;
import com.example.ngopi.apps.model.RvCart;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RvCartAdapter extends RecyclerView.Adapter<RvCartAdapter.RvCartHolder> {
    private final Context context;
    private final ArrayList<RvCart> cartItem;
    private final String username;

    String userId;

    private View view;
    private Dialog cartDialog;

    private FirebaseFirestore db;

    public RvCartAdapter(Context context, ArrayList<RvCart> cartItem, String username) {
        this.context = context;
        this.cartItem = cartItem;
        this.username = username;
    }

    @NonNull
    @Override
    public RvCartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_app_cart,parent,false);

        db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .whereEqualTo("username",username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            userId = document.getId();
                        }
                    }
                });

        cartDialog = new Dialog(view.getContext());

        return new RvCartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvCartHolder holder, int position) {
        RvCart currentItem = cartItem.get(position);

        Glide.with(context).load(currentItem.getItemImage()).into(holder.imageView);
        holder.txtTitle.setText(currentItem.getItemName()+" (x"+currentItem.getItemQty()+")");
        holder.txtType.setText(currentItem.getItemType());
        holder.txtPrice.setText(String.format(Locale.getDefault(),"RM %.2f", currentItem.getItemPrice()));

        holder.btnRemove.setOnClickListener(v -> openDialog(currentItem));
    }

    @Override
    public int getItemCount() {
        return cartItem == null ? 0:cartItem.size();
    }

    private void openDialog(RvCart currentItem) {
        view = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_app_cart,null);

        cartDialog.setContentView(view);
        cartDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.dialog_background));
        cartDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        cartDialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        Button btnAll, btnOne;

        btnAll = view.findViewById(R.id.btnAll);
        btnOne = view.findViewById(R.id.btnOne);

        btnAll.setOnClickListener(v -> toDatabase(currentItem,"all"));

        btnOne.setOnClickListener(v -> toDatabase(currentItem,"one"));

        cartDialog.setCancelable(true);
        cartDialog.show();
    }

    private void toDatabase(RvCart currentItem, String type) {

        db.collection("Order")
                .whereEqualTo("userId",userId)
                .whereEqualTo("status","In Cart")
                .get()
                .addOnCompleteListener(taskOrder -> {
                    if (taskOrder.isSuccessful()){
                        for (QueryDocumentSnapshot documentOrder : taskOrder.getResult()){
                            documentOrder.getReference()
                                    .collection("Order Details")
                                    .whereEqualTo("menuId",currentItem.getItemId())
                                    .whereEqualTo("type",currentItem.getItemType())
                                    .get()
                                    .addOnCompleteListener(taskMenuItem -> {
                                        if (taskMenuItem.isSuccessful()){
                                            for (QueryDocumentSnapshot documentMenuItem : taskMenuItem.getResult()){
                                                if(type.equals("one")){
                                                    int qty = Integer.parseInt(documentMenuItem.getData().get("quantity").toString());
                                                    if(qty > 1){
                                                        CollectionReference dbOrderDetail = db.collection("Order").document(documentOrder.getId()).collection("Order Details");

                                                        Map<String, Object> updateOrderDetail = new HashMap<>();
                                                        updateOrderDetail.put("quantity", qty - 1);

                                                        dbOrderDetail.document(documentMenuItem.getId())
                                                                .update(updateOrderDetail)
                                                                .addOnSuccessListener(unused -> {
                                                                    Toast.makeText(view.getContext(), "Successfully added to your order", Toast.LENGTH_SHORT).show();
                                                                    cartDialog.cancel();
                                                                });
                                                    } else {
                                                        db.collection("Order")
                                                                .document(documentOrder.getId())
                                                                .collection("Order Details")
                                                                .document(documentMenuItem.getId())
                                                                .delete()
                                                                .addOnSuccessListener(unused -> {
                                                                    Toast.makeText(view.getContext(), "Successfully delete item from your order", Toast.LENGTH_SHORT).show();
                                                                    cartDialog.cancel();
                                                                });
                                                    }
                                                } else if(type.equals("all")){
                                                    db.collection("Order")
                                                            .document(documentOrder.getId())
                                                            .collection("Order Details")
                                                            .document(documentMenuItem.getId())
                                                            .delete()
                                                            .addOnSuccessListener(unused -> {
                                                                Toast.makeText(view.getContext(), "Successfully delete item from your order", Toast.LENGTH_SHORT).show();
                                                                cartDialog.cancel();
                                                            });
                                                }
                                            }

                                        }
                                    });

                        }
                    }
                    else {
                        System.out.println("Error: "+ taskOrder.getException());
                    }
                });


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
