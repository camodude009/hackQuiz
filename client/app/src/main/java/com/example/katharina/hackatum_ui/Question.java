package com.example.katharina.hackatum_ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.katharina.hackatum_ui.model.QuestionPacket;
import com.example.katharina.hackatum_ui.serverinterface.Serializer;

/**
 * Created by Katharina on 18/11/2017.
 */

public class Question extends AppCompatActivity {
    private int questionNum;
    private int numQuestionsPerRound;
    private int answer;

    QuestionPacket question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        String jsonText = getIntent().getExtras().getString("JSON");
        question = (QuestionPacket)Serializer.deserializePacket(jsonText, QuestionPacket.class);

        System.out.println(jsonText);
    }

    private void init(int questionNum, int numQuestionsPerRound){//TODO Frage eingeben
        Button fancy = findViewById(R.id.fancyButton);
        fancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Transition to activity_bar
            }
        });

        TextView qNum = findViewById(R.id.questionNum);
        qNum.setText("Question "+questionNum+"/"+numQuestionsPerRound);

        setQuestion();

        ProgressBar pb = findViewById(R.id.progressBar);
        pb.setMax(10);
        pb.setProgress(1);
    }

    public void nextQuestion(){ //TODO Frage eingeben
        ProgressBar pb = findViewById(R.id.progressBar);
        pb.setProgress(pb.getProgress()+1);
        setQuestion();
    }

    private void setQuestion(){ //TODO requires Question
        //TODO setQuestion
    }

    public int getAnswer(){
        return answer;
    }



}
