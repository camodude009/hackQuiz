package com.example.katharina.hackatum_ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.katharina.hackatum_ui.model.AnswerPacket;
import com.example.katharina.hackatum_ui.model.QuestionPacket;
import com.example.katharina.hackatum_ui.serverinterface.Serializer;

/**
 * Created by Katharina on 18/11/2017.
 */

public class Question extends AppCompatActivity {

    private int answer;
    private boolean answered = false;
    private static final int BAR_RESOLUTION = 5000;

    QuestionPacket question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        String jsonText = getIntent().getExtras().getString("JSON");
        question = (QuestionPacket)Serializer.deserializePacket(jsonText, QuestionPacket.class);
        if(question!=null) init();

        System.out.println(jsonText);
    }

    private void init(){
        setQuestion();

        //INitalize color states
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed
        };
        int standartColor = getResources().getColor(R.color.Color_Quiz_Standard);
        int[] standartColorList = new int[] {standartColor,standartColor,standartColor,standartColor};
        final ColorStateList standartColorState = new ColorStateList(states, standartColorList);
        int highlightColor = getResources().getColor(R.color.Color_Quiz_Highlight);
        int[] highlightColorList = new int[] {highlightColor,highlightColor,highlightColor,highlightColor};
        final ColorStateList highlightColorState = new ColorStateList(states, highlightColorList);



        Button fancy = findViewById(R.id.fancyButton);
        fancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dialogIntent = new Intent(Question.this, Bar.class);
                //dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);
            }
        });

        //Relay answers

        final Button answerA1 = (Button)findViewById(R.id.answer1);
        answerA1.setOnClickListener(new Button.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                sendAnswer(0);
                resetColors(standartColorState);
                answerA1.setBackgroundTintList(highlightColorState);
            }
        });
        final Button answerA2 = (Button)findViewById(R.id.answer2);
        answerA2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAnswer(1);
                resetColors(standartColorState);
                answerA2.setBackgroundTintList(highlightColorState);
            }
        });
        final Button answerA3 = (Button)findViewById(R.id.answer3);
        answerA3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAnswer(2);
                resetColors(standartColorState);
                answerA3.setBackgroundTintList(highlightColorState);
            }
        });
        final Button answerA4 = (Button)findViewById(R.id.answer4);
        answerA4.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAnswer(3);
                resetColors(standartColorState);
                answerA4.setBackgroundTintList(highlightColorState);
            }
        });

        ProgressBar countdownBar = findViewById(R.id.countdown_bar);
        long max = (question.ms);
        countdownBar.setMax(BAR_RESOLUTION);
        countdownBar.setProgress(BAR_RESOLUTION);
        new Timer(max).start();
    }

//    public void nextQuestion(){ //Frage eingeben -> Don via client
//        ProgressBar pb = findViewById(R.id.progressBar);
//        pb.setProgress(pb.getProgress()+1);
//        setQuestion();
//    }

    private void setQuestion(){
        if(question != null){
            TextView qNum = findViewById(R.id.questionNum);
            qNum.setText("Question "+(question.num+1)+"/"+question.total);

            TextView textView = findViewById(R.id.question);
            textView.setText(question.question);

            Button answer = findViewById(R.id.answer1);
            answer.setText(question.a);

            answer = findViewById(R.id.answer2);
            answer.setText(question.b);

            answer = findViewById(R.id.answer3);
            answer.setText(question.c);

            answer = findViewById(R.id.answer4);
            answer.setText(question.d);

        }
    }

    private void resetColors(ColorStateList csl) {
        Button answer1 = findViewById(R.id.answer1);
        answer1.setBackgroundTintList(csl);
        Button answer2 = findViewById(R.id.answer2);
        answer2.setBackgroundTintList(csl);
        Button answer3 = findViewById(R.id.answer3);
        answer3.setBackgroundTintList(csl);
        Button answer4 = findViewById(R.id.answer4);
        answer4.setBackgroundTintList(csl);
    }

    public void sendAnswer(int answer){
            CustomApplication ca = (CustomApplication)getApplication();
            AnswerPacket ap = new AnswerPacket(ca.getTableNum(), answer, question.num );
            ca.getMessageQueue().add(ap); //Send answer message
    }

    public void setCountdownTimer(long millisleft) {
        ProgressBar countdownBar = findViewById(R.id.countdown_bar);
        int ratio = (int) ((double)millisleft/(double)question.ms*BAR_RESOLUTION);
        countdownBar.setProgress(ratio);
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
                countdown=countdown-10;
                //System.out.println(countdown);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setCountdownTimer(countdown);
                    }
                });
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
