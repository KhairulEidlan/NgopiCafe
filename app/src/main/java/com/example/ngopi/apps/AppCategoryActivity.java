package com.example.ngopi.apps;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ngopi.R;
import com.example.ngopi.apps.rv.RvMenuAdapter;
import com.example.ngopi.apps.rv.RvMenuModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AppCategoryActivity extends AppCompatActivity {
    private Bundle bundle;
    private int ids, img;
    private String username;
    private String name;
    private RecyclerView rvMenu;
    private ImageView imgBackground;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_category);

        //hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //up button

        ids = getIntent().getIntExtra("ids",0);
        username = getIntent().getStringExtra("username");
        name = getIntent().getStringExtra("name");
        img = getIntent().getIntExtra("img",0);

        // Firestore Database
        db = FirebaseFirestore.getInstance();

        imgBackground = findViewById(R.id.imgBackground);
        rvMenu = findViewById(R.id.item);

        menuView();
    }

    private void menuView() {

        imgBackground.setBackgroundResource(img);

        recyclerview();
    }


    private void recyclerview() {

        db.collection("Category")
                .whereEqualTo("name",name)
                .get()
                .addOnCompleteListener(taskCategory -> {
                    if (taskCategory.isSuccessful()){
                        for (QueryDocumentSnapshot documentCategory : taskCategory.getResult()){
                            documentCategory.getReference().collection("Menu")
                                    .whereEqualTo("is_active",true)
                                    .whereEqualTo("is_deleted",false)
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

                                            RvMenuAdapter menuAdapter = new RvMenuAdapter(this, username, menu);
                                            rvMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
                                            rvMenu.setAdapter(menuAdapter);
                                        }
                            });
                        }
                    }
        });
    }
}