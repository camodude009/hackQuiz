package com.example.katharina.hackatum_ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Katharina on 18/11/2017.
 */

public class Result extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }

    private void init(int place, int[] points){
        showPoints(points);
        setPlace(place);
    }

    private void showPoints(int[] points){
        //TODO add Rounds / Points
    }

    private void setPlace(int place){
        TextView textView = findViewById(R.id.place);
        String addone = "th";
        if(place == 1) addone = "st";
        else if(place == 2) addone = "nd";
        else if(place == 3) addone = "rd";
        textView.setText("Your team won the "+place+addone+"place");
    }
}
