package com.example.ngopi.apps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.ngopi.R;
import com.example.ngopi.apps.rv.RvSearchAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class AppSearchActivity extends AppCompatActivity {

    ImageView search_button, back;
    EditText search;
    RecyclerView rcSearch;
    String userid;
    ArrayList<Map<String,Object>> menu = new ArrayList<>();
    RvSearchAdapter searchAdapter;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_search);

        userid = getIntent().getStringExtra("userid");

        search_button = findViewById(R.id.search_button);
        back = findViewById(R.id.back);
        search = findViewById(R.id.search);
        rcSearch = findViewById(R.id.rcSearch);

        recyclerView();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {



                ArrayList<Map<String,Object>> searchItems = new ArrayList<>();
                for(Map<String,Object> documentSnapshot : menu){
                    if(documentSnapshot.get("menu_name").toString().toLowerCase().contains(s.toString().toLowerCase())){
                        searchItems.add(documentSnapshot);
                    }
                }
                searchAdapter = new RvSearchAdapter(AppSearchActivity.this, searchItems,userid);
                rcSearch.setLayoutManager(new LinearLayoutManager(AppSearchActivity.this, LinearLayoutManager.VERTICAL,false));
                rcSearch.setAdapter(searchAdapter);

            }
        });

    }


    public void recyclerView(){

        db.collection("Category")
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
                                            for (QueryDocumentSnapshot documentMenu : taskMenu.getResult()){
                                                Map<String, Object> data = documentMenu.getData();
                                                data.put("category", documentCategory.get("name"));
                                                data.put("id", documentMenu.getId());
                                                menu.add(data);
                                            }
                                        }
                                    });
                        }
                         searchAdapter = new RvSearchAdapter(this, menu,userid);
                        rcSearch.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
                        rcSearch.setAdapter(searchAdapter);
                    }
                });


    }
}