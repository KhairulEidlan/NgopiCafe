package com.example.ngopi.apps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    RadioGroup rgPaymentMethod;
    RadioButton rbSelectedMethod;

    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour,currentMinute,tHour,tMinute;

    private Dialog paymentDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_app_payment);

        username = getIntent().getStringExtra("username");
        totalCheckout = getIntent().getDoubleExtra("total",0);

        db = FirebaseFirestore.getInstance();

        generateOrderNo();

        lblTotal = findViewById(R.id.lblTotal);
        lblTotal.append(String.format(Locale.getDefault(),"RM %.2f", totalCheckout));

        lblOrderNo = findViewById(R.id.lblOrderNo);

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

        rgPaymentMethod = findViewById(R.id.paymentMethod);

        paymentDialog = new Dialog(this);
    }

    private void generateOrderNo() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        ZonedDateTime now = LocalDateTime.now().atZone(ZoneId.of("Asia/Kuala_Lumpur"));

        ArrayList<String> status= new ArrayList<>();
        status.add("In Cart");
        status.add("Payed");


        db.collection("Order")
                .whereIn("status",status)
                .whereEqualTo("orderDate",dtf.format(now))
                .get()
                .addOnCompleteListener(taskOrder -> {
                    if (taskOrder.isSuccessful()){
                        if (taskOrder.getResult().size() == 1){
                            orderNo = "001";
                        } else {
                            int counter = 1;
                            for (QueryDocumentSnapshot ignored : taskOrder.getResult()){
                                counter++;
                            }

                            if (counter < 10){
                                orderNo = "00"+ counter;
                            } else if (counter < 100){
                                orderNo = "0"+ counter;
                            } else if(counter < 1000){
                                orderNo = String.valueOf(counter);
                            }
                        }
                        lblOrderNo.append(orderNo);
                    }
                });
    }

    public void complete(View view) {
        int selectedPaymentMethodId = rgPaymentMethod.getCheckedRadioButtonId();

        if (!txtTime.getText().toString().isEmpty() && selectedPaymentMethodId != -1){

            rbSelectedMethod = findViewById(selectedPaymentMethodId);

            if(rbSelectedMethod.getText().toString().equals("Credit Card")){
                view = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_app_payment,null);

                paymentDialog.setContentView(view);
                paymentDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.dialog_background));
                paymentDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                paymentDialog.getWindow().getAttributes().windowAnimations = R.style.animation;


                EditText creditCardNo,cardDate,cardCVC;
                Button btnOk;

                creditCardNo = view.findViewById(R.id.creditCardNo);
                cardDate = view.findViewById(R.id.cardDate);
                cardCVC = view.findViewById(R.id.cardCVC);
                btnOk = view.findViewById(R.id.btnOk);

                btnOk.setOnClickListener(v -> {
                    if(!creditCardNo.getText().toString().isEmpty() && !cardDate.getText().toString().isEmpty() && !cardCVC.getText().toString().isEmpty()){
                        if (creditCardNo.length() == 16 && cardDate.length() == 5 && cardCVC.length() == 3){
                            if(creditCardNo.getText().toString().matches("4[0-9]+") || creditCardNo.getText().toString().matches("5[1-4]+[0-9]+")){
                                //visa start with 4
                                //mastercard start with 51,52,53,54
                                if (cardDate.getText().toString().matches("0[1-9]+/[2-9]+[2-9]")||cardDate.getText().toString().matches("1[0-2]+/[2-9]+[2-9]")){
                                    toDatabase();
                                }
                                else{
                                    Toast.makeText(v.getContext(), "Credit Card is Declined", Toast.LENGTH_SHORT).show();
                                }

                            }
//                            4111111111111111
                            else {
                                Toast.makeText(v.getContext(), "Visa Or Mastercard is Declined", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(v.getContext(), "Credit Card Sufficient Number!!!", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        Toast.makeText(v.getContext(), "Please insert your credit card number to proceed", Toast.LENGTH_SHORT).show();
                    }
                });

                paymentDialog.setCancelable(true);
                paymentDialog.show();
            } else {
                toDatabase();
            }
        } else {
            Toast.makeText(this, "Please select time to pickup your order and your payment method", Toast.LENGTH_SHORT).show();
        }

    }

    private void toDatabase() {

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
    }
}