package com.example.rockpaperscissors;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.crypto.spec.IvParameterSpec;

public class MainActivity extends AppCompatActivity {

    int currentRound;
    int lastRound;
    Player opponent;
    Player user;
    String winner;
    String[] possibleActions;
    String[] insults;

    LinearLayout mainMenuLayout;
    LinearLayout helpScreenLayout;
    LinearLayout settingsScreenLayout;
    RelativeLayout gameScreenLayout;

    EditText nameInput;
    RadioGroup modeRadioGroup;
    RadioButton standardMode;
    RadioButton minefieldMode;
    Switch dirtyOpponentSwitch;
    Switch manualRoundsSwitch;
    EditText manualRoundsInput;

    TextView roundDisplayText;
    TextView opponentScoreText;
    TextView userScoreText;
    TextView opponentPlayText;
    TextView userPlayText;
    TextView gameEventText;
    ImageView opponentRockImage;
    ImageView opponentPaperImage;
    ImageView opponentScissorsImage;
    ImageView userRockImage;
    ImageView userPaperImage;
    ImageView userScissorsImage;
    ImageView opponentBoomImage;
    ImageView userBoomImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentRound = 1;
        lastRound = 5;
        opponent = new Player("PC","",0);
        user = new Player("","",0);
        winner = "";
        possibleActions = new String[]{"rock", "paper", "scissors"};
        insults = new String[]{
                "Rock, paper, loser!",
                "Are you even trying?",
                "If your gaming habits were a dress code, they'd be casual.",
                "You can't even beat your own mobile device ... How sad.",
                "N00b",
                "Had both hands tied behind my back, and you still lost.",
                "Are you even trying?"
        };

        mainMenuLayout = (LinearLayout)findViewById(R.id.main_menu_layout);
        helpScreenLayout = (LinearLayout)findViewById(R.id.help_screen_layout);
        settingsScreenLayout = (LinearLayout)findViewById(R.id.settings_screen_layout);
        gameScreenLayout = (RelativeLayout)findViewById(R.id.game_screen_layout);

        nameInput = (EditText)findViewById(R.id.name_edit);
        modeRadioGroup = (RadioGroup)findViewById(R.id.mode_radiogroup);
        standardMode = (RadioButton)findViewById(R.id.standard_mode);
        minefieldMode = (RadioButton)findViewById(R.id.minefield_mode);
        dirtyOpponentSwitch = (Switch)findViewById(R.id.dirty_opponent_switch);
        manualRoundsSwitch = (Switch)findViewById(R.id.manual_rounds_switch);
        manualRoundsInput = (EditText)findViewById(R.id.manual_rounds_edit);

        //Standard mode is selected by default
        standardMode.setChecked(true);

        roundDisplayText = (TextView)findViewById(R.id.round_display_text);
        opponentScoreText = (TextView)findViewById(R.id.opponent_score_text);
        userScoreText = (TextView)findViewById(R.id.user_score_text);
        opponentPlayText = (TextView)findViewById(R.id.opponent_play_text);
        userPlayText = (TextView)findViewById(R.id.user_play_text);
        gameEventText = (TextView)findViewById(R.id.game_event_text);
        userRockImage = (ImageView)findViewById(R.id.user_rock_image);
        userPaperImage = (ImageView)findViewById(R.id.user_paper_image);
        userScissorsImage = (ImageView)findViewById(R.id.user_scissors_image);
        opponentRockImage = (ImageView)findViewById(R.id.opponent_rock_image);
        opponentPaperImage = (ImageView)findViewById(R.id.opponent_paper_image);
        opponentScissorsImage = (ImageView)findViewById(R.id.opponent_scissors_image);
        opponentBoomImage = (ImageView)findViewById(R.id.opponent_boom_image);
        userBoomImage = (ImageView)findViewById(R.id.user_boom_image);
    }

    /*                    MAIN MENU FUNCTIONS(start)                    */
    public void playGame (View view) {
        mainMenuLayout.setVisibility(View.INVISIBLE);
        gameScreenLayout.setVisibility(View.VISIBLE);

        //Whenever a new game begins, everything is reset
        gameEventText.setTextColor(Color.parseColor("#ffffff"));

        if(nameInput.getText().toString().isEmpty())
            user.setName("You");
        else
            user.setName(nameInput.getText().toString());

        userPlayText.setText(null);
        opponentPlayText.setText(null);
        gameEventText.setText(null);
        winner = null;

        user.setScore(0);
        opponent.setScore(0);

        userRockImage.setClickable(true);
        userPaperImage.setClickable(true);
        userScissorsImage.setClickable(true);

        if(manualRoundsSwitch.isChecked()) {
            currentRound = 1;
            String temp = manualRoundsInput.getText().toString();               //Handling manual
            lastRound = Integer.parseInt(temp);                                 //  rounds
        } else {
            currentRound = 1;
            lastRound = 5;
        }

        roundDisplayText.setText("Round " + currentRound + " of " + lastRound); //Displaying number
                                                                                //  of rounds left

        opponentScoreText.setText("Score: " + opponent.getScore());             //Displaying player
        userScoreText.setText("Score: " + user.getScore());                     //   scores
    }

    public void goToSettings(View view) {
        mainMenuLayout.setVisibility(View.INVISIBLE);
        settingsScreenLayout.setVisibility(View.VISIBLE);
    }

    public void goToHelp(View view) {
        mainMenuLayout.setVisibility(View.INVISIBLE);
        helpScreenLayout.setVisibility(View.VISIBLE);
    }
    /*                    MAIN MENU FUNCTIONS (end)                    */



    /*                    HELP FUNCTIONS (start)                    */
    public void backToMainFromHelp(View view) {
        mainMenuLayout.setVisibility(View.VISIBLE);
        helpScreenLayout.setVisibility(View.INVISIBLE);
    }
    /*                    HELP FUNCTIONS (end)                    */



    /*                    SETTINGS FUNCTIONS (start)                    */
    public void backToMainFromSettings(View view) {

        //User is not allowed to go back to main menu if manual rounds are enabled with no input
        if(manualRoundsInput.getVisibility() == View.VISIBLE && manualRoundsInput.getText().toString().isEmpty()){
            Toast.makeText(this, "Enter number of rounds", Toast.LENGTH_SHORT).show();
        } else {
            mainMenuLayout.setVisibility(View.VISIBLE);
            settingsScreenLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void manualRoundsToggled(View view) {
        if(manualRoundsInput.getVisibility() == View.INVISIBLE)
            manualRoundsInput.setVisibility(View.VISIBLE);
        else
            manualRoundsInput.setVisibility(View.INVISIBLE);
    }
    /*                    SETTINGS FUNCTIONS (end)                    */



    /*                    GAME FUNCTIONS (start)                    */
    public void backToMainFromGame(View view) {
        mainMenuLayout.setVisibility(View.VISIBLE);
        gameScreenLayout.setVisibility(View.INVISIBLE);
    }

    public void animateOpponentAction(String action) { ;
        switch(action){
            case "rock":
                opponentRockImage.animate().scaleY(1.3f).scaleX(1.3f).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        opponentRockImage.animate().scaleY(1f).scaleX(1f);
                    }
                });
                break;
            case"paper":
                opponentPaperImage.animate().scaleY(1.3f).scaleX(1.3f).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        opponentPaperImage.animate().scaleY(1f).scaleX(1f);
                    }
                });
                break;
            case"scissors":
                opponentScissorsImage.animate().scaleY(1.3f).scaleX(1.3f).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        opponentScissorsImage.animate().scaleY(1f).scaleX(1f);
                    }
                });
                break;
        }
    }

    public void animateUserAction(String action) {
        switch(action){
            case "rock":
                userRockImage.animate().scaleY(1.3f).scaleX(1.3f).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        userRockImage.animate().scaleY(1f).scaleX(1f);
                    }
                });
                break;
            case"paper":
                userPaperImage.animate().scaleY(1.3f).scaleX(1.3f).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        userPaperImage.animate().scaleY(1f).scaleX(1f);
                    }
                });
                break;
            case"scissors":
                userScissorsImage.animate().scaleY(1.3f).scaleX(1.3f).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        userScissorsImage.animate().scaleY(1f).scaleX(1f);
                    }
                });
                break;
        }
    }

    public void Rock (View view) {
        user.setAction("rock");
        userPlayText.setText(user.getName() + " played " + user.getAction() + "!");
        animateUserAction(user.getAction());

        opponent.setAction(randAction());
        opponentPlayText.setText(opponent.getName() + " played " + opponent.getAction() + "!");
        animateOpponentAction(opponent.getAction());

        switch (opponent.getAction()){
            case "rock":
                break;
            case "paper":
                opponent.wins();
                if(dirtyOpponentSwitch.isChecked())
                    spoutInsult();
                break;
            case "scissors":
                user.wins();
                break;
        }

        if(minefieldMode.isChecked())
            triggerMinefield();

        updateScores();
        updateRounds();
    }

    public void Paper (View view) {
        user.setAction("paper");
        userPlayText.setText(user.getName() + " played " + user.getAction() + "!");
        animateUserAction(user.getAction());

        opponent.setAction(randAction());
        opponentPlayText.setText(opponent.getName() + " played " + opponent.getAction() + "!");
        animateOpponentAction(opponent.getAction());

        switch (opponent.getAction()){
            case "rock":
                user.wins();
                break;
            case "paper":
                break;
            case "scissors":
                opponent.wins();
                if(dirtyOpponentSwitch.isChecked())
                    spoutInsult();
                break;
        }

        if(minefieldMode.isChecked())
            triggerMinefield();

        updateScores();
        updateRounds();
    }

    public void Scissors (View view) {
        user.setAction("scissors");
        userPlayText.setText(user.getName() + " played " + user.getAction() + "!");
        animateUserAction(user.getAction());

        opponent.setAction(randAction());
        opponentPlayText.setText(opponent.getName() + " played " + opponent.getAction() + "!");
        animateOpponentAction(opponent.getAction());

        switch (opponent.getAction()){
            case "rock":
                opponent.wins();
                if(dirtyOpponentSwitch.isChecked())
                    spoutInsult();
                break;
            case "paper":
                user.wins();
                break;
            case "scissors":
                break;
        }

        if(minefieldMode.isChecked())
            triggerMinefield();

        updateScores();
        updateRounds();
    }

    public String randAction() {
        Random rand = new Random();
        int randomIndex = rand.nextInt(3);
        Log.i("randomAction", "Index is: " + randomIndex);

        String randomAction;
        randomAction = possibleActions[randomIndex];
        Log.i("randomAction", "Action is: " + randomAction);

        return randomAction;
    }

    public void updateScores() {
        opponentScoreText.setText("Score: " + opponent.getScore());
        userScoreText.setText("Score: " + user.getScore());
    }

    public void updateRounds(){
        if(currentRound < lastRound) {
            currentRound++;
            roundDisplayText.setText("Round " + currentRound + " of " + lastRound);
        } else if(currentRound == lastRound){
            gameOver();
        }
    }
    
    public void gameOver() {
        Log.i("gameOver()", "gameOver() invoked");
        if(user.getScore() > opponent.getScore()){
           gameEventText.setTextColor(Color.parseColor("#00ff00"));
            winner = user.getName();
            gameEventText.setText("Game over! The winner is " + winner.toUpperCase() + ".");
        } else if(opponent.getScore() > user.getScore()){
            winner = opponent.getName();
            gameEventText.setTextColor(Color.parseColor("#ff4d4d"));
            gameEventText.setText("Game over! The winner is " + winner.toUpperCase() + ".");
        } else
            gameEventText.setText("Gamer over! It's a tie.");

        userRockImage.setClickable(false);
        userPaperImage.setClickable(false);
        userScissorsImage.setClickable(false);
    }

    public void triggerMinefield() {
        Random rand = new Random();
        int chance = rand.nextInt(101);

        if(chance <= 35) {          //35% chance of user triggering minefield
            user.loses();
            gameEventText.setText("MINEFIELD TRIGGERED by " + user.getName() + "!");
            animateUserMinefield();
        } else if (chance >= 65) { //35% chance of opponent triggering minefield
            opponent.loses();
            gameEventText.setText("MINEFIELD TRIGGERED by " + opponent.getName() + "!");
            animateOpponentMinefield();
        } else {                    //30% chance of both triggering minefield
            user.loses();
            opponent.loses();
            gameEventText.setText("MINEFIELD TRIGGERED by both players!");
            animateUserMinefield();
            animateOpponentMinefield();
        }
    }

    public void animateOpponentMinefield() {
        opponentBoomImage.bringToFront();
        opponentBoomImage.animate().scaleX(1f).scaleY(1f).setDuration(400)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        opponentBoomImage.animate().scaleX(0f).scaleY(0f).setDuration(400);
                    }
                });

    }

    public void animateUserMinefield() {
        userBoomImage.bringToFront();
        userBoomImage.animate().scaleX(1f).scaleY(1f).setDuration(400)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        userBoomImage.animate().scaleX(0f).scaleY(0f).setDuration(400);
                    }
                });
    }

    public void spoutInsult() {
        Random rand = new Random();
        int randomInsultIndex = rand.nextInt(7);

        Toast.makeText(this, insults[randomInsultIndex], Toast.LENGTH_LONG).show();
    }
    /*                    GAME FUNCTIONS (end)                    */
}




