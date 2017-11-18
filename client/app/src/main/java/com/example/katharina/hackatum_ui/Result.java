package com.example.katharina.hackatum_ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.katharina.hackatum_ui.model.CountdownPacket;
import com.example.katharina.hackatum_ui.model.SummaryPacket;
import com.example.katharina.hackatum_ui.serverinterface.Serializer;

/**
 * Created by Katharina on 18/11/2017.
 */

public class Result extends AppCompatActivity {
    SummaryPacket summary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        String jsonText = getIntent().getExtras().getString("JSON");
        summary = (SummaryPacket) Serializer.deserializePacket(jsonText, SummaryPacket.class);
        init();
    }

    private void init(){
        showPoints(summary.correct, summary.total);
        setPlace(summary.place);
    }

    private void showPoints(int correct, int total){
        TextView summary = (TextView)findViewById(R.id.summary_text);
        summary.setText( ""+correct+"/"+total);
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
