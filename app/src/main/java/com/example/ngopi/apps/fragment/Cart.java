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
import com.example.ngopi.apps.rv.RvCartModel;
import com.example.ngopi.object.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;


public class Cart extends Fragment {
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
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.rcview);

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

        db.collection("Order")
                .whereEqualTo("user_id","RbVFEeg2mNtZwElyllx1")
                .get()
                .addOnCompleteListener(taskCart -> {
                    if (taskCart.isSuccessful()){
                        ArrayList<RvCartModel> item = new ArrayList<>();
                        for (QueryDocumentSnapshot documentCart : taskCart.getResult()){

                            db.collectionGroup("Menu")
                                    .get()
                                    .addOnCompleteListener(taskMenu -> {
                                        if (taskMenu.isSuccessful()){

                                            for (QueryDocumentSnapshot documentMenu : taskMenu.getResult()){
                                                if(documentMenu.getId().equals(documentCart.getData().get("menu_id"))){
                                                    item.add(new RvCartModel(
                                                                    documentMenu.getData().get("menu_pic").toString(),
                                                                    documentMenu.getData().get("menu_name").toString(),
                                                                    Double.parseDouble(documentMenu.getData().get("menu_price").toString())
                                                            )
                                                    );
                                                    subtotal += Double.parseDouble(documentMenu.getData().get("menu_price").toString());
                                                }
                                            }
                                            recyclerView.setAdapter(new RvCartAdapter(getContext(),item));
                                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));

                                            total = subtotal + serviceCharge;
                                            costService.setText(String.format(Locale.getDefault(),"RM %.2f", serviceCharge));
                                            costSubtotal.setText(String.format(Locale.getDefault(),"RM %.2f", subtotal));
                                            costTotal.setText(String.format(Locale.getDefault(),"RM %.2f", total));

                                            btnCheckout.setVisibility(View.VISIBLE);
                                            btnCheckout.setOnClickListener(v -> {
                                                Intent intent = new Intent(getContext(), AppPaymentActivity.class);
                                                intent.putExtra("total",total);
                                                startActivity(intent);
                                            });
                                        }
                                    });
                        }
                    }
                    costService.setText("RM 0.00");
                    costSubtotal.setText("RM 0.00");
                    costTotal.setText("RM 0.00");
                });
    }
}