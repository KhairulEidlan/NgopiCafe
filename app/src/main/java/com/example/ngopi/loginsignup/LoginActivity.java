package com.example.ngopi.loginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ngopi.R;
import com.example.ngopi.admin.AdminDashboardActivity;
import com.example.ngopi.apps.AppMainActivity;
import com.example.ngopi.object.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {
    Button login_btn ,signup_btn;
    TextInputLayout username_log, password_log;
    LinearLayout layout1;

    User user= new User();

    private FirebaseFirestore db= FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //button
        signup_btn = findViewById(R.id.signup_btn);
        login_btn = findViewById(R.id.login_btn);

        //edit text
        username_log = findViewById(R.id.username_log);
        password_log = findViewById(R.id.password_log);

        layout1 = findViewById(R.id.layout1);
        layout1.animate().translationY(-450).setDuration(1000);
    }
    public void register(View v){
        Intent intent = new Intent(LoginActivity.this ,SignupActivity.class);
        startActivity(intent);
        overridePendingTransition(R.transition.fadein,R.transition.fadeout);
        finish();
    }

    public void login(View view){
        user.setUsername(username_log.getEditText().getText().toString());
        user.setPassword(password_log.getEditText().getText().toString());
        Validatepage();
    }

    public void Validatepage(){
        db.collection("Users")
                .whereEqualTo("username",user.getUsername())
                .whereEqualTo("password",user.getPassword())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.get("usertype").equals("Admin")){
                                    Toast.makeText(LoginActivity.this, "Welcome Back " + user.getUsername(),Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this , AdminDashboardActivity.class);
                                    startActivity(intent);
                                }
                                else if (document.get("usertype").equals("User")){
                                    Toast.makeText(LoginActivity.this, "Welcome Back " + user.getUsername(),Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this , AppMainActivity.class);
                                    startActivity(intent);
                                }
                            }
                            if (task.getResult().isEmpty()){
                                Toast.makeText(LoginActivity.this, "UserName and Password Not Found2",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}