package com.example.ngopi.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.ngopi.R;
import com.example.ngopi.admin.rv.RvAdminMenuAdapter;
import com.example.ngopi.admin.rv.RvAdminOrder;
import com.example.ngopi.apps.model.RvOrder;
import com.example.ngopi.apps.rv.RvOrderHistoryAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminRequestOrder extends AppCompatActivity {

    RecyclerView rcOrder;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_request_order);

        rcOrder = findViewById(R.id.rcOrder);

        showOrder();
    }

    public void showOrder(){

        db.collection("Order")
                .whereEqualTo("status","Payed")
                .get()
                .addOnCompleteListener(taskOrder -> {
                    if (taskOrder.isSuccessful()) {

                        ArrayList<RvOrder> order = new ArrayList<>();

                        for (QueryDocumentSnapshot documentOrder : taskOrder.getResult()) {

                            userid = documentOrder.getData().get("userId").toString();
                                order.add(new RvOrder(
                                        documentOrder.getId(),
                                        documentOrder.getData().get("userId").toString(),
                                        documentOrder.getData().get("orderNo").toString(),
                                        documentOrder.getData().get("orderDate").toString(),
                                        documentOrder.getData().get("orderPickUp").toString(),
                                        documentOrder.getData().get("status").toString(),
                                        documentOrder.getData().get("amount").toString()
                                ));

                        }
                        RvAdminOrder adminOrder = new RvAdminOrder(this, order);
                        rcOrder.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
                        rcOrder.setAdapter(adminOrder);
                    }
                });

    }
    public void onBackPressed() {
        Intent intent = new Intent(AdminRequestOrder.this, AdminDashboardActivity.class);
        startActivity(intent);
    }
}