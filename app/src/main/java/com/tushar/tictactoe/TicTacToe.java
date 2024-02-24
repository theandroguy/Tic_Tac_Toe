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
    private MediaPlayer mediaPlayer;
    private int count;
    private int player1Score;
    private int player2Score;

    private TextView player1TextView;
    private TextView player2TextView;

    private static final String PLAYER_WINS_MESSAGE = "%s wins!";
    private static final String DRAW_MESSAGE = "Match has been drawn";

    private boolean isPlayer1Turn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        mediaPlayer = MediaPlayer.create(this, R.raw.ding);
        player1TextView = findViewById(R.id.player1);
        player2TextView = findViewById(R.id.player2);
        player1TextView.setText(getIntent().getStringExtra("text"));
        player2TextView.setText(getIntent().getStringExtra("text2"));

        initializeButtons();

        Button resetButton = findViewById(R.id.reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
                showAlert("Game Restarted");
            }
        });
    }

    private void initializeButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonId = "btn" + i + j;
                int resourceId = getResources().getIdentifier(buttonId, "id", getPackageName());
                buttons[i][j] = findViewById(resourceId);
                buttons[i][j].setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        mediaPlayer.start();

        Button clickedButton = (Button) v;
        if (!clickedButton.getText().toString().equals("")) {
            return; // The button has already been clicked
        }

        if (isPlayer1Turn) {
            setButtonProperties(clickedButton, Color.RED, "X");
        } else {
            setButtonProperties(clickedButton, Color.CYAN, "O");
        }

        count++;

        if (checkForWin()) {
            if (isPlayer1Turn) {
                playerWins(getIntent().getStringExtra("text"));
            } else {
                playerWins(getIntent().getStringExtra("text2"));
            }
        } else if (count == 9) {
            draw();
        } else {
            isPlayer1Turn = !isPlayer1Turn;
        }
    }

    private void setButtonProperties(Button button, int textColor, String symbol) {
        button.setTextColor(textColor);
        button.setText(symbol);
    }

    private boolean checkForWin() {
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

    private void playerWins(String player) {
        if (player.equals(getIntent().getStringExtra("text"))) {
            player1Score++;
        } else {
            player2Score++;
        }
        updateScores();
        showAlert(String.format(PLAYER_WINS_MESSAGE, player));
        resetBoard();
    }

    private void draw() {
        Toast.makeText(this, DRAW_MESSAGE, Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    private void updateScores() {
        player1TextView.setText(getIntent().getStringExtra("text") + " : " + player1Score);
        player2TextView.setText(getIntent().getStringExtra("text2") + " : " + player2Score);
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        count = 0;
        isPlayer1Turn = true;
    }

    private void resetGame() {
        player1Score = 0;
        player2Score = 0;
        updateScores();
        resetBoard();
    }

    private void showAlert(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(TicTacToe.this).create();
        alertDialog.setTitle("Tic Tac Toe");
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OKAY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mediaPlayer.start();
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save necessary state variables if needed
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore saved state variables if needed
    }
}
