package com.example.katharina.hackatum_ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.katharina.hackatum_ui.model.QuestionPacket;
import com.example.katharina.hackatum_ui.serverinterface.Serializer;

/**
 * Created by Katharina on 18/11/2017.
 */

public class Enter extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
    }
}
