package com.example.ngopi.apps.rv;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ngopi.R;
import com.example.ngopi.apps.model.Order;
import com.example.ngopi.apps.model.OrderDetail;
import com.example.ngopi.apps.model.RvMenu;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RvMenuAdapter extends RecyclerView.Adapter<RvMenuAdapter.RvMenuHolder> {
    private final Context context;
    private final ArrayList<RvMenu> menuItem;
    private final String username;

    String userId;


    private View view;
    private Dialog menuDialog;

    private FirebaseFirestore db;

    String type = null;
    private double amount = 0;

    public RvMenuAdapter(Context context, String username, ArrayList<RvMenu> menuItem) {
        this.context = context;
        this.username = username;
        this.menuItem = menuItem;
    }

    @NonNull
    @Override
    public RvMenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_app_menu,parent,false);

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

        menuDialog = new Dialog(view.getContext());

        return new RvMenuHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvMenuHolder holder, int position) {
        RvMenu currentItem = menuItem.get(position);

        Glide.with(context).load(currentItem.getItemImage()).into(holder.imageView);
        holder.txtTitle.setText(currentItem.getItemName());
        holder.txtPrice.setText(String.format(Locale.getDefault(),"RM %.2f", Double.parseDouble(currentItem.getItemPrice())));

        if(!currentItem.getItemCategory().equals("Dessert") && !currentItem.getItemName().equals("Mineral Water"))
        {
            holder.btnAdd.setOnClickListener(v -> {
                openDialog(currentItem);
            });
        } else {
            holder.btnAdd.setOnClickListener(v -> {
                if(currentItem.getItemName().equals("Mineral Water")){
                    type = "Bottle";
                } else if(currentItem.getItemName().contains("Cupcake")){
                    type = "Cupcake";
                } else if(currentItem.getItemName().contains("Ice Cream")){
                    type = "Ice Cream";
                }
                amount = 0.00;
                toDatabase(currentItem,type,amount);
            });
        }
    }

    @Override
    public int getItemCount() {
        return menuItem == null ? 0:menuItem.size();
    }

    private void openDialog(RvMenu currentItem) {

        view = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_app_menu,null);

        menuDialog.setContentView(view);
        menuDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.dialog_background));
        menuDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        menuDialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextView lblItemMenu;
        ImageView imageItemMenu;
        Spinner spinnerMenuItem;
        TextView lblItemMenuInfo;
        Button btnConfirm;

        lblItemMenu = view.findViewById(R.id.lblItemMenu);
        imageItemMenu = view.findViewById(R.id.imgItemMenu);
        spinnerMenuItem = view.findViewById(R.id.spinnerMenuItem);
        lblItemMenuInfo = view.findViewById(R.id.lblItemMenuInfo);
        btnConfirm = view.findViewById(R.id.btnConfirm);

        lblItemMenu.setText(currentItem.getItemName());
        Glide.with(context).load(currentItem.getItemImage()).into(imageItemMenu);

        String[] arrayType =  new String[2];
        switch (currentItem.getItemCategory()) {
            case "Coffee":
                arrayType = new String[]{"Small", "Regular"};
                lblItemMenuInfo.setText("*For regular type, item will added RM 1.00");
                break;
            case "Ice Coffee":
            case "Smoothies":
                arrayType = new String[]{"Regular", "Tall"};
                lblItemMenuInfo.setText("*For tall type item will added RM 1.30");
                break;
            case "Beverage":
                arrayType = new String[]{"Can", "Bottle"};
                lblItemMenuInfo.setText("*For bottle type, item will added RM 0.50");
                break;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, arrayType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMenuItem.setAdapter(adapter);

        btnConfirm.setOnClickListener(v -> {
            type = spinnerMenuItem.getSelectedItem().toString();
            switch (currentItem.getItemCategory()) {
                case "Coffee":
                    if (spinnerMenuItem.getSelectedItem().toString().equals("Regular")) {
                        amount = 1.00;
                    }
                    break;
                case "Ice Coffee":
                case "Smoothies":
                    if (spinnerMenuItem.getSelectedItem().toString().equals("Tall")) {
                        amount = 1.30;
                    }
                    break;
                case "Beverage":
                    if (spinnerMenuItem.getSelectedItem().toString().equals("Bottle")) {
                        amount = 0.50;
                    }
                    break;
            }
            toDatabase(currentItem, type, amount);
        });

        menuDialog.setCancelable(true);
        menuDialog.show();
    }

    private void toDatabase(RvMenu currentItem, String typeMenu, double amountMenu) {
        db.collection("Order")
                .whereEqualTo("userId",userId)
                .whereEqualTo("status","In Cart")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        QuerySnapshot documentOrder = task.getResult();
                        if (documentOrder.isEmpty()){
                            CollectionReference dbOrder = db.collection("Order");

                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                            LocalDateTime now = LocalDateTime.now();

                            Order order = new Order(
                                    "",
                                    userId,
                                    "",
                                    dtf.format(now),
                                    "",
                                    "In Cart"
                            );

                            dbOrder.add(order).addOnSuccessListener(documentReferenceOrder -> {
                                CollectionReference dbMenu = dbOrder.document(documentReferenceOrder.getId()).collection("Order Details");

                                OrderDetail orderDetail= new OrderDetail(
                                        currentItem.getItemId(),
                                        String.format(Locale.getDefault(),"%.2f", Double.parseDouble(currentItem.getItemPrice()) + amountMenu),
                                        typeMenu,
                                        1
                                );

                                dbMenu.add(orderDetail).addOnSuccessListener(documentReferenceMenu -> {
                                    Toast.makeText(view.getContext(), "Successfully added to your order", Toast.LENGTH_SHORT).show();
                                    menuDialog.cancel();
                                });
                            });
                        }
                        else{
                            documentOrder.forEach(queryDocumentSnapshot -> db.collection("Order")
                                            .document(queryDocumentSnapshot.getId())
                                            .collection("Order Details")
                                            .whereEqualTo("menuId", currentItem.getItemId())
                                            .whereEqualTo("type",typeMenu)
                                            .get()
                                            .addOnCompleteListener(taskOrderDetail -> {
                                                if (taskOrderDetail.isSuccessful()) {
                                                    if (!taskOrderDetail.getResult().isEmpty()) {
                                                        for (QueryDocumentSnapshot documentOrderDetail : taskOrderDetail.getResult()) {
                                                            CollectionReference dbOrderDetail = db.collection("Order").document(queryDocumentSnapshot.getId()).collection("Order Details");

                                                            Map<String, Object> updateOrderDetail = new HashMap<>();
                                                            updateOrderDetail.put("quantity", Integer.parseInt(documentOrderDetail.getData().get("quantity").toString()) + 1);

                                                            dbOrderDetail.document(documentOrderDetail.getId())
                                                                    .update(updateOrderDetail)
                                                                    .addOnSuccessListener(unused -> {
                                                                        Toast.makeText(view.getContext(), "Successfully added to your order", Toast.LENGTH_SHORT).show();
                                                                        menuDialog.cancel();
                                                                    });
                                                        }

                                                    } else {
                                                        CollectionReference dbMenu = db.collection("Order").document(queryDocumentSnapshot.getId()).collection("Order Details");

                                                        OrderDetail orderDetail= new OrderDetail(
                                                                currentItem.getItemId(),
                                                                String.format(Locale.getDefault(),"%.2f", Double.parseDouble(currentItem.getItemPrice()) + amountMenu),
                                                                typeMenu,
                                                                1
                                                        );

                                                        dbMenu.add(orderDetail).addOnSuccessListener(documentReferenceMenu -> {
                                                            Toast.makeText(view.getContext(), "Successfully added to your order", Toast.LENGTH_SHORT).show();
                                                            menuDialog.cancel();
                                                        });
                                                    }
                                                }
                                            }));
                        }
                    }
                    else {
                        System.out.println("Error: "+ task.getException());
                    }
                });

    }

    public static class RvMenuHolder extends RecyclerView.ViewHolder{
        TextView txtTitle, txtType, txtPrice;
        ImageView imageView, btnAdd;

        public RvMenuHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgView);
            txtTitle = itemView.findViewById(R.id.menuTitle);
            txtType = itemView.findViewById(R.id.menuType);
            txtPrice = itemView.findViewById(R.id.menuPrice);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }
    }
}
