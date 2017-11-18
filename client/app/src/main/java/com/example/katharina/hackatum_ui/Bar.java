package com.example.katharina.hackatum_ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.katharina.hackatum_ui.model.ServiceRequestPacket;

/**
 * Created by Katharina on 18/11/2017.
 */

public class Bar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar);
        init();
    }

    private void init(){
        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             finish();
            }
        });

        final CustomApplication ca = (CustomApplication)getApplication();

        Button bartender = findViewById(R.id.bartender);
        bartender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             ca.getMessageQueue().add(new ServiceRequestPacket(ServiceRequestPacket.CALL_BARKEEPER));
                Toast.makeText(Bar.this, "The bartender will be with you shortly", Toast.LENGTH_SHORT).show();
            }
        });

        Button order = findViewById(R.id.order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ca.getMessageQueue().add(new ServiceRequestPacket(ServiceRequestPacket.CALL_ORDER_DRINK));
                Toast.makeText(Bar.this, "The waiter will be with you shortly", Toast.LENGTH_SHORT).show();
            }
        });

        Button drink_recommend = findViewById(R.id.drink_recommend);
        drink_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO transition to activity_drink_recommend
            }
        });

        Button colour = findViewById(R.id.colour);
        colour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO transition to activity_colour
            }
        });

        Button music = findViewById(R.id.music);
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO transition to activity_music
            }
        });
    }

}
