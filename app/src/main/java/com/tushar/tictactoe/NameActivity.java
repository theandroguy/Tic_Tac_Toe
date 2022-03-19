package com.tushar.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NameActivity extends AppCompatActivity {

    EditText p1,p2;
    TextView X,O;
    String player1,player2;
    //Button play,interchange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.ding);
        p1=findViewById(R.id.player1);
        p2=findViewById(R.id.player2);
        X=findViewById(R.id.XSign);
        O=findViewById(R.id.OSign);
        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                player1=p1.getText().toString();
                player2=p2.getText().toString();
//                if (p1.getText().toString().equals(null))
//                {
//                    findViewById(R.id.play).setActivated(false);
//                }
//                if (p2.getText().toString().equals(null))
//                {
//                    findViewById(R.id.play).setActivated(false);
//                }
                Intent ticTac=new Intent(NameActivity.this,TicTacToe.class);
                ticTac.putExtra("text", player1);
                ticTac.putExtra("text2",player2);
                startActivity(ticTac);
               mp.start();
            }
        });
//        findViewById(R.id.interchange).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(X.getText().toString().equals("X"))
//                {
//                    X.setText("O");
//                    else
//                    {
//                        X.setText("X");
//                    }
//                }
//                else if(O.getText().toString().equals("O"))
//                {
//                    O.setText("X");
//                    if(O.getText().toString().equals("X"))
//                    {
//                        O.setText("X");
//                    }
//                }
//
//            }
//        });




    }
}