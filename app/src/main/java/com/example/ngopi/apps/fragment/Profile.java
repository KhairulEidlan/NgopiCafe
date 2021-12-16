package com.example.ngopi.apps.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ngopi.R;
import com.example.ngopi.admin.AdminDashboardActivity;
import com.example.ngopi.apps.AppMainActivity;
import com.example.ngopi.loginsignup.LoginActivity;
import com.example.ngopi.object.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

public class Profile extends Fragment {

    private TextView fullname_pro,username_pro,email_pro,phonenum_pro,password_pro;
    TextView update;
    User user= new User();
    private FirebaseFirestore db= FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        update = view.findViewById(R.id.update);

        fullname_pro = view.findViewById(R.id.fullname_pro);
        username_pro = view.findViewById(R.id.username_log);
        email_pro = view.findViewById(R.id.email_pro);
        phonenum_pro = view.findViewById(R.id.phonenum_pro);
        password_pro = view.findViewById(R.id.password_pro);
        profilepage();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), user.getUsername(),Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    public void profilepage(){


        db.collection("Users")
                .whereEqualTo("username",user.getUsername())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                fullname_pro.setText(document.getData().get("fullname").toString());
                                username_pro.setText(document.getData().get("username").toString());
                                email_pro.setText(document.getData().get("email").toString());
                            }
                        }
                    }
                });
    }


}