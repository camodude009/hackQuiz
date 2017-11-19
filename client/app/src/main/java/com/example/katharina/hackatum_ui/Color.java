package com.example.katharina.hackatum_ui;

import android.content.res.ColorStateList;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.katharina.hackatum_ui.model.ServiceRequestPacket;

/**
 * Created by Katharina on 18/11/2017.
 */

public class Color extends AppCompatActivity {
    private int selection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);
        init();
    }

    private void init() {
        selection = -1;

        final Button btnBack = findViewById(R.id.color_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(btnBack, -1);
            }
        });

        final Button button0 = findViewById(R.id.mood_black);
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(button0, 0);
            }
        });

        final Button button1 = findViewById(R.id.mood_brown);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(button1, 1);
            }
        });

        final Button button2 = findViewById(R.id.mood_red);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(button2, 2);
            }
        });

        final Button button3 = findViewById(R.id.mood_orange);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(button3, 3);
            }
        });

        final Button button4 = findViewById(R.id.mood_yellow);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(button4, 4);
            }
        });

        final Button button5 = findViewById(R.id.mood_green);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(button5, 5);
            }
        });

        final Button button6 = findViewById(R.id.mood_lightBlue);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(button6, 6);
            }
        });

        final Button button7 = findViewById(R.id.mood_blue);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(button7, 7);
            }
        });

        final Button button8 = findViewById(R.id.mood_pink);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(button8, 8);
            }
        });

    }

    private void selectButton(Button button, int selection) {
        this.selection = selection;
        String color = getRgb(selection);

        ServiceRequestPacket srp = new ServiceRequestPacket(ServiceRequestPacket.CHOOSE_COLOR, color);
        ((CustomApplication)getApplication()).getMessageQueue().add(srp);
        finish();
    }

    private String getRgb(int selection){
        switch (selection) {
            case 0: return color(0,0,0);
            case 1: return color(118,14, 204);
            case 2: return color(204, 2, 00);
            case 3: return color( 200, 40, 0);
            case 4: return color(  249, 73, 14);
            case 5: return color( 154, 204, 02);
            case 6: return color( 111, 191, 216);
            case 7: return color(48, 63, 159);
            default: return  color (250, 0, 250);
        }
    }

    public static String color(int r, int g, int b) {
        return "color:" + String.format("%03d", r & 0xff) + ","
                + String.format("%03d", g & 0xff) + ","
                + String.format("%03d", b & 0xff);
    }

}
