package com.example.katharina.hackatum_ui;

import android.content.Intent;
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
        Button back = findViewById(R.id.bar_back);
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

        Button color = findViewById(R.id.colour);
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button fancy = findViewById(R.id.colour);
                fancy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent dialogIntent = new Intent(Bar.this, Color.class);
                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(dialogIntent);
                    }
                });
            }
        });

        Button music = findViewById(R.id.music);
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Button fancy = findViewById(R.id.music);
                fancy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent dialogIntent = new Intent(Bar.this, Music.class);
                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(dialogIntent);
                    }
                });
            }
        });
    }

}
