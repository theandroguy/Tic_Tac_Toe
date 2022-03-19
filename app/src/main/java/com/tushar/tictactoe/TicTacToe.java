package com.tushar.tictactoe;

import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TicTacToe extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];


    private boolean play1 = true;
    private int count;
    private int player1p;
    private int player2p;

    private TextView tv1;
    private TextView tv2;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        MediaPlayer mp = MediaPlayer.create(this, R.raw.ding);
        tv1 = findViewById(R.id.player1);
        tv2 = findViewById(R.id.player2);
        tv1.setText(getIntent().getStringExtra("text"));
        tv2.setText(getIntent().getStringExtra("text2"));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String btnid = "btn" + i + j;
                int resid = getResources().getIdentifier(btnid, "id", getPackageName());
                buttons[i][j] = findViewById(resid);
                buttons[i][j].setOnClickListener(this);
            }
        }
        Button reset = findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetgame();
                mp.start();
                //Toast.makeText(TicTacToe.this, "Game Restarted", Toast.LENGTH_SHORT).show();
                AlertDialog alertDialog=new AlertDialog.Builder(TicTacToe.this).create();
                alertDialog.setTitle("Tic Tac Toe");
                alertDialog.setMessage("Game Restarted");
                alertDialog.setCancelable(false);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OKAY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mp.start();
                        dialog.dismiss();
                    }
                });
                alertDialog.show();

            }
        });

    }



    @Override
    public void onClick(View v) {
        MediaPlayer m2 = MediaPlayer.create(this, R.raw.ding);


        if (!((Button) v).getText().toString().equals("")) {
            return;
        }
        if (play1) {
            m2.start();

            ((Button) v).setTextColor(Color.RED);
            ((Button) v).setText("X");
        } else {
            m2.start();
            ((Button) v).setTextColor(Color.CYAN);
            ((Button) v).setText("O");
        }
        count++;
        if (checkingwin()) {
            if (play1) {
                player1wins();
            } else {
                player2wins();
            }
        } else if (count == 9) {
            draw();
        } else {
            play1 = !play1;
        }

    }

    private boolean checkingwin() {
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals("")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals("")) {
                return true;
            }
        }
        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals("")) {
            return true;
        }

        if (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals("")) {
            return true;
        }

        return false;
    }

    private void player1wins() {
        player1p++;
       // Toast.makeText(this, "Player 1 wins!!", Toast.LENGTH_SHORT).show();
        updatepoints();
        reset1();
    }

    private void player2wins() {
        player2p++;
       // Toast.makeText(this, "Player 2 wins!!", Toast.LENGTH_SHORT).show();
        updatepoints();
        reset1();
    }

    private void draw() {
     Toast.makeText(this, "Match Has been Drawn", Toast.LENGTH_SHORT).show();
        reset1();
    }

    private void updatepoints() {
        tv1.setText(getIntent().getStringExtra("text")+" : "+ player1p);
        tv2.setText(getIntent().getStringExtra("text2")+" : "+ player2p);
    }

    private void reset1() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        MediaPlayer m3 = MediaPlayer.create(this, R.raw.ding);
        count = 0;
        play1 = true;
        if (player1p>player2p)
        {

            AlertDialog alertDialog=new AlertDialog.Builder(TicTacToe.this).create();
            alertDialog.setTitle("Tic Tac Toe");
            alertDialog.setMessage(getIntent().getStringExtra("text")+" is the winner ");
            alertDialog.setCancelable(false);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "RESET GAME", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m3.start();
                    resetgame();
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }
        else if (player2p>player1p)
        {
            {
                AlertDialog alertDialog=new AlertDialog.Builder(TicTacToe.this).create();
                alertDialog.setTitle("Tic Tac Toe");
                alertDialog.setMessage(getIntent().getStringExtra("text2")+" is the winner ");
                alertDialog.setCancelable(false);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "RESET GAME", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m3.start();
                        resetgame();
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        }

        }

    public void resetgame() {
        player1p = 0;
        player2p = 0;
        updatepoints();
        reset1();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("count", count);
        outState.putInt("player1p", player1p);
        outState.putInt("player2p", player2p);
        outState.putBoolean("play1", play1);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        count = savedInstanceState.getInt("count");
        player1p = savedInstanceState.getInt("player1p");
        player2p = savedInstanceState.getInt("player2p");
        play1 = savedInstanceState.getBoolean("play1");
    }
}