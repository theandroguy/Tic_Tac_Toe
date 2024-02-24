package com.tushar.tictactoe;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.ding);
        findViewById(R.id.po).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                Toast.makeText(MainActivity.this, "Feature is Coming soon", Toast.LENGTH_SHORT).show();

//                Intent intent= new Intent(MainActivity.this , OnlineNameActivity.class);
//                startActivity(intent);

            }
        });
        findViewById(R.id.pwf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                Intent pwf=new Intent(MainActivity.this,NameActivity.class);
                startActivity(pwf);
            }
        });
       findViewById(R.id.pwc).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mp.start();
               Toast.makeText(MainActivity.this, "Play with Computer Feature is Coming soon", Toast.LENGTH_SHORT).show();
           }
       });



    }
}