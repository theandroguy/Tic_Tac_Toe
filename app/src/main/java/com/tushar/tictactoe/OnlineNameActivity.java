package com.tushar.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OnlineNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_name);

        final EditText playerName = findViewById(R.id.playername);
        final Button playOnline = findViewById(R.id.playOnline);

        playOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String getPlayerName = playerName.getText().toString();
                if(getPlayerName.isEmpty())
                {
                    Toast.makeText(OnlineNameActivity.this, "Please Enter a name to continue", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent=new Intent(OnlineNameActivity.this,TicTacToeOnline.class);
                    intent.putExtra("playerName",getPlayerName);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

}