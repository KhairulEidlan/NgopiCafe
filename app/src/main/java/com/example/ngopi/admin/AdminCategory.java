package com.example.ngopi.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ngopi.R;
import com.example.ngopi.admin.rv.RvAdminMenuAdapter;
import com.example.ngopi.apps.model.RvMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AdminCategory extends AppCompatActivity {
    private String name,id;
    private int img;
    private RecyclerView menuitem;
    private ProgressBar progressBar;

    TextView category;
    ImageView bgimg;
    private Uri uri;

    //Dialog box declare
    EditText item_title,item_price;
    ImageView imgItem;
    SwitchCompat activebtn;
    Button btnConfirm, btnCancel;

    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        //take pass data
        name = getIntent().getStringExtra("name");
        img = getIntent().getIntExtra("img",0);

        // Firestore Database
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference(name +"/");

        category = findViewById(R.id.category);
        bgimg = findViewById(R.id.bgimg);
        menuitem = findViewById(R.id.menuitem);
        progressBar = findViewById(R.id.progress_bar);

        category.setText(name);
        bgimg.setBackgroundResource(img);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFdead00,
                android.graphics.PorterDuff.Mode.MULTIPLY);

        recyclerview();

    }
    public void back(View view){
        Intent intent = new Intent(AdminCategory.this, AdminDashboardActivity.class);
        startActivity(intent);
    }
    public void additem(View view){
        showDialog();

    }

    public void showDialog(){
        Dialog dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_admin_menu,null);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.dialog_background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog.show();
        dialog.setCancelable(true);

        item_title = view.findViewById(R.id.item_title);
        item_price = view.findViewById(R.id.item_price);
        imgItem = view.findViewById(R.id.imgItem);

        activebtn = view.findViewById(R.id.activebtn);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        btnCancel = view.findViewById(R.id.btnCancel);

        imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item_title.getText().toString().isEmpty() && item_price.getText().toString().isEmpty()){
                    item_title.setError("field cannot be empty");
                    item_price.setError("field cannot be empty");
                    return;
                }
                else{
                    if(uri !=null){
                        addintodb();
                    }
                    else{
                        Toast.makeText(AdminCategory.this,"Picture is Empty!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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
            uri = data.getData();
            Picasso.get().load(uri).into(imgItem);
        }
    }

    public void addintodb(){

        // search if item already have
        db.collection("Category")
                .whereEqualTo("name",name)
                .get()
                .addOnCompleteListener(taskCategory -> {
                    if (taskCategory.isSuccessful()){
                        for (QueryDocumentSnapshot documentCategory : taskCategory.getResult()){
                            documentCategory.getReference().collection("Menu")
                                    .whereEqualTo("menu_name",item_title.getText().toString())
                                    .get()
                                    .addOnCompleteListener(taskMenu -> {
                                        if (taskMenu.isSuccessful()){
                                            for (QueryDocumentSnapshot documentMenu : taskMenu.getResult()){
                                                Toast.makeText(AdminCategory.this,"Item Already have" , Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            //proceed to add image to storage if item doesn't have in db
                                            StorageReference fileReference = storage.child(item_title.getText().toString() + ".jpg");
                                            StorageTask mUploadTask = fileReference.putFile(uri)
                                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Uri> task) {

                                                                    //proceed to add data to database
                                                                    Map<String, Object> item = new HashMap<>();
                                                                    item.put("is_active",activebtn.isChecked());
                                                                    item.put("is_deleted","false");
                                                                    item.put("menu_name",item_title.getText().toString());
                                                                    item.put("menu_pic",task.getResult().toString());
                                                                    item.put("menu_price",item_price.getText().toString());

                                                                    db.collection("Category").document(id).collection("Menu")
                                                                            .add(item)
                                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                @Override
                                                                                public void onSuccess(DocumentReference documentReference) {
                                                                                    Toast.makeText(AdminCategory.this,"Successful Added" , Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });
                                                                }
                                                            });
                                                        }
                                                    });


                                        }
                                    });
                        }
                    }
                });
    }

    private void recyclerview() {

        progressBar.setProgress(0);
        db.collection("Category")
                .whereEqualTo("name",name)
                .get()
                .addOnCompleteListener(taskCategory -> {
                    if (taskCategory.isSuccessful()){
                        for (QueryDocumentSnapshot documentCategory : taskCategory.getResult()){
                            progressBar.setProgress(80);
                            id= documentCategory.getId();
                            documentCategory.getReference().collection("Menu")
                                    .get()
                                    .addOnCompleteListener(taskMenu -> {
                                        if (taskMenu.isSuccessful()){

                                            ArrayList<RvMenu> menu = new ArrayList<>();
                                            for (QueryDocumentSnapshot documentMenu : taskMenu.getResult()){
                                                menu.add(new RvMenu(
                                                                documentMenu.getId(),
                                                                name,
                                                                documentMenu.getData().get("menu_pic").toString(),
                                                                documentMenu.getData().get("menu_name").toString(),
                                                                documentMenu.getData().get("menu_price").toString()
                                                        )
                                                );
                                            }

                                            RvAdminMenuAdapter adminmenuAdapter = new RvAdminMenuAdapter(this, menu,name,img);
                                            menuitem.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
                                            menuitem.setAdapter(adminmenuAdapter);
                                        }
                                    });
                        }
                    }
                });
    }
    public void onBackPressed() {
        Intent intent = new Intent(AdminCategory.this, AdminDashboardActivity.class);
        startActivity(intent);
    }
}