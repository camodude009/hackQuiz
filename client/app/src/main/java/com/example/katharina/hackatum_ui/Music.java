package com.example.katharina.hackatum_ui;

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

        button = findViewById(R.id.heart);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selection = 0;
                finish();
            }
        });

        button = findViewById(R.id.neutral_smiley);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selection = 1;
                finish();
            }
        });

        button = findViewById(R.id.poop);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selection = 2;
                finish();
            }
        });

        button = findViewById(R.id.handsUp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selection = 3;
                finish();
            }
        });

        button = findViewById(R.id.sunglasses);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selection = 4;
                finish();
            }
        });

        button = findViewById(R.id.chilli);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selection = 5;
                finish();
            }
        });
    }


}
