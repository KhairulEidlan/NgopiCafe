package com.example.ngopi.apps.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ngopi.R;
import com.example.ngopi.apps.AppPaymentActivity;
import com.example.ngopi.apps.rv.RvCartAdapter;
import com.example.ngopi.apps.model.RvCart;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Locale;


public class Cart extends Fragment {
    String username;
    final double  serviceCharge = 4.00;
    double subtotal;
    double total;

    RecyclerView recyclerView;
    TextView costService,costSubtotal,costTotal;
    Button btnCheckout;

    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_app_cart, container, false);

        recyclerView = view.findViewById(R.id.rcview);

        Bundle bundle = this.getArguments();
        username = bundle.getString("username");

        db = FirebaseFirestore.getInstance();

        costService = view.findViewById(R.id.cost_service);
        costSubtotal = view.findViewById(R.id.cost_subtotal);
        costTotal = view.findViewById(R.id.cost_total);

        btnCheckout = view.findViewById(R.id.btnCheckout);
        btnCheckout.setVisibility(View.INVISIBLE);

        displayCart(view);

        return view;
    }

    public void displayCart(View view) {

        db.collection("Users")
                .whereEqualTo("username",username)
                .get()
                .addOnCompleteListener(taskUser -> {
                    if (taskUser.isSuccessful()) {
                        for (QueryDocumentSnapshot documentUser : taskUser.getResult()) {
                            db.collection("Order")
                                    .whereEqualTo("userId",documentUser.getId())
                                    .whereEqualTo("status","In Cart")
                                    .get()
                                    .addOnCompleteListener(taskOrder -> {
                                        if (taskOrder.isSuccessful()) {
                                            for (QueryDocumentSnapshot documentOrder : taskOrder.getResult()) {
                                                db.collection("Order")
                                                    .document(documentOrder.getId())
                                                    .collection("Order Details")
                                                    .get()
                                                    .addOnCompleteListener(taskOrderDetail -> {
                                                        if (taskOrderDetail.isSuccessful()){
                                                            ArrayList<RvCart> item = new ArrayList<>();
                                                            for (QueryDocumentSnapshot documentOrderDetail : taskOrderDetail.getResult()){
                                                                db.collectionGroup("Menu")
                                                                        .get()
                                                                        .addOnCompleteListener(taskMenu -> {
                                                                            if (taskMenu.isSuccessful()){
                                                                                for (QueryDocumentSnapshot documentMenu : taskMenu.getResult()){
                                                                                    if(documentMenu.getId().equals(documentOrderDetail.getData().get("menuId"))){
                                                                                        item.add(new RvCart(
                                                                                                        documentMenu.getId(),
                                                                                                        documentMenu.getData().get("menu_pic").toString(),
                                                                                                        documentMenu.getData().get("menu_name").toString(),
                                                                                                        documentOrderDetail.getData().get("type").toString(),
                                                                                                        Integer.parseInt(documentOrderDetail.getData().get("quantity").toString()),
                                                                                                Double.parseDouble(documentOrderDetail.getData().get("price").toString())*Double.parseDouble(documentOrderDetail.getData().get("quantity").toString())
                                                                                                )
                                                                                        );
                                                                                        subtotal += Double.parseDouble(documentOrderDetail.getData().get("price").toString())*Double.parseDouble(documentOrderDetail.getData().get("quantity").toString());
                                                                                    }
                                                                                }
                                                                                recyclerView.setAdapter(new RvCartAdapter(getContext(),item, username));
                                                                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));

                                                                                total = subtotal + serviceCharge;
                                                                                costService.setText(String.format(Locale.getDefault(),"RM %.2f", serviceCharge));
                                                                                costSubtotal.setText(String.format(Locale.getDefault(),"RM %.2f", subtotal));
                                                                                costTotal.setText(String.format(Locale.getDefault(),"RM %.2f", total));

                                                                                btnCheckout.setVisibility(View.VISIBLE);
                                                                                btnCheckout.setOnClickListener(this::toPayment);
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    private void toPayment(View v) {
        Intent intent = new Intent(getContext(), AppPaymentActivity.class);
        intent.putExtra("username",username);
        intent.putExtra("total",total);
        startActivity(intent);
    }
}