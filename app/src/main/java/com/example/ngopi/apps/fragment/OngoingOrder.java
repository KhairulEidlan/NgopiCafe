package com.example.ngopi.apps.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ngopi.R;
import com.example.ngopi.apps.model.RvOrder;
import com.example.ngopi.apps.rv.RvOrderHistoryAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class OngoingOrder extends Fragment {
    String username;

    View view;
    RecyclerView recyclerView;

    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_app_ongoing_order, container, false);

        Bundle bundle = this.getArguments();
        username = bundle.getString("username");

        recyclerView = view.findViewById(R.id.rcview);

        db = FirebaseFirestore.getInstance();

        displayOrder(view);

        return view;
    }

    private void displayOrder(View view) {

        db.collection("Users")
                .whereEqualTo("username",username)
                .get()
                .addOnCompleteListener(taskUser -> {
                    if (taskUser.isSuccessful()) {
                        for (QueryDocumentSnapshot documentUser : taskUser.getResult()) {
                            db.collection("Order")
                                    .whereEqualTo("userId",documentUser.getId())
                                    .orderBy("orderDate", Query.Direction.DESCENDING)
                                    .orderBy("orderNo",Query.Direction.DESCENDING)
                                    .get()
                                    .addOnCompleteListener(taskOrder -> {
                                        if (taskOrder.isSuccessful()) {
                                            ArrayList<RvOrder> order = new ArrayList<>();
                                            for (QueryDocumentSnapshot documentOrder : taskOrder.getResult()) {
                                                if (!documentOrder.getData().get("status").toString().equals("In Cart") && !documentOrder.getData().get("status").toString().equals("Complete")){
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
                                            }
                                            recyclerView.setAdapter(new RvOrderHistoryAdapter(getContext(),order));
                                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
                                        }
                                    });
                        }

                    }
                });


    }
}