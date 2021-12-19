package com.example.ngopi.loginsignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ngopi.R;
import com.example.ngopi.apps.AppMainActivity;
import com.example.ngopi.object.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SignupActivity extends AppCompatActivity {
    private Button sign, sign_in;
    private TextInputLayout fullname_reg,username_reg,email_reg,phonenum_reg,password_reg;

    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    LinearLayout layout1;
    User user= new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();

        //button
        sign = findViewById(R.id.sign);
        sign_in = findViewById(R.id.sign_in);

        //edit text
        fullname_reg = findViewById(R.id.fullname_reg);
        username_reg = findViewById(R.id.username_reg);
        email_reg = findViewById(R.id.email_reg);
        phonenum_reg = findViewById(R.id.phonenum_reg);
        password_reg = findViewById(R.id.password_reg);

        layout1 = findViewById(R.id.layout1);
        layout1.animate().translationY(-750).setDuration(1000);

        sign.setOnClickListener(view -> {
            if (validateFullname() && validateUsername() && validateEmail() && validatePhoneNum() && validatePassword() == true)
            {
                RegisterUser();
            }
            else{return;
            }
        });

        //go to login page
        sign_in.setOnClickListener(view -> {
            Intent intent = new Intent(SignupActivity.this ,LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.transition.fadein,R.transition.fadeout);
            finish();
        });
    }

    public boolean validateFullname(){
        String val = fullname_reg.getEditText().getText().toString();
        if (val.isEmpty()){
            fullname_reg.setError("field cannot be empty");
            return false;
        }
        else {
            fullname_reg.setError(null);
            fullname_reg.setErrorEnabled(false);
            return true;
        }
    }
    public boolean validateUsername(){
        String val = username_reg.getEditText().getText().toString();
        if (val.isEmpty()){
            username_reg.setError("field cannot be empty");
            return false;
        }
        else if (val.length()>=15){
            username_reg.setError("username too long");
            return false;

        }
        else {
            username_reg.setError(null);
            return true;
        }
    }
    public boolean validateEmail(){
        String val = email_reg.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()){
            email_reg.setError("field cannot be empty");
            return false;
        }
        else if (!val.matches(emailPattern)){
            email_reg.setError("Invalid email address");
            return false;
        }
        else {
            email_reg.setError(null);
            email_reg.setErrorEnabled(false);
            return true;
        }
    }
    public boolean validatePhoneNum(){
        String val = phonenum_reg.getEditText().getText().toString();
        if (val.isEmpty()){
            phonenum_reg.setError("field cannot be empty");
            return false;
        }
        else {
            phonenum_reg.setError(null);
            phonenum_reg.setErrorEnabled(false);
            return true;
        }
    }
    public boolean validatePassword(){
        String val = password_reg.getEditText().getText().toString();
        if (val.isEmpty()){
            password_reg.setError("field cannot be empty");
            return false;
        }
        else {
            password_reg.setError(null);
            password_reg.setErrorEnabled(false);
            return true;
        }
    }

    public void RegisterUser(){

        user.setFullname(fullname_reg.getEditText().getText().toString());
        user.setUsername(username_reg.getEditText().getText().toString());
        user.setEmail(email_reg.getEditText().getText().toString());
        user.setPhonenum(phonenum_reg.getEditText().getText().toString());
        user.setPassword(password_reg.getEditText().getText().toString());
        user.setUsertype("User");
        //check register email
        db.collection("Users").whereEqualTo("email",user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Toast.makeText(SignupActivity.this, "Email Already Register",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            db.collection("Users")
                                    .add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            mAuth.signInAnonymously();
                                            SharedPreferences.Editor editor = getSharedPreferences("UserPreferences", MODE_PRIVATE).edit();
                                            editor.putString("username", user.getUsername());
                                            editor.putString("usertype", "User");
                                            editor.apply();
                                            Toast.makeText(SignupActivity.this, "Welcome " + user.getUsername(),Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SignupActivity.this , AppMainActivity.class);
                                            intent.putExtra("username",user.getUsername().toString());
                                            startActivity(intent);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignupActivity.this, "Register Unsuccessfull",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    }
                });
    }
}