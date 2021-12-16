package com.example.ngopi.apps;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.ngopi.MainActivity;
import com.example.ngopi.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppPaymentActivity extends AppCompatActivity {
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

        txtTime = findViewById(R.id.txtTimePicker);
        txtTime.setOnClickListener(v -> {
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMinute = calendar.get(Calendar.MINUTE);

            timePickerDialog = new TimePickerDialog(AppPaymentActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
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
                }
            }, currentHour, currentMinute, false);

            timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            timePickerDialog.updateTime(tHour,tMinute);
            timePickerDialog.show();
        });
    }

    public void complete(View view) {
        Intent intent = new Intent(this, AppMainActivity.class);
        startActivity(intent);
        finish();
    }
}