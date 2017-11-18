package com.example.katharina.hackatum_ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.katharina.hackatum_ui.model.CountdownPacket;
import com.example.katharina.hackatum_ui.model.QuestionPacket;
import com.example.katharina.hackatum_ui.serverinterface.QuizClient;
import com.example.katharina.hackatum_ui.serverinterface.Serializer;

public class Start extends AppCompatActivity {
    private CountdownPacket countdown;
    final int abTitleId = getResources().getIdentifier("action_bar_title", "id", "android");


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        if (getIntent() != null && getIntent().getExtras() != null) {
            String jsonText = getIntent().getExtras().getString("JSON");
            countdown = (CountdownPacket) Serializer.deserializePacket(jsonText, CountdownPacket.class);
        }
        if (countdown != null) {
            TextView textView = findViewById(R.id.countdown);
            textView.setText(countdown.getTime());
        }


            System.out.println("HelloWorld hackatum start");
            // use this to start and trigger a service
            Intent i = new Intent(this, QuizClient.class);
            // potentially add data to the intent
            //i.putExtra("KEY1", "Value to be used by the service");
            startService(i);

    }

    public void setTime(CountdownPacket countdown) {
        this.countdown = countdown;
        TextView txt = findViewById(R.id.countdown);
        txt.setText(countdown.getTime());
    }

    public void setTableNumber(int number) {
        TextView txt = findViewById(R.id.tableNum);
        txt.setText(number);
    }


}
