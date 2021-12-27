package com.example.ngopi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.firestore.FirebaseFirestore;

public class AppSearchActivity extends AppCompatActivity {

    ImageView search_button, back;
    EditText search;
    RecyclerView rcSearch;
    String userid;

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
    }
}