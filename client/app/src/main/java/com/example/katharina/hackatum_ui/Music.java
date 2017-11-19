package com.example.katharina.hackatum_ui;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Katharina on 18/11/2017.
 */

public class Music  extends AppCompatActivity {
    int selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        init();
    }

    private void init(){
        selection = -1;

        Button button = findViewById(R.id.music_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Button button0 = findViewById(R.id.heart);
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(button0, 0);
            }
        });

        final Button button1 = findViewById(R.id.neutral_smiley);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(button1, 1);
            }
        });

        final Button button2 = findViewById(R.id.poop);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(button2, 2);
            }
        });

        final Button button3 = findViewById(R.id.handsUp);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(button3, 3);
            }
        });

        final Button button4 = findViewById(R.id.sunglasses);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(button4, 4);
            }
        });

        final Button button5 = findViewById(R.id.chilli);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(button5, 5);
            }
        });
    }

    private void selectButton(Button button, int selection) {
        this.selection = selection;
        button.setVisibility(View.INVISIBLE);
        finish();
    }


}
