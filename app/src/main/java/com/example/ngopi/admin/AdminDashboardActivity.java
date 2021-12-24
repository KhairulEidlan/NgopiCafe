package com.example.ngopi.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ngopi.R;
import com.example.ngopi.loginsignup.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminDashboardActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

    }
    public void Logout(View view){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth.getInstance().signOut();
        user.delete();
        Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
        startActivity(intent);
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
}