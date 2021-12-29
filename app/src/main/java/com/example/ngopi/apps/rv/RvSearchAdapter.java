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

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ngopi.R;
import com.example.ngopi.apps.model.Order;
import com.example.ngopi.apps.model.OrderDetail;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RvSearchAdapter extends RecyclerView.Adapter<RvSearchAdapter.ViewHolder> {


    private List<Map<String,Object>> mData;
    private LayoutInflater mInflater;
    Context context;
    private Dialog menuDialog;
    private View view;
    String userid;
    String type = null;
    private double amount = 0;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // data is passed into the constructor
    public RvSearchAdapter(Context context, List<Map<String,Object>> data,String userid) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.userid = userid;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_app_menu,parent,false);

        menuDialog = new Dialog(view.getContext());
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Map<String,Object> animal = mData.get(position);
        holder.myTextView.setText(animal.get("menu_name").toString());
        holder.menu_price.setText("RM "+ animal.get("menu_price").toString());
        Glide.with(context).load(animal.get("menu_pic")).into(holder.imgView);

        if(!animal.get("category").toString().equals("Dessert") && !animal.get("menu_name").toString().equals("Mineral Water"))
        {
            holder.btnAdd.setOnClickListener(v -> {
                openDialog(animal);
            });
        } else {
            holder.btnAdd.setOnClickListener(v -> {
                if(animal.get("menu_name").toString().equals("Mineral Water")){
                    type = "Bottle";
                } else if(animal.get("menu_name").toString().contains("Cupcake")){
                    type = "Cupcake";
                } else if(animal.get("menu_name").toString().contains("Ice Cream")){
                    type = "Ice Cream";
                }
                amount = 0.00;
                toDatabase(animal,type,amount);
            });
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView myTextView, menu_price;
        ImageView btnAdd, imgView;

        public ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.menuTitle);
            menu_price = itemView.findViewById(R.id.menuPrice);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            imgView = itemView.findViewById(R.id.imgView);

        }
    }

    private void openDialog(Map<String,Object> currentItem) {

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

        lblItemMenu.setText(currentItem.get("menu_name").toString());
        Glide.with(context).load(currentItem.get("menu_pic").toString()).into(imageItemMenu);

        String[] arrayType =  new String[2];
        switch (currentItem.get("category").toString()) {
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
            switch (currentItem.get("category").toString()) {
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


    private void toDatabase(Map<String,Object> currentItem, String typeMenu, double amountMenu) {
        db.collection("Order")
                .whereEqualTo("userId",userid)
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
                                    userid,
                                    "",
                                    dtf.format(now),
                                    "",
                                    "In Cart"
                            );

                            dbOrder.add(order).addOnSuccessListener(documentReferenceOrder -> {
                                CollectionReference dbMenu = dbOrder.document(documentReferenceOrder.getId()).collection("Order Details");

                                OrderDetail orderDetail= new OrderDetail(
                                        currentItem.get("id").toString(),
                                        String.format(Locale.getDefault(),"%.2f", Double.parseDouble(currentItem.get("menu_price").toString()) + amountMenu),
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
                                    .whereEqualTo("menuId", currentItem.get("id").toString())
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
                                                        currentItem.get("id").toString(),
                                                        String.format(Locale.getDefault(),"%.2f", Double.parseDouble(currentItem.get("menu_price").toString()) + amountMenu),
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


}
