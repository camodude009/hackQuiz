package com.example.katharina.hackatum_ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

    }

    public void setTime(String time) {
        TextView txt = findViewById(R.id.textView6);
        txt.setText(time);
    }

    public void setTableNumber(int number) {
        TextView txt = findViewById(R.id.tableNum);
        txt.setText(number);
    }
}
