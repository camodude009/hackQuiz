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

        String jsonText = getIntent().getExtras().getString("JSON");

        countdown = (CountdownPacket) Serializer.deserializePacket(jsonText, CountdownPacket.class);
        new Timer(countdown.ms/1000).start(); //FIXME

        TextView table = findViewById(R.id.tableNum);
        int tableNum = ((CustomApplication)getApplication()).getTableNum();
        table.setText(""+tableNum);
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }


    public void setCountdownTextUI(long left) {
        long minutes = left/60;
        long seconds = left%60;
        String formatted = String.format("%02d", minutes)+":"+String.format("%02d", seconds);
        TextView txt = (TextView)findViewById(R.id.countdownBeforeQuiz);
        txt.setText(formatted);
    }

    //Quick and dirty TODO clean
    private  class Timer extends Thread {
        long countdown = 440;

        public Timer(long countdown) {
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

