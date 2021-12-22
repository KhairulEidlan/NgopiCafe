package com.example.ngopi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ngopi.admin.AdminDashboardActivity;
import com.example.ngopi.apps.rv.RvAdminMenuAdapter;
import com.example.ngopi.apps.rv.RvMenuAdapter;
import com.example.ngopi.apps.rv.RvMenuModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


public class AdminCategory extends AppCompatActivity {
    private String name;
    private int img;
    private RecyclerView menuitem;

    TextView category;
    ImageView bgimg;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        // Firestore Database
        db = FirebaseFirestore.getInstance();

        name = getIntent().getStringExtra("name");
        img = getIntent().getIntExtra("img",0);

        category = findViewById(R.id.category);
        bgimg = findViewById(R.id.bgimg);
        menuitem = findViewById(R.id.menuitem);

        category.setText(name);
        bgimg.setBackgroundResource(img);
//        Toast.makeText(AdminCategory.this, name,Toast.LENGTH_SHORT).show();

        recyclerview();

    }
    public void back(View view){
        Intent intent = new Intent(AdminCategory.this, AdminDashboardActivity.class);
        startActivity(intent);
    }
    private void recyclerview() {

        db.collection("Category")
                .whereEqualTo("name",name)
                .get()
                .addOnCompleteListener(taskCategory -> {
                    if (taskCategory.isSuccessful()){
                        for (QueryDocumentSnapshot documentCategory : taskCategory.getResult()){
                            documentCategory.getReference().collection("Menu")
                                    .get()
                                    .addOnCompleteListener(taskMenu -> {
                                        if (taskMenu.isSuccessful()){

                                            ArrayList<RvMenuModel> menu = new ArrayList<>();
                                            for (QueryDocumentSnapshot documentMenu : taskMenu.getResult()){
                                                menu.add(new RvMenuModel(
                                                                documentMenu.getId(),
                                                                name,
                                                                documentMenu.getData().get("menu_pic").toString(),
                                                                documentMenu.getData().get("menu_name").toString(),
                                                                documentMenu.getData().get("menu_price").toString()
                                                        )
                                                );
                                            }

                                            RvAdminMenuAdapter adminmenuAdapter = new RvAdminMenuAdapter(this, menu);
                                            menuitem.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
                                            menuitem.setAdapter(adminmenuAdapter);
                                        }
                                    });
                        }
                    }
                });
    }



}