package com.example.katharina.hackatum_ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

    @Override
    public void onBackPressed() {
        //do nothing
    }

    private void init(){
        showPoints(summary.correct, summary.total);
        setPlace(summary.place);

        Button fancy = findViewById(R.id.fancyButton);
        fancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dialogIntent = new Intent(Result.this, Bar.class);
                //dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);
            }
        });
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
        textView.setText("Your team won the "+place+addone+" place");
    }
}
