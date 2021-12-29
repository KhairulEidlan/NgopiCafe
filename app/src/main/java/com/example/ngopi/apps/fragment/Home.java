package com.example.ngopi.apps.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ngopi.apps.AppSearchActivity;
import com.example.ngopi.R;
import com.example.ngopi.apps.model.Order;
import com.example.ngopi.apps.model.OrderDetail;
import com.example.ngopi.apps.rv.RvCategoryAdapter;
import com.example.ngopi.apps.model.RvCategory;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Home extends Fragment {

    //firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    RecyclerView recyclerView;
    ImageView profile_pic,search;

    //item
    ImageView imgView,btnAdd;
    TextView menuTitle,menuPrice;

    //item1
    ImageView imgView1,btnAdd1;
    TextView menuTitle1,menuPrice1;

    //item2
    ImageView imgView2,btnAdd2;
    TextView menuTitle2,menuPrice2;

    String username,userId;
    String link,link1,link2,profilelink;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_app_home, container, false);

        //get pass data username
        Bundle bundle = this.getArguments();
        username = bundle.getString("username");
        getuserid();

        recyclerView = view.findViewById(R.id.rcview);
        profile_pic = view.findViewById(R.id.profile_pic);
        search = view.findViewById(R.id.search);

        //item
        imgView = view.findViewById(R.id.imgView);
        menuTitle = view.findViewById(R.id.menuTitle);
        menuPrice = view.findViewById(R.id.menuPrice);
        btnAdd = view.findViewById(R.id.btnAdd);


        //item1
        imgView1 = view.findViewById(R.id.imgView1);
        menuTitle1 = view.findViewById(R.id.menuTitle1);
        menuPrice1 = view.findViewById(R.id.menuPrice1);
        btnAdd1 = view.findViewById(R.id.btnAdd1);


        //item2
        imgView2 = view.findViewById(R.id.imgView2);
        menuTitle2 = view.findViewById(R.id.menuTitle2);
        menuPrice2 = view.findViewById(R.id.menuPrice2);
        btnAdd2 = view.findViewById(R.id.btnAdd2);


        categories();
        showitem();
        showitem1();
        showitem2();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AppSearchActivity.class);
                intent.putExtra("userid", userId);
                startActivity(intent);
            }
        });

        return view;
    }


    public void getuserid(){
        db.collection("Users")
                .whereEqualTo("username",username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            userId = document.getId();
                            profilelink = document.getData().get("imageURL").toString();
                            Picasso.get().load(profilelink).into(profile_pic);
                        }
                    }
                });
    }

    public void categories(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));

        // Item
        ArrayList<RvCategory> item = new ArrayList<>();
        item.add(new RvCategory(R.mipmap.coffee_foreground,"Coffee"));
        item.add(new RvCategory(R.mipmap.ice_coffee_foreground,"Ice Coffee"));
        item.add(new RvCategory(R.mipmap.boba_foreground,"Smoothies"));
        item.add(new RvCategory(R.mipmap.ice_cream_foreground,"Dessert"));
        item.add(new RvCategory(R.mipmap.can_foreground,"Beverage"));

        recyclerView.setAdapter(new RvCategoryAdapter(getContext(),username, item));

    }

    public void showitem(){

        db.collection("Category")
                .whereEqualTo("name","Coffee")
                .get()
                .addOnCompleteListener(taskCategory -> {
                    if (taskCategory.isSuccessful()){
                        for (QueryDocumentSnapshot documentCategory : taskCategory.getResult()){
                            documentCategory.getReference().collection("Menu")
                                    .whereEqualTo("menu_name","Caramel Latte")
                                    .whereEqualTo("is_active",true)
                                    .get()
                                    .addOnCompleteListener(taskMenu -> {
                                        if (taskMenu.isSuccessful()){
                                            for (QueryDocumentSnapshot documentMenu : taskMenu.getResult()){
                                                menuTitle.setText(documentMenu.getData().get("menu_name").toString());
                                                menuPrice.setText("RM "+documentMenu.getData().get("menu_price").toString());
                                                link = documentMenu.getData().get("menu_pic").toString();
                                                Picasso.get().load(link).into(imgView);
                                            }
                                        }
                                    });
                        }
                    }
                });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(getActivity());
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_app_menu,null);
                dialog.setContentView(view);
                dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.dialog_background));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                dialog.show();
                dialog.setCancelable(true);

                TextView lblItemMenu;
                ImageView imageItemMenu;
                Spinner spinnerMenuItem;
                TextView lblItemMenuInfo;
                Button btnConfirm;

                String[] arraytype = {"Small","Regular"};
                double amount;

                lblItemMenu = view.findViewById(R.id.lblItemMenu);
                imageItemMenu = view.findViewById(R.id.imgItemMenu);
                spinnerMenuItem = view.findViewById(R.id.spinnerMenuItem);
                lblItemMenuInfo = view.findViewById(R.id.lblItemMenuInfo);
                btnConfirm = view.findViewById(R.id.btnConfirm);

                lblItemMenu.setText(menuTitle.getText());
                lblItemMenuInfo.setText("*For regular type, item will added RM 1.00");
                Picasso.get().load(link).into(imageItemMenu);

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arraytype);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMenuItem.setAdapter(dataAdapter);
                if (spinnerMenuItem.getSelectedItem().toString().equals("Regular")){
                    amount = 1.00;
                }else{
                    amount = 0;}

                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                                                        "0YSzRBnzF0kzDhFZ4CcY",
                                                        String.format(Locale.getDefault(),"%.2f", Double.parseDouble("5.10") + amount),
                                                        spinnerMenuItem.getSelectedItem().toString(),
                                                        1
                                                );

                                                dbMenu.add(orderDetail).addOnSuccessListener(documentReferenceMenu -> {
                                                    Toast.makeText(view.getContext(), "Successfully added to your order", Toast.LENGTH_SHORT).show();
                                                    dialog.cancel();
                                                });
                                            });
                                        }
                                        else{
                                            documentOrder.forEach(queryDocumentSnapshot -> db.collection("Order")
                                                    .document(queryDocumentSnapshot.getId())
                                                    .collection("Order Details")
                                                    .whereEqualTo("menuId", "0YSzRBnzF0kzDhFZ4CcY")
                                                    .whereEqualTo("type",spinnerMenuItem.getSelectedItem().toString())
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
                                                                                dialog.cancel();
                                                                            });
                                                                }

                                                            } else {
                                                                CollectionReference dbMenu = db.collection("Order").document(queryDocumentSnapshot.getId()).collection("Order Details");

                                                                OrderDetail orderDetail= new OrderDetail(
                                                                        "0YSzRBnzF0kzDhFZ4CcY",
                                                                        String.format(Locale.getDefault(),"%.2f", Double.parseDouble("5.10") + amount),
                                                                        spinnerMenuItem.getSelectedItem().toString(),
                                                                        1
                                                                );

                                                                dbMenu.add(orderDetail).addOnSuccessListener(documentReferenceMenu -> {
                                                                    Toast.makeText(view.getContext(), "Successfully added to your order", Toast.LENGTH_SHORT).show();
                                                                    dialog.cancel();
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
                });

            }
        });

    }

    public void showitem1(){

        db.collection("Category")
                .whereEqualTo("name","Dessert")
                .get()
                .addOnCompleteListener(taskCategory -> {
                    if (taskCategory.isSuccessful()){
                        for (QueryDocumentSnapshot documentCategory : taskCategory.getResult()){
                            documentCategory.getReference().collection("Menu")
                                    .whereEqualTo("menu_name","Red Velvet Cupcake")
                                    .whereEqualTo("is_active",true)
                                    .get()
                                    .addOnCompleteListener(taskMenu -> {
                                        if (taskMenu.isSuccessful()){
                                            for (QueryDocumentSnapshot documentMenu : taskMenu.getResult()){
                                                menuTitle1.setText(documentMenu.getData().get("menu_name").toString());
                                                menuPrice1.setText("RM "+documentMenu.getData().get("menu_price").toString());
                                                link1 = documentMenu.getData().get("menu_pic").toString();
                                                Picasso.get().load(link1).into(imgView1);
                                            }
                                        }
                                    });
                        }
                    }
                });

        btnAdd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                                "JkV2Ino8PaFlEDqGYFb0",
                                                String.format(Locale.getDefault(),"%.2f", Double.parseDouble("3.80")),
                                                "Cupcake",
                                                1
                                        );

                                        dbMenu.add(orderDetail).addOnSuccessListener(documentReferenceMenu -> {
                                            Toast.makeText(getContext(), "Successfully added to your order", Toast.LENGTH_SHORT).show();
                                        });
                                    });
                                }
                                else{
                                    documentOrder.forEach(queryDocumentSnapshot -> db.collection("Order")
                                            .document(queryDocumentSnapshot.getId())
                                            .collection("Order Details")
                                            .whereEqualTo("menuId", "JkV2Ino8PaFlEDqGYFb0")
                                            .whereEqualTo("type","Cupcake")
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
                                                                        Toast.makeText(getContext(), "Successfully added to your order", Toast.LENGTH_SHORT).show();
                                                                    });
                                                        }

                                                    } else {
                                                        CollectionReference dbMenu = db.collection("Order").document(queryDocumentSnapshot.getId()).collection("Order Details");

                                                        OrderDetail orderDetail= new OrderDetail(
                                                                "JkV2Ino8PaFlEDqGYFb0",
                                                                String.format(Locale.getDefault(),"%.2f", Double.parseDouble("3.80")),
                                                                "Cupcake",
                                                                1
                                                        );

                                                        dbMenu.add(orderDetail).addOnSuccessListener(documentReferenceMenu -> {
                                                            Toast.makeText(getContext(), "Successfully added to your order", Toast.LENGTH_SHORT).show();
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
        });

    }

    public void showitem2(){

        db.collection("Category")
                .whereEqualTo("name","Smoothies")
                .get()
                .addOnCompleteListener(taskCategory -> {
                    if (taskCategory.isSuccessful()){
                        for (QueryDocumentSnapshot documentCategory : taskCategory.getResult()){
                            documentCategory.getReference().collection("Menu")
                                    .whereEqualTo("menu_name","Mango Smoothies")
                                    .whereEqualTo("is_active",true)
                                    .get()
                                    .addOnCompleteListener(taskMenu -> {
                                        if (taskMenu.isSuccessful()){
                                            for (QueryDocumentSnapshot documentMenu : taskMenu.getResult()){
                                                menuTitle2.setText(documentMenu.getData().get("menu_name").toString());
                                                menuPrice2.setText("RM "+documentMenu.getData().get("menu_price").toString());
                                                link2 = documentMenu.getData().get("menu_pic").toString();
                                                Picasso.get().load(link2).into(imgView2);
                                            }
                                        }
                                    });
                        }
                    }
                });

        btnAdd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(getActivity());
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_app_menu,null);
                dialog.setContentView(view);
                dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.dialog_background));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
                dialog.show();
                dialog.setCancelable(true);

                TextView lblItemMenu;
                ImageView imageItemMenu;
                Spinner spinnerMenuItem;
                TextView lblItemMenuInfo;
                Button btnConfirm;

                String[] arraytype = {"Regular","Tall"};
                double amount;

                lblItemMenu = view.findViewById(R.id.lblItemMenu);
                imageItemMenu = view.findViewById(R.id.imgItemMenu);
                spinnerMenuItem = view.findViewById(R.id.spinnerMenuItem);
                lblItemMenuInfo = view.findViewById(R.id.lblItemMenuInfo);
                btnConfirm = view.findViewById(R.id.btnConfirm);

                lblItemMenu.setText(menuTitle2.getText());
                lblItemMenuInfo.setText("*For tall type item will added RM 1.30");
                Picasso.get().load(link2).into(imageItemMenu);

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arraytype);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMenuItem.setAdapter(dataAdapter);
                if (spinnerMenuItem.getSelectedItem().toString().equals("Tall")){
                    amount = 1.30;
                }else{
                    amount = 0;}

                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                                                        "GCnGzOPDcLF2F2wewEq7",
                                                        String.format(Locale.getDefault(),"%.2f", Double.parseDouble("5.90") + amount),
                                                        spinnerMenuItem.getSelectedItem().toString(),
                                                        1
                                                );

                                                dbMenu.add(orderDetail).addOnSuccessListener(documentReferenceMenu -> {
                                                    Toast.makeText(view.getContext(), "Successfully added to your order", Toast.LENGTH_SHORT).show();
                                                    dialog.cancel();
                                                });
                                            });
                                        }
                                        else{
                                            documentOrder.forEach(queryDocumentSnapshot -> db.collection("Order")
                                                    .document(queryDocumentSnapshot.getId())
                                                    .collection("Order Details")
                                                    .whereEqualTo("menuId", "GCnGzOPDcLF2F2wewEq7")
                                                    .whereEqualTo("type",spinnerMenuItem.getSelectedItem().toString())
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
                                                                                dialog.cancel();
                                                                            });
                                                                }

                                                            } else {
                                                                CollectionReference dbMenu = db.collection("Order").document(queryDocumentSnapshot.getId()).collection("Order Details");

                                                                OrderDetail orderDetail= new OrderDetail(
                                                                        "GCnGzOPDcLF2F2wewEq7",
                                                                        String.format(Locale.getDefault(),"%.2f", Double.parseDouble("5.90") + amount),
                                                                        spinnerMenuItem.getSelectedItem().toString(),
                                                                        1
                                                                );

                                                                dbMenu.add(orderDetail).addOnSuccessListener(documentReferenceMenu -> {
                                                                    Toast.makeText(view.getContext(), "Successfully added to your order", Toast.LENGTH_SHORT).show();
                                                                    dialog.cancel();
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
                });

            }
        });

    }

}