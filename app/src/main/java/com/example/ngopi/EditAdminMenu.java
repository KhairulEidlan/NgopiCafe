package com.example.ngopi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ngopi.admin.AdminDashboardActivity;
import com.example.ngopi.apps.rv.RvAdminMenuAdapter;
import com.example.ngopi.apps.rv.RvMenuModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EditAdminMenu extends AppCompatActivity {

    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private StorageReference storage;

    String itemname,name,categoryid,menuid;
    int img;
    boolean check;
    EditText item_title, item_price;
    ImageView imgItem;
    SwitchCompat activebtn;
    Button btnCancel,btnConfirm;

    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_admin_menu);

        name = getIntent().getStringExtra("name");
        itemname = getIntent().getStringExtra("itemname");
        img = getIntent().getIntExtra("img",0);

        storage = FirebaseStorage.getInstance().getReference(name +"/");

        item_title = findViewById(R.id.item_title);
        item_price = findViewById(R.id.item_price);
        imgItem = findViewById(R.id.imgItem);

        activebtn = findViewById(R.id.activebtn);
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);

        imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        display();
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

            Picasso.get().load(mImageUri).into(imgItem);
        }
    }

    public void display(){
        db.collection("Category")
                .whereEqualTo("name",name)
                .get()
                .addOnCompleteListener(taskCategory -> {
                    if (taskCategory.isSuccessful()){
                        for (QueryDocumentSnapshot documentCategory : taskCategory.getResult()){
                            categoryid = documentCategory.getId();
                            documentCategory.getReference().collection("Menu")
                                    .whereEqualTo("menu_name",itemname)
                                    .get()
                                    .addOnCompleteListener(taskMenu -> {
                                        if (taskMenu.isSuccessful()){
                                            for (QueryDocumentSnapshot documentMenu : taskMenu.getResult()){
                                                menuid = documentMenu.getId();
                                                item_title.setText(documentMenu.getData().get("menu_name").toString());
                                                item_price.setText(documentMenu.getData().get("menu_price").toString());
                                                String link = documentMenu.getData().get("menu_pic").toString();
                                                Picasso.get().load(link).into(imgItem);
                                                if (documentMenu.getBoolean("is_active")==true){
                                                    activebtn.setChecked(true);
                                                }else{
                                                    activebtn.setChecked(false);
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void confirm(View view){
        if (activebtn.isChecked()){
            check = true;
        }else {
            check = false;
        }
        if (mImageUri != null) {
            StorageReference fileReference = storage.child(itemname + ".jpg");

            StorageTask mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    DocumentReference documentReference = db.collection("Category").document(categoryid).collection("Menu").document(menuid);
                                    documentReference
                                            .update(
                                                    "is_active", check,
                                                    "menu_name", item_title.getText().toString(),
                                                    "menu_price", item_price.getText().toString(),
                                                    "menu_pic", task.getResult().toString()
                                            );
                                }
                            });
                        }
                    });
            Toast.makeText(EditAdminMenu.this,"Update Successful!" , Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(EditAdminMenu.this,"Update UnSuccessful!" , Toast.LENGTH_SHORT).show();
        }

    }

    public void back(View view){
        Intent intent = new Intent(EditAdminMenu.this, AdminCategory.class);
        intent.putExtra("name",name);
        intent.putExtra("img",img);
        startActivity(intent);

    }
}