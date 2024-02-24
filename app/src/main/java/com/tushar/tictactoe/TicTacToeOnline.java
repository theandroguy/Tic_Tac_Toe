package com.tushar.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TicTacToeOnline extends AppCompatActivity  {

    private LinearLayout player1layout, player2layout;
    private ImageView img1,img2,img3,img4,img5,img6,img7,img8,img9;
    private TextView player1, player2;

    private final List<int[]> combinationsList =new ArrayList<>();

    private final List<String> boxesD= new ArrayList<>();


    private String playerUniqueId ="0";

    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://console.firebase.google.com/u/1/project/tic-tac-toe-fa0aa/database/tic-tac-toe-fa0aa-default-rtdb/data/~2F");
    private  boolean opponent=false;
    private String opponentUniqueID="0";

    private String statusCon ="matching";

    String playerTurn = "";

    private String ConnectionID="";

    ValueEventListener turn , won;

    private  final String[] boxesSelectedBy={"","","","","","","","",""};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe_online);

        // online


        player1layout = findViewById(R.id.player1layout);
        player2layout = findViewById(R.id.player2layout);

        player1 = findViewById(R.id.player1);
        player2 = findViewById(R.id.player2);

        img1 = findViewById(R.id.img00);
        img2 = findViewById(R.id.img01);
        img3 = findViewById(R.id.img02);
        img4 = findViewById(R.id.img10);
        img5 = findViewById(R.id.img11);
        img6 = findViewById(R.id.img12);
        img7 = findViewById(R.id.img20);
        img8 = findViewById(R.id.img21);
        img9 = findViewById(R.id.img22);

        final String getPlayerName = getIntent().getStringExtra("playerName");

        combinationsList.add(new int[]{0, 1, 2});
        combinationsList.add(new int[]{3, 4, 5});
        combinationsList.add(new int[]{6, 7, 8});
        combinationsList.add(new int[]{0, 3, 6});
        combinationsList.add(new int[]{1, 4, 7});
        combinationsList.add(new int[]{2, 5, 8});
        combinationsList.add(new int[]{2, 4, 6});
        combinationsList.add(new int[]{0, 4, 8});

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Waiting for someone to join");
        progressDialog.show();

        playerUniqueId = String.valueOf(System.currentTimeMillis());

        player1.setText(getPlayerName);

        databaseReference.child("connections").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!opponent) {
                    if (snapshot.hasChildren()) {
                        for (DataSnapshot connections : snapshot.getChildren()) {
                            String conID = connections.getKey();

                            int getplayerscount = (int) connections.getChildrenCount();

                            if (statusCon.equals("waiting")) {
                                if (getplayerscount == 2) {
                                    playerTurn = playerUniqueId;
                                    RunPlayerTurn(playerTurn);
                                    boolean playerFound = false;

                                    for (DataSnapshot players : connections.getChildren()) {
                                        String getplayerUniqueID = players.getKey();
                                        if (getplayerUniqueID.equals(playerUniqueId)) {
                                            playerFound = true;
                                        } else {
                                            String getOpponentPlayerName = players.child("player_name").getValue(String.class);
                                            opponentUniqueID = players.getKey();

                                            player2.setText(getOpponentPlayerName);
                                            ConnectionID = conID;

                                            opponent = true;

                                            databaseReference.child("turn").child(ConnectionID).addValueEventListener(turn);
                                            databaseReference.child("won").child(ConnectionID).addValueEventListener(won);

                                            if (progressDialog.isShowing()) {
                                                progressDialog.dismiss();
                                            }

                                            databaseReference.child("connections").removeEventListener(this);
                                        }
                                    }
                                } else {
                                    if (getplayerscount == 1) {
                                        connections.child(playerUniqueId).child("player_name").getRef().setValue(getPlayerName);
                                        for (DataSnapshot players : connections.getChildren()) {
                                            String getOppoName = players.child("player_name").getValue(String.class);
                                            opponentUniqueID = players.getKey();

                                            playerTurn = opponentUniqueID;

                                            RunPlayerTurn(playerTurn);

                                            player2.setText(getOppoName);

                                            ConnectionID = conID;
                                            opponent = true;


                                            databaseReference.child("turn").child(ConnectionID).addValueEventListener(turn);
                                            databaseReference.child("won").child(ConnectionID).addValueEventListener(won);

                                            if (progressDialog.isShowing()) {
                                                progressDialog.dismiss();
                                            }

                                            databaseReference.child("connections").removeEventListener(this);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (!opponent && !statusCon.equals("waiting")) {
                            String ConnectionID = String.valueOf(System.currentTimeMillis());
                            snapshot.child(ConnectionID).child("player_name").getRef().setValue(getPlayerName);
                            statusCon = "waiting";
                        }
                    } else {
                        String ConnectionID = String.valueOf(System.currentTimeMillis());
                        snapshot.child(ConnectionID).child("player_name").getRef().setValue(getPlayerName);
                        statusCon = "waiting";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        turn = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getChildrenCount() == 2) {
                        final int getBoxPos = Integer.parseInt(dataSnapshot.child("box_position").getValue(String.class));
                        final String getplayerID = dataSnapshot.child("player_id").getValue(String.class);

                        if (!boxesD.contains(String.valueOf(getBoxPos))) {
                            boxesD.add(String.valueOf(getBoxPos));

                            if (getBoxPos == 1) {
                                selectBox(img1, getBoxPos, getplayerID);
                            } else if (getBoxPos == 2) {
                                selectBox(img2, getBoxPos, getplayerID);
                            } else if (getBoxPos == 3) {
                                selectBox(img3, getBoxPos, getplayerID);
                            } else if (getBoxPos == 4) {
                                selectBox(img4, getBoxPos, getplayerID);
                            } else if (getBoxPos == 5) {
                                selectBox(img5, getBoxPos, getplayerID);
                            } else if (getBoxPos == 6) {
                                selectBox(img6, getBoxPos, getplayerID);
                            } else if (getBoxPos == 7) {
                                selectBox(img7, getBoxPos, getplayerID);
                            } else if (getBoxPos == 8) {
                                selectBox(img8, getBoxPos, getplayerID);
                            } else if (getBoxPos == 9) {
                                selectBox(img9, getBoxPos, getplayerID);
                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        won = new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("player_id"))
                {
                    String getWinPlayerID = snapshot.child("player_id").getValue(String.class);

                    final WinWindow wInWindow;

                    if (getWinPlayerID.equals(playerUniqueId))
                    {
                        wInWindow=new WinWindow(TicTacToeOnline.this,"You Won the Game");

                    }
                    else
                    {
                        wInWindow=new WinWindow(TicTacToeOnline.this,"Opponent Won the Game");

                    }
                    wInWindow.setCancelable(false);
                    wInWindow.show();

                    databaseReference.child("turn").child(ConnectionID).addValueEventListener(turn);
                    databaseReference.child("won").child(ConnectionID).addValueEventListener(won);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(boxesD.contains("1") && playerTurn.equals(playerUniqueId))
                {
                    ((ImageView)v).setImageResource(R.drawable.cross);
                    databaseReference.child("turn").child(ConnectionID).child(String.valueOf(boxesD.size()+1)).child(
                            "box_position").setValue("1");

                    databaseReference.child("turn").child(ConnectionID).child(String.valueOf(boxesD.size()+1)).child(
                            "player_id").setValue(playerUniqueId);

                    playerTurn =opponentUniqueID;
                }
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(boxesD.contains("2") && playerTurn.equals(playerUniqueId))
                {
                    ((ImageView)v).setImageResource(R.drawable.cross);
                    databaseReference.child("turn").child(ConnectionID).child(String.valueOf(boxesD.size()+1)).child(
                            "box_position").setValue("2");

                    databaseReference.child("turn").child(ConnectionID).child(String.valueOf(boxesD.size()+1)).child(
                            "player_id").setValue(playerUniqueId);

                    playerTurn =opponentUniqueID;
                }
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(boxesD.contains("3") && playerTurn.equals(playerUniqueId))
                {
                    ((ImageView)v).setImageResource(R.drawable.cross);
                    databaseReference.child("turn").child(ConnectionID).child(String.valueOf(boxesD.size()+1)).child(
                            "box_position").setValue("3");

                    databaseReference.child("turn").child(ConnectionID).child(String.valueOf(boxesD.size()+1)).child(
                            "player_id").setValue(playerUniqueId);

                    playerTurn =opponentUniqueID;
                }
            }
        });

        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(boxesD.contains("4") && playerTurn.equals(playerUniqueId))
                {
                    ((ImageView)v).setImageResource(R.drawable.cross);
                    databaseReference.child("turn").child(ConnectionID).child(String.valueOf(boxesD.size()+1)).child(
                            "box_position").setValue("4");

                    databaseReference.child("turn").child(ConnectionID).child(String.valueOf(boxesD.size()+1)).child(
                            "player_id").setValue(playerUniqueId);

                    playerTurn =opponentUniqueID;
                }
            }
        });

        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(boxesD.contains("5") && playerTurn.equals(playerUniqueId))
                {
                    ((ImageView)v).setImageResource(R.drawable.cross);
                    databaseReference.child("turn").child(ConnectionID).child(String.valueOf(boxesD.size()+1)).child(
                            "box_position").setValue("5");

                    databaseReference.child("turn").child(ConnectionID).child(String.valueOf(boxesD.size()+1)).child(
                            "player_id").setValue(playerUniqueId);

                    playerTurn =opponentUniqueID;
                }
            }
        });

        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(boxesD.contains("6") && playerTurn.equals(playerUniqueId))
                {
                    ((ImageView)v).setImageResource(R.drawable.cross);
                    databaseReference.child("turn").child(ConnectionID).child(String.valueOf(boxesD.size()+1)).child(
                            "box_position").setValue("6");

                    databaseReference.child("turn").child(ConnectionID).child(String.valueOf(boxesD.size()+1)).child(
                            "player_id").setValue(playerUniqueId);

                    playerTurn =opponentUniqueID;
                }
            }
        });

        img7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(boxesD.contains("7") && playerTurn.equals(playerUniqueId))
                {
                    ((ImageView)v).setImageResource(R.drawable.cross);
                    databaseReference.child("turn").child(ConnectionID).child(String.valueOf(boxesD.size()+1)).child(
                            "box_position").setValue("7");

                    databaseReference.child("turn").child(ConnectionID).child(String.valueOf(boxesD.size()+1)).child(
                            "player_id").setValue(playerUniqueId);

                    playerTurn =opponentUniqueID;
                }
            }
        });

        img8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(boxesD.contains("8") && playerTurn.equals(playerUniqueId))
                {
                    ((ImageView)v).setImageResource(R.drawable.cross);
                    databaseReference.child("turn").child(ConnectionID).child(String.valueOf(boxesD.size()+1)).child(
                            "box_position").setValue("8");

                    databaseReference.child("turn").child(ConnectionID).child(String.valueOf(boxesD.size()+1)).child(
                            "player_id").setValue(playerUniqueId);

                    playerTurn =opponentUniqueID;
                }
            }
        });

        img9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(boxesD.contains("9") && playerTurn.equals(playerUniqueId))
                {
                    ((ImageView)v).setImageResource(R.drawable.cross);
                    databaseReference.child("turn").child(ConnectionID).child(String.valueOf(boxesD.size()+1)).child(
                            "box_position").setValue("9");

                    databaseReference.child("turn").child(ConnectionID).child(String.valueOf(boxesD.size()+1)).child(
                            "player_id").setValue(playerUniqueId);

                    playerTurn =opponentUniqueID;
                }
            }
        });
    }

    private void RunPlayerTurn(String playerUniqueId2) {
        if(playerUniqueId2.equals(playerUniqueId))
        {
            player1layout.setBackgroundResource(R.drawable.roundstroke);
            player2layout.setBackgroundResource(R.drawable.round);
        }
        else
        {
            player1layout.setBackgroundResource(R.drawable.round);
            player2layout.setBackgroundResource(R.drawable.roundstroke);
        }
    }

    private  void selectBox(ImageView imageView , int  selectBoxPos , String selectedPlayer)
    {
        boxesSelectedBy[selectBoxPos -1] = selectedPlayer;
        if(selectedPlayer.equals(playerUniqueId))
        {
            imageView.setImageResource(R.drawable.cross);
            playerTurn=opponentUniqueID;
        }
        else {
            imageView.setImageResource(R.drawable.circle);
            playerTurn=playerUniqueId;
        }
        RunPlayerTurn(playerTurn);

        // check another player has won or not
        if(checkPlayerWin(selectedPlayer))
        {
            databaseReference.child("won").child(ConnectionID).child("player_id").setValue(selectedPlayer);

        }
        if(boxesD.size() == 9)
        {
            final WinWindow window =new WinWindow(TicTacToeOnline.this,"The Game has been Drawn");
            window.setCancelable(false);
            window.show();
        }
    }

    private boolean checkPlayerWin(String playerID)
    {
        boolean isPlayerWon = false;
        for (int i=0;i<combinationsList.size();i++)
        {
            final int[] combinations = combinationsList.get(i);

            if(boxesSelectedBy[combinations[0]].equals(playerID) && boxesSelectedBy[combinations[1]].equals(playerID) &&
                    boxesSelectedBy[combinations[1]].equals(playerID ))
            {
                isPlayerWon = true;

            }
        }

        return isPlayerWon;
    }


}

