package com.example.katharina.hackatum_ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.katharina.hackatum_ui.model.CountdownPacket;
import com.example.katharina.hackatum_ui.model.QuestionPacket;
import com.example.katharina.hackatum_ui.model.RegisterPacket;
import com.example.katharina.hackatum_ui.serverinterface.QuizClient;
import com.example.katharina.hackatum_ui.serverinterface.Serializer;

public class Start extends AppCompatActivity {
    private CountdownPacket countdown;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        if (getIntent() != null && getIntent().getExtras() != null) {
            String jsonText = getIntent().getExtras().getString("JSON");
            countdown = (CountdownPacket) Serializer.deserializePacket(jsonText, CountdownPacket.class);
        }
        if (countdown != null) {
            //TextView textView = findViewById(R.id.countdown);
            //textView.setText(countdown.getTime());
        }

        //RegisterPacket rp = new RegisterPacket(007, "Drop table");
        //((CustomApplication)getApplication()).getMessageQeuue().add(rp);

        // use this to start and trigger a service
        //Intent i = new Intent(this, QuizClient.class);
        // potentially add data to the intent
        //i.putExtra("KEY1", "Value to be used by the service");
       // startService(i);

        new Timer(65).start();

    }


    public void setCountdownTextUI(int left) {
        int minutes = left/60;
        int seconds = left%60;
        String formatted = String.format("%02d", minutes)+":"+String.format("%02d", seconds);
        TextView txt = (TextView)findViewById(R.id.countdownBeforeQuiz);
        txt.setText(formatted);
    }

    //Quick and dirty TODO clean
    private  class Timer extends Thread {
        int countdown = 10;

        public Timer(int countdown) {
            this.countdown = countdown;
        }

        @Override
        public void run() {
            while( countdown>0 ) {
                countdown--;
                //System.out.println(countdown);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setCountdownTextUI(countdown);
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}

