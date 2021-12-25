package com.example.ngopi.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ngopi.R;
import com.example.ngopi.loginsignup.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

public class AdminDashboardActivity extends AppCompatActivity {

    private FirebaseFirestore db= FirebaseFirestore.getInstance();

    String orderid;

    TextView orderRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        orderRequest = findViewById(R.id.orderRequest);

        showrequest();
    }

    public void showrequest(){
        db.collection("Order")
                .whereEqualTo("status","Payed")
                .get()
                .addOnCompleteListener(taskOrder -> {
                    if (taskOrder.isSuccessful()){
                        int i = 0;
                        for (QueryDocumentSnapshot documentCategory : taskOrder.getResult()){
                            orderid = documentCategory.getId();
                             i += 1;
                        }
                        orderRequest.setText("You Have " + i + " On Complete Order Request.");
                    }
                });
    }

    public void Request(View view){

    }

    public void coffee_category(View view){
        Intent intent = new Intent(AdminDashboardActivity.this, AdminCategory.class);
        intent.putExtra("name","Coffee");
        intent.putExtra("img",R.drawable.bg_coffee);
        startActivity(intent);
    }
    public void ice_coffee_category(View view){
        Intent intent = new Intent(AdminDashboardActivity.this, AdminCategory.class);
        intent.putExtra("name","Ice Coffee");
        intent.putExtra("img",R.drawable.bg_ice_coffee);
        startActivity(intent);
    }
    public void smoothies(View view){
        Intent intent = new Intent(AdminDashboardActivity.this, AdminCategory.class);
        intent.putExtra("name","Smoothies");
        intent.putExtra("img",R.drawable.bg_smoothies);
        startActivity(intent);
    }
    public void dessert(View view){
        Intent intent = new Intent(AdminDashboardActivity.this, AdminCategory.class);
        intent.putExtra("name","Dessert");
        intent.putExtra("img",R.drawable.bg_ice_coffee);
        startActivity(intent);
    }
    public void beverage(View view){
        Intent intent = new Intent(AdminDashboardActivity.this, AdminCategory.class);
        intent.putExtra("name","Beverage");
        intent.putExtra("img",R.drawable.bg_drinks);
        startActivity(intent);
    }
    public void Logout(View view){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth.getInstance().signOut();
        user.delete();
        Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void onBackPressed() {
    }
}