package com.example.katharina.hackatum_ui;

import android.content.Intent;
import android.os.Bundle;
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

        Button fancy = findViewById(R.id.fancyButton);
        fancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dialogIntent = new Intent(Question.this, Bar.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);
            }
        });

        //Relay answers
        Button answerA1 = (Button)findViewById(R.id.answer1);
        answerA1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAnswer(0);
            }
        });
        Button answerA2 = (Button)findViewById(R.id.answer2);
        answerA2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAnswer(1);
            }
        });
        Button answerA3 = (Button)findViewById(R.id.answer3);
        answerA3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAnswer(2);
            }
        });
        Button answerA4 = (Button)findViewById(R.id.answer4);
        answerA4.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAnswer(3);
            }
        });

        ProgressBar pb = findViewById(R.id.progressBar);
        pb.setMax(question.total);
        pb.setProgress(question.num);
    }

//    public void nextQuestion(){ //Frage eingeben -> Don via client
//        ProgressBar pb = findViewById(R.id.progressBar);
//        pb.setProgress(pb.getProgress()+1);
//        setQuestion();
//    }

    private void setQuestion(){
        if(question != null){
            TextView qNum = findViewById(R.id.questionNum);
            qNum.setText("Question "+question.num+"/"+question.total);

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

    public void sendAnswer(int answer){
        if (answered==false) {
            CustomApplication ca = (CustomApplication)getApplication();
            AnswerPacket ap = new AnswerPacket(answer, question.num, ca.getTableNum());
            ca.getMessageQeuue().add(ap); //Send answer message
            answered = true;
        }
    }



}
