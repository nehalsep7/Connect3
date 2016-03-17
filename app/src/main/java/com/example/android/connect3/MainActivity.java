package com.example.android.connect3;

import android.graphics.Color;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int currentPlayer = 0;
    int[] board = {2,2,2,2,2,2,2,2,2};
    int[][] winningPositions = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
    int[] randomUsed = {0,0,0,0,0,0,0,0,0};
    int winValue=100;
    boolean gameIsActive = true;
    MediaPlayer mplayer;
    GridLayout gridLayout;
    LinearLayout startScreen;
    Button onePlayer;
    Button twoPlayer;
    int oneOrTwoTag;
    boolean computerStarts = true;
    boolean userTurn = true;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = (LinearLayout)findViewById(R.id.WinLayout);
        gridLayout = (GridLayout)findViewById(R.id.gridLayout);
        startScreen = (LinearLayout)findViewById(R.id.startScreen);
        onePlayer = (Button)findViewById(R.id.onePlayer);
        twoPlayer = (Button)findViewById(R.id.twoPlayer);
        gridLayout.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
    }

    public void startGame(View view){
        gridLayout.setVisibility(View.VISIBLE);
        startScreen.setVisibility(View.GONE);
        onePlayer.setVisibility(View.GONE);
        twoPlayer.setVisibility(View.GONE);
        oneOrTwoTag = Integer.parseInt(((Button) view).getTag().toString());
        Log.i("Single or Double",Integer.toString(oneOrTwoTag));
        if(oneOrTwoTag == 1){
            Log.i("Players : ","Single");
            if(computerStarts){
                computerPlays(view);
                Toast.makeText(getApplicationContext(),"Your Turn",Toast.LENGTH_LONG).show();
                computerStarts = false;
            }
            else{
                Toast.makeText(getApplicationContext(),"You Start",Toast.LENGTH_LONG).show();
                computerStarts = true;
            }
        }
        else{
            Log.i("Players :","Two");
            Toast.makeText(getApplicationContext(),"Start Playing!",Toast.LENGTH_LONG).show();
        }
    }
    public void onClick(final View view){
        ImageView counter = (ImageView)view;
        int currentTag = Integer.parseInt(counter.getTag().toString());
        randomUsed[currentTag] = 1;
        dropIn(view, counter, currentTag);
        if(oneOrTwoTag == 1){
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    for(int j = 0;j<gridLayout.getChildCount();j++)
                        gridLayout.getChildAt(j).setClickable(false);
                    // Do something after 5s = 5000ms
                    computerPlays(view);
                    for(int j = 0;j<gridLayout.getChildCount();j++)
                        gridLayout.getChildAt(j).setClickable(true);
                }
            }, 1000);

        }

    }

    public void computerPlays(View view){
        int compLoc = (int)(Math.random()*9 );
        while (randomUsed[compLoc] == 1){
            compLoc = (int)(Math.random()*9 );
        }
        Log.i("Random Number", Integer.toString(compLoc));
        ImageView counter = ((ImageView)gridLayout.getChildAt(compLoc));
        randomUsed[compLoc] = 1;
        dropIn(view,counter,compLoc);
    }
    public void dropIn(View view,ImageView counter, int currentTag){

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
                    winMessage.setText("It's a Draw");
                    layout.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    public void playAgain(View view){
        mplayer.stop();
        gameIsActive = true;
        layout.setVisibility(View.GONE);
        currentPlayer = 0;
        for(int k =0;k<9;k++)
            board[k] = 2;
        winValue = 100;

        for(int j = 0;j<gridLayout.getChildCount();j++){
            ((ImageView)gridLayout.getChildAt(j)).setImageResource(0);
            randomUsed[j] = 0;
        }
        view.setTag(oneOrTwoTag);
        startGame(view);
    }

    public void getMainMenu(View view){
        layout = (LinearLayout)findViewById(R.id.WinLayout);
        gridLayout = (GridLayout)findViewById(R.id.gridLayout);
        startScreen = (LinearLayout)findViewById(R.id.startScreen);
        startScreen.setVisibility(View.VISIBLE);
        onePlayer = (Button)findViewById(R.id.onePlayer);
        twoPlayer = (Button)findViewById(R.id.twoPlayer);
        onePlayer.setVisibility(View.VISIBLE);
        twoPlayer.setVisibility(View.VISIBLE);
        gridLayout.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
        mplayer.stop();
        gameIsActive = true;
        currentPlayer = 0;
        for(int k =0;k<9;k++)
            board[k] = 2;
        winValue = 100;

        for(int j = 0;j<gridLayout.getChildCount();j++){
            ((ImageView)gridLayout.getChildAt(j)).setImageResource(0);
            randomUsed[j] = 0;
        }
    }

}
