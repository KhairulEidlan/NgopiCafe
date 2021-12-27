package com.example.ngopi.apps.fragment;

import android.app.Dialog;
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

import com.example.ngopi.R;
import com.example.ngopi.apps.rv.RvCategoryAdapter;
import com.example.ngopi.apps.model.RvCategory;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Home extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;
    ImageView imgView,btnAdd;
    TextView menuTitle,menuPrice;


    String username;
    String link;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_app_home, container, false);

        //get pass data username
        Bundle bundle = this.getArguments();
        username = bundle.getString("username");

        recyclerView = view.findViewById(R.id.rcview);
        imgView = view.findViewById(R.id.imgView);
//        imgView1 = view.findViewById(R.id.imgView1);
//        imgView2 = view.findViewById(R.id.imgView2);
        menuTitle = view.findViewById(R.id.menuTitle);
        menuPrice = view.findViewById(R.id.menuPrice);
        btnAdd = view.findViewById(R.id.btnAdd);


        categories();

        showitem1();
        return view;
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

    public void showitem1(){
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
                String type ;
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
                }

                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(getActivity(), spinnerMenuItem.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                        //masuk db

                    }
                });

            }
        });

    }

}