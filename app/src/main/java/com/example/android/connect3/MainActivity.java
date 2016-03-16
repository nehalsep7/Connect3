package com.example.android.connect3;

import android.media.Image;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int currentPlayer = 0;
    int[] board = {2,2,2,2,2,2,2,2,2};
    int[][] winningPositions = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
    int winValue=100;
    boolean gameIsActive = true;
    MediaPlayer mplayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout layout = (LinearLayout)findViewById(R.id.WinLayout);
        layout.setVisibility(View.GONE);
    }
    public void playAgain(View view){
        mplayer.stop();
        gameIsActive = true;
        LinearLayout layout = (LinearLayout)findViewById(R.id.WinLayout);
        layout.setVisibility(View.GONE);
        currentPlayer = 0;
        for(int k =0;k<9;k++)
            board[k] = 2;
        winValue = 100;
        GridLayout grids = (GridLayout)findViewById(R.id.gridLayout);
        for(int j = 0;j<grids.getChildCount();j++){
            ((ImageView)grids.getChildAt(j)).setImageResource(0);
        }


    }
    public void onClick(View view){
        dropIn(view);
    }
    public void dropIn(View view){
        ImageView counter = (ImageView)view;
        int currentTag = Integer.parseInt(counter.getTag().toString());
        if(board[currentTag] == 2 && gameIsActive){
            counter.setTranslationY(-1000f);
            if (currentPlayer == 0){
                counter.setImageResource(R.drawable.yellow);
                currentPlayer = 1;
            }

            else{
                counter.setImageResource(R.drawable.red);
                currentPlayer = 0;
            }
            counter.animate().translationYBy(1000f).rotation(360).setDuration(100);
            board[currentTag] = currentPlayer;

            for(int[] winningPosition : winningPositions){
                if(board[winningPosition[0]] == board[winningPosition[1]]
                        && board[winningPosition[1]] == board[winningPosition[2]]
                        && board[winningPosition[0]] == 0) winValue =0;
                else if(board[winningPosition[0]] == board[winningPosition[1]]
                        && board[winningPosition[1]] == board[winningPosition[2]]
                        && board[winningPosition[0]] == 1) winValue =1;



            }
            if(winValue == 0 || winValue == 1){
                gameIsActive = false;
                LinearLayout layout = (LinearLayout)findViewById(R.id.WinLayout);
                final Handler handler = new Handler();
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        handler.postDelayed(this,10000);
                    }
                };
                handler.post(run);
                if(winValue == 1){
                    TextView winMessage = (TextView)findViewById(R.id.wonText);
                    winMessage.setText("Yellow Wins");
                }

                else{
                    TextView winMessage = (TextView)findViewById(R.id.wonText);
                    winMessage.setText("Red Wins");
                }

                layout.setVisibility(View.VISIBLE);
                mplayer = MediaPlayer.create(this,R.raw.applaud);
                mplayer.start();


            }
            else{
                boolean boardFull = true;
                for(int i =0 ;i<board.length;i++){
                    if(board[i] == 2)
                        boardFull = false;
                }
                if(boardFull){
                    gameIsActive = false;
                    LinearLayout layout = (LinearLayout)findViewById(R.id.WinLayout);
                    TextView winMessage = (TextView)findViewById(R.id.wonText);
                    winMessage.setText("Itz a Draw");
                    layout.setVisibility(View.VISIBLE);
                }
            }
        }


    }

}
