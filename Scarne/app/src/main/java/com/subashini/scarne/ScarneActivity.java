package com.subashini.scarne;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.logging.Handler;

public class ScarneActivity extends AppCompatActivity {

    // user's overall score
    private int userOverallScore;
    // user's turn score
    private int userTurnScore;
    // computer's overall score
    private int compOverallScore;
    // computer's turn score
    private int compTurnScore;

    private Random randomVal = new Random();
    // current dice Value
    private int diceValue;

    private boolean isUserTurn = true;

    private  Handler compHandler;
    private Runnable compRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scarne);

        findViewById(R.id.reset_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });
        findViewById(R.id.hold_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserTurn) {
                    hold();
                }
            }
        });
        findViewById(R.id.roll_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserTurn) {
                    roll();
                }
            }
        });
        /**
        // Create a new Handler. This schedules runnables.
        compHandler = new Handler();
        compRunnable = new Runnable() {
            @Override
            public void run() {
                // Do the computer's roll.
                roll();

                // See if it is still our turn, and if the score is low enough to keep rolling.
                if (compTurnScore < 20 && !isUserTurn) {
                    computerTurn();
                } else if (isUserTurn) {
                    // Go ahead and stop -- it is the computer's turn, but it is about 20 points.
                    hold();
                }
            }
        };*/


    }



    private void roll() {
        diceValue = randomVal.nextInt(6) + 1;

        //updateScoreDisplay();
        if (diceValue == 1) {
            userTurnScore = 0;
            compTurnScore = 0;
            if(isUserTurn) {
                computerTurn();
            } else {
                isUserTurn = true;
            }
            //updateScoreDisplay();
            //computerTurn();
        } else {
            if (isUserTurn) {
                userTurnScore += diceValue;
                updateScoreDisplay();
            } else {
                compTurnScore += diceValue;
                updateScoreDisplay();
            }
        }
        changeDiceImage(diceValue);
        checkForWin();

    }

    /** Save the turn score of the computer or user and update the overall score.
     *  Switch players. */
    private void hold() {

        if (isUserTurn) {
            userOverallScore += userTurnScore;
            userTurnScore = 0;
            updateScoreDisplay();
            isUserTurn = false;
            computerTurn();
        } else {
            compOverallScore += compTurnScore;
            compTurnScore = 0;
            updateScoreDisplay();
            isUserTurn = true;
        }

        // Switch players turns
        //updateScoreDisplay();
    }


    private void reset() {
        userTurnScore = 0;
        userOverallScore = 0;
        compOverallScore = 0;
        compTurnScore = 0;
        diceValue = 0;
        updateScoreDisplay();
        isUserTurn = true;

    }

    private void computerTurn() {
        isUserTurn = false;
        findViewById(R.id.roll_button).setEnabled(false);
        findViewById(R.id.hold_button).setEnabled(false);
        while (compTurnScore < 20 && !isUserTurn) {
            roll();
        }
        //compHandler.postDelayed(compRunnable, 1000);
        enableButtons();
        hold();
    }
    /*private void doComputerTurn() {
        mHandler.postDelayed(mRunnable, COMPUTER_TURN_DELAY);
    }*/

    private void enableButtons() {
        findViewById(R.id.roll_button).setEnabled(true);
        findViewById(R.id.hold_button).setEnabled(true);
    }


    // Change the face of dice Image based on the value rolled
    private void changeDiceImage(int value) {

        ImageView diceImage = (ImageView) findViewById(R.id.dice);

        if (value == 1) {
            diceImage.setImageDrawable(getResources().getDrawable(R.drawable.dice1));
            diceImage.setContentDescription("Dice face 1");
        } else if (value == 2) {
            diceImage.setImageDrawable(getResources().getDrawable(R.drawable.dice2));
            diceImage.setContentDescription("Dice face 2");
        } else if (value == 3) {
            diceImage.setImageDrawable(getResources().getDrawable(R.drawable.dice3));
            diceImage.setContentDescription("Dice face 3");
        } else if (value == 4) {
            diceImage.setImageDrawable(getResources().getDrawable(R.drawable.dice4));
            diceImage.setContentDescription("Dice face 4");
        } else if (value == 5) {
            diceImage.setImageDrawable(getResources().getDrawable(R.drawable.dice5));
            diceImage.setContentDescription("Dice face 5");
        } else {
            diceImage.setImageDrawable(getResources().getDrawable(R.drawable.dice6));
            diceImage.setContentDescription("Dice face 6");
        }
    }

    private void updateScoreDisplay() {
        TextView scoreDisplay = (TextView) findViewById(R.id.score_display);
        String displayScore = "Your score: " + userOverallScore + "  Computer score: " + compOverallScore;
        if(isUserTurn) {
            displayScore += " Your Turn score: " + userTurnScore;
        } else {
            displayScore += " Computer Turn score: " + compTurnScore;
        }
        scoreDisplay.setText(displayScore);
    }

    private void checkForWin() {
        /*switch (whosTurns) {
            case PLAYER: if (userOverallScore + userTurnScore > 25) playerWins(); break;
            case COMPUTER: if (compOverallScore + compTurnScore > 100) computerWins(); break;
        }*/
         if (isUserTurn) {
            if (userOverallScore + userTurnScore > 25) playerWins();

        } else {
            if (compOverallScore + compTurnScore > 100) computerWins();
         }

    }
    public static final String USER_SCORE = "com.subashini.scarne.userOverallScore";
    private void playerWins() {
        Intent intent = new Intent(this, WinActivity.class);
        intent.putExtra(USER_SCORE, String.valueOf(userOverallScore));
        startActivity(intent);
        reset();
    }

    public static final String COMPUTER_SCORE = "com.subashini.scarne.compOverallScore";

    private void computerWins() {
        Intent intent = new Intent(this, LoseActivity.class);
        intent.putExtra(COMPUTER_SCORE, String.valueOf(compOverallScore));
        startActivity(intent);
    }

}
