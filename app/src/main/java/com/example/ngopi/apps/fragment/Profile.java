package com.example.ngopi.apps.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ngopi.OrderHistory;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Profile extends Fragment {

    private static final int image_request = 1;
    private EditText fullname_pro,username_pro,email_pro,phonenum_pro,password_pro;
    TextView edit,save,orderhistory;
    ImageView logout, profile_pic,add_profile;
    LinearLayout layout_addpic;
    String uid;

    User user= new User();

    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private StorageReference storage;

    private Uri mImageUri;
    private StorageTask mUploadTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // storage location
        storage = FirebaseStorage.getInstance().getReference("Users/");

        edit = view.findViewById(R.id.edit);
        save = view.findViewById(R.id.save);
        orderhistory = view.findViewById(R.id.orderhistory);
        layout_addpic = view.findViewById(R.id.layout_addpic);

        logout = view.findViewById(R.id.logout);
        profile_pic = view.findViewById(R.id.profile_pic);
        add_profile = view.findViewById(R.id.add_profile);

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

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        add_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilepage();
                fullname_pro.setEnabled(true);
                username_pro.setEnabled(true);
                email_pro.setEnabled(true);
                phonenum_pro.setEnabled(true);
                password_pro.setEnabled(true);

                layout_addpic.setVisibility(View.VISIBLE);
                edit.setVisibility(View.INVISIBLE);
                orderhistory.setVisibility(View.INVISIBLE);
                save.setVisibility(View.VISIBLE);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFullname() && validateUsername() && validateEmail() && validatePhoneNum() && validatePassword() == true)
                {
                    layout_addpic.setVisibility(View.INVISIBLE);
                    edit.setVisibility(View.VISIBLE);
                    save.setVisibility(View.INVISIBLE);
                    orderhistory.setVisibility(View.VISIBLE);
                    save();
                }
                else{return;
                }
            }
        });

        orderhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderHistory.class);
                startActivity(intent);
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
    private void openFileChooser(){
        Intent photoPickerIntent = new Intent();
        photoPickerIntent.setType("image/*");
        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(photoPickerIntent, 1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(profile_pic);
        }
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
                                String link = document.getData().get("imageURL").toString();
                                Picasso.get().load(link).into(profile_pic);

                            }
                        }
                    }
                });
    }
    public boolean validateFullname(){
        String val = fullname_pro.getText().toString();
        if (val.isEmpty()){
            fullname_pro.setError("field cannot be empty");
            return false;
        }
        else {
            fullname_pro.setError(null);
            return true;
        }
    }
    public boolean validateUsername(){
        String val = username_pro.getText().toString();
        if (val.isEmpty()){
            username_pro.setError("field cannot be empty");
            return false;
        }
        else if (val.length()>=15){
            username_pro.setError("username too long");
            return false;

        }
        else {
            username_pro.setError(null);
            return true;
        }
    }
    public boolean validateEmail(){
        String val = email_pro.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()){
            email_pro.setError("field cannot be empty");
            return false;
        }
        else if (!val.matches(emailPattern)){
            email_pro.setError("Invalid email address");
            return false;
        }
        else {
            email_pro.setError(null);
            return true;
        }
    }
    public boolean validatePhoneNum(){
        String val = phonenum_pro.getText().toString();
        if (val.isEmpty()){
            phonenum_pro.setError("field cannot be empty");
            return false;
        }
        else {
            phonenum_pro.setError(null);
            return true;
        }
    }
    public boolean validatePassword(){
        String val = password_pro.getText().toString();
        if (val.isEmpty()){
            password_pro.setError("field cannot be empty");
            return false;
        }
        else {
            password_pro.setError(null);
            return true;
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void save(){
        fullname_pro.setEnabled(false);
        username_pro.setEnabled(false);
        email_pro.setEnabled(false);
        phonenum_pro.setEnabled(false);
        password_pro.setEnabled(false);

        if (mImageUri != null) {
            StorageReference fileReference = storage.child(uid + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    DocumentReference documentReference = db.collection("Users").document(uid);
                                    documentReference
                                            .update(
                                                    "fullname",fullname_pro.getText().toString(),
                                                    "usename",username_pro.getText().toString(),
                                                    "email",email_pro.getText().toString(),
                                                    "phonenum",phonenum_pro.getText().toString(),
                                                    "password",password_pro.getText().toString(),
                                                    "imageURL",task.getResult().toString()

                                            );
                                    user.setImageURL(task.getResult().toString());
                                }
                            });
                        }
                    });

        } else {
            Toast.makeText(getActivity(),"Update UNSuccessful!",Toast.LENGTH_SHORT).show();
        }
    }

    public void Logout(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth.getInstance().signOut();
        user.delete();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }


}