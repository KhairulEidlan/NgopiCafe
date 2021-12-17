package com.example.ngopi.apps.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ngopi.R;
import com.example.ngopi.admin.AdminDashboardActivity;
import com.example.ngopi.apps.AppMainActivity;
import com.example.ngopi.loginsignup.LoginActivity;
import com.example.ngopi.object.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

public class Profile extends Fragment {

    private TextView fullname_pro,username_pro,email_pro,phonenum_pro,password_pro;
    TextView update;
    ImageView logout;
    User user= new User();
    private FirebaseFirestore db= FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        update = view.findViewById(R.id.update);

        logout = view.findViewById(R.id.logout);

        fullname_pro = view.findViewById(R.id.fullname_pro);
        username_pro = view.findViewById(R.id.username_pro);
        email_pro = view.findViewById(R.id.email_pro);
        phonenum_pro = view.findViewById(R.id.phonenum_pro);
        password_pro = view.findViewById(R.id.password_pro);
        profilepage();




        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
    public void Logout(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth.getInstance().signOut();
        user.delete();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }


}