package com.example.ngopi.apps.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ngopi.R;
import com.example.ngopi.admin.AdminDashboardActivity;
import com.example.ngopi.apps.AppMainActivity;
import com.example.ngopi.loginsignup.LoginActivity;
import com.example.ngopi.object.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Profile extends Fragment {

    private EditText fullname_pro,username_pro,email_pro,phonenum_pro,password_pro;
    TextView edit,save;
    ImageView logout;
    User user= new User();
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        edit = view.findViewById(R.id.edit);
        save = view.findViewById(R.id.save);

        logout = view.findViewById(R.id.logout);

        fullname_pro = view.findViewById(R.id.fullname_pro);
        username_pro = view.findViewById(R.id.username_pro);
        email_pro = view.findViewById(R.id.email_pro);
        phonenum_pro = view.findViewById(R.id.phonenum_pro);
        password_pro = view.findViewById(R.id.password_pro);

        fullname_pro.setEnabled(false);
        username_pro.setEnabled(false);
        email_pro.setEnabled(false);
        phonenum_pro.setEnabled(false);
        password_pro.setEnabled(false);
        profilepage();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilepage();
                fullname_pro.setEnabled(true);
                username_pro.setEnabled(true);
                email_pro.setEnabled(true);
                phonenum_pro.setEnabled(true);
                password_pro.setEnabled(true);
                edit.setVisibility(View.INVISIBLE);
                save.setVisibility(View.VISIBLE);


            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.setVisibility(View.VISIBLE);
                save.setVisibility(View.INVISIBLE);
                save();

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });

        return view;
    }
    public void profilepage(){

        Bundle bundle = this.getArguments();
        String username = bundle.getString("username",user.getUsername());

        db.collection("Users")
                .whereEqualTo("username",username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                uid = document.getId();
                                fullname_pro.setText(document.getData().get("fullname").toString());
                                username_pro.setText(document.getData().get("username").toString());
                                email_pro.setText(document.getData().get("email").toString());
                                phonenum_pro.setText(document.getData().get("phonenum").toString());
                                password_pro.setText(document.getData().get("password").toString());
                            }
                        }
                    }
                });
    }

    public void save(){

        Toast.makeText(getActivity(),fullname_pro.getText().toString(),Toast.LENGTH_SHORT).show();
        fullname_pro.setEnabled(false);
        username_pro.setEnabled(false);
        email_pro.setEnabled(false);
        phonenum_pro.setEnabled(false);
        password_pro.setEnabled(false);

        DocumentReference documentReference = db.collection("Users").document();

        documentReference
                .update("fullname",fullname_pro.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(),"success!",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),"Unsuccess!",Toast.LENGTH_SHORT).show();
                    }
                });

    }


    public void Logout(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth.getInstance().signOut();
        user.delete();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }


}