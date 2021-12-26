package com.example.ngopi.apps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ngopi.MainActivity;
import com.example.ngopi.R;
import com.example.ngopi.loginsignup.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AppPaymentActivity extends AppCompatActivity {
    String username, orderNo;
    double totalCheckout;

    private FirebaseFirestore db;

    TextView lblTotal, lblOrderNo;
    EditText txtTime;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour,currentMinute,tHour,tMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_app_payment);

        username = getIntent().getStringExtra("username");
        totalCheckout = getIntent().getDoubleExtra("total",0);
        orderNo = getIntent().getStringExtra("orderNo");

        db = FirebaseFirestore.getInstance();

        lblTotal = findViewById(R.id.lblTotal);
        lblTotal.append(String.format(Locale.getDefault(),"RM %.2f", totalCheckout));

        lblOrderNo = findViewById(R.id.lblOrderNo);
        lblOrderNo.append(orderNo);

        txtTime = findViewById(R.id.txtTimePicker);
        txtTime.setOnClickListener(v -> {
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);

            timePickerDialog = new TimePickerDialog(
                    AppPaymentActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    (view, hourOfDay, minute) -> {
                        // Initialize hour and minute
                        tHour = hourOfDay;
                        tMinute = minute;

                        // Store hour and minute in string
                        String time = tHour + ":" + tMinute;

                        // Initialize 24 hours time format
                        SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");

                        try {
                            Date date = f24Hours.parse(time);

                            SimpleDateFormat f12Hours = new SimpleDateFormat("hh:mm aa");

                            txtTime.setText(f12Hours.format(date));

                        } catch (ParseException e){
                            e.printStackTrace();
                        }
                    },
                    currentHour,
                    currentMinute,
                    false);

            timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            timePickerDialog.updateTime(tHour,tMinute);
            timePickerDialog.show();
        });
    }



    public void complete(View view) {
        if (!txtTime.getText().toString().isEmpty()){
            try {
                db.collection("Users")
                        .whereEqualTo("username",username)
                        .get()
                        .addOnCompleteListener(taskUser -> {
                            if (taskUser.isSuccessful()) {
                                for (QueryDocumentSnapshot documentUser : taskUser.getResult()) {
                                    db.collection("Order")
                                            .whereEqualTo("userId",documentUser.getId())
                                            .whereEqualTo("status","In Cart")
                                            .get()
                                            .addOnCompleteListener(taskOrder -> {
                                                if (taskOrder.isSuccessful()){
                                                    for (QueryDocumentSnapshot documentOrder : taskOrder.getResult()){
                                                        System.out.println("Order ID: "+documentOrder.getId());
                                                        DocumentReference dbOrder = db.collection("Order").document(documentOrder.getId());

                                                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                                                        LocalDateTime now = LocalDateTime.now();

                                                        Map<String, Object> updateOrder = new HashMap<>();
                                                        updateOrder.put("orderNo", orderNo);
                                                        updateOrder.put("amount", String.format(Locale.getDefault(),"%.2f", totalCheckout));
                                                        updateOrder.put("orderPickUp", txtTime.getText().toString());
                                                        updateOrder.put("orderDate", dtf.format(now));
                                                        updateOrder.put("status","Payed");

                                                        dbOrder.update(updateOrder).addOnSuccessListener(unused -> {
                                                            Toast.makeText(this, "The payment is success", Toast.LENGTH_SHORT).show();
                                                            SharedPreferences prefs = getSharedPreferences("UserPreferences", MODE_PRIVATE);
                                                            String username = prefs.getString("username","");
                                                            Intent intent = new Intent(AppPaymentActivity.this, AppMainActivity.class);
                                                            intent.putExtra("username", username);
                                                            startActivity(intent);
                                                            finish();
                                                        });
                                                    }
                                                }
                                            });
                                }
                            }
                        });
            } catch (Exception ex){
                Toast.makeText(this, "Error in processing payment", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please select time to pickup your order", Toast.LENGTH_SHORT).show();
        }

    }
}