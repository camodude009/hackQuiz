package com.example.katharina.hackatum_ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.katharina.hackatum_ui.model.RegisterPacket;
import com.example.katharina.hackatum_ui.serverinterface.QuizClient;

/**
 * Created by Katharina on 18/11/2017.
 */

public class Enter extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        //Setup button
        Button submitBtn = (Button)findViewById(R.id.submit);
        submitBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView nameEdt = (EditText)findViewById(R.id.edit_name);
                String nameString = nameEdt.getText().toString();
                TextView tableNrEdt = (EditText)findViewById(R.id.edit_table);
                if (nameString==null || nameString.equals("")) {
                    Toast.makeText(Enter.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    //Number and name should be sanitized here
                    //=> Invoking the send registration message on our lient
                    int tableNr = Integer.parseInt( tableNrEdt.getText().toString() );

                    RegisterPacket rp = new RegisterPacket( tableNr, nameString );
                    //Start client interface
                    Intent i = new Intent(Enter.this, QuizClient.class);
                    //i.putExtra("KEY1", "Value to be used by the service");
                    startService(i);

                    ((CustomApplication)getApplication()).getMessageQueue().add(rp);
                    ((CustomApplication)getApplication()).setTableNum(tableNr);
                } catch(Exception e) { //TODO specify exception
                    Toast.makeText(Enter.this, "Please enter a number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
