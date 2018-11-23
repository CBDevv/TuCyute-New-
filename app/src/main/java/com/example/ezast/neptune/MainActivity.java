package com.example.ezast.neptune;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity
{

    //Animation Variables declared
    public AnimationDrawable idleFaceMovement;
    public AnimationDrawable sleepFaceMovement;
    public AnimationDrawable energyBarMovement;
    public AnimationDrawable hungerBarMovement;
    public AnimationDrawable eatingMovement;
    public AnimationDrawable treatMovement;
    public AnimationDrawable talkFaceMovement;
    public AnimationDrawable studyFaceMovement;

    private boolean studyRunning;
    private CountDownTimer countDownTimer;
    private static long timeLeftInMilliseconds = 600000; //10 minutes

    //Age and treats / reward variables
    TextView ageValue;
    TextView treatValue;
    TextView knowledgeValue;
    TextView gradeLevel;
    int test = 500;
    int age = 1;
    int treat = 1;
    Switch switch1;

    @SuppressLint({"WrongViewCast", "RestrictedApi"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Method for the switch to toggle music on and off
        switch1 = findViewById(R.id.togglebutton);

        //Link for the background music while starting.
        final MediaPlayer themeSong;
        themeSong = MediaPlayer.create(this, R.raw.tucyutetheme);
        themeSong.setLooping(true);
        themeSong.setVolume(50,50);
        themeSong.start();

        //Face Views assigned
        final ImageView sleepFace = findViewById(R.id.sleepface);
        final ImageView eatingFace = findViewById(R.id.eatingface);
        final ImageView studyFace = findViewById(R.id.studyface);
        final ImageView face = findViewById(R.id.mainface);

        //Button assignments
        final Button feedButton = findViewById(R.id.eatButton);
        final Button restButton = findViewById(R.id.restButton);
        final Button wakeButton = findViewById(R.id.wakeButton);
        final Button studyButton = findViewById(R.id.bookButton);
        final Button studyButton2 = findViewById(R.id.bookButton2);
        final Button playButton = findViewById(R.id.playButton);
        final Button mapButton = findViewById(R.id.mapButton);
        final Button feedButtonDisabled = findViewById(R.id.eatButtonDisabled);
        final Button restButtonDisabled = findViewById(R.id.restButtonDisabled);
        final Button studyButtonDisabled = findViewById(R.id.bookButtonDisabled);
        final Button playButtonDisabled = findViewById(R.id.playButtonDisabled);
        final Button mapButtonDisabled = findViewById(R.id.mapButtonDisabled);

        //Assigning sounds and music
        final MediaPlayer talkSound = MediaPlayer.create(this, R.raw.fullintro);
        final MediaPlayer eatingSound = MediaPlayer.create(this, R.raw.tucyuteate);
        final MediaPlayer sleepSound = MediaPlayer.create(this, R.raw.sleeping);
        final MediaPlayer readSound = MediaPlayer.create(this, R.raw.readingsound);
        final MediaPlayer gradeSound = MediaPlayer.create( this, R.raw.gradesound);
        final MediaPlayer treatSound = MediaPlayer.create( this, R.raw.treatsound);


        //Links to text views

        ageValue = findViewById(R.id.agetext);
        treatValue = findViewById(R.id.treatText);
        knowledgeValue = findViewById(R.id.knowledgetext);
        gradeLevel = findViewById(R.id.gradeLevel);

        // Treat animation flying across screen.
        final ImageView treatsFlying = findViewById(R.id.treats);
        treatsFlying.setBackgroundResource(R.drawable.treatanimation);
        treatMovement = (AnimationDrawable) treatsFlying.getBackground();

        // Rest Bar Decrease over time animation
        final ImageView energyLevel = findViewById(R.id.energyBar);
        energyLevel.setBackgroundResource(R.drawable.energybaranimation);
        energyBarMovement = (AnimationDrawable) energyLevel.getBackground();

        energyBarMovement.start();

        // Hunger Bar Decrease over time animation
        final ImageView hungerLevel = findViewById(R.id.hungerBar);
        hungerLevel.setBackgroundResource(R.drawable.hungerbaranimation);
        hungerBarMovement = (AnimationDrawable) hungerLevel.getBackground();

        //Start hunger bar drop animation
        hungerBarMovement.start();

        //start with the following buttons invisible
        wakeButton.setVisibility(View.INVISIBLE);
        mapButton.setVisibility(View.INVISIBLE);
        studyButton.setVisibility(View.INVISIBLE);
        playButton.setVisibility(View.INVISIBLE);
        studyButton2.setVisibility(View.INVISIBLE);

        //Start with the following buttons visible
        playButtonDisabled.setVisibility(View.VISIBLE);
        mapButtonDisabled.setVisibility(View.VISIBLE);
        studyButton.setVisibility(View.INVISIBLE);

        //Set the image on startup to the idle face by default
        face.setBackgroundResource(R.drawable.idlefaceanimation);
        idleFaceMovement = (AnimationDrawable) face.getBackground();

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            //override allows for the music to play in background. The switch is invisible and cannot be accessed.
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked) {
                    themeSong.start();
                }
            }
        });

        //Feed Button
        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Stops the idle face animation
                idleFaceMovement.stop();
                face.setBackgroundResource(R.drawable.blank);

                //Stops the sleep face animation
                sleepFace.setBackgroundResource(R.drawable.blank);
                //Creates eating animation

                eatingFace.setBackgroundResource(R.drawable.eatinganimation);
                eatingMovement = (AnimationDrawable) eatingFace.getBackground();

                //starts eating animation
                eatingMovement.stop();
                eatingMovement.start();

                //Reset Hunger
                hungerBarMovement.stop();
                hungerBarMovement.start();

                //Play the sound of eating
                eatingSound.start();
            }
        });

        //Rest Button
        restButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"RestrictedApi", "SetTextI18n"})
            public void onClick(View sleeping) {

                //Rest button is no longer visible until the wake button is pressed.
                restButton.setVisibility(View.INVISIBLE);
                wakeButton.setVisibility(View.VISIBLE);

                //Feeding button is disabled so you can't wake up by eating.
                feedButton.setVisibility(View.INVISIBLE);
                feedButtonDisabled.setVisibility(View.VISIBLE);

                //Study button is disabled so you can't wake up by studying.
                studyButton.setVisibility(View.INVISIBLE);
                studyButtonDisabled.setVisibility(View.VISIBLE);

                //Rest button is disabled so you can't wake up by playing.
                playButton.setVisibility(View.INVISIBLE);

                //Counts the click of the rest button and aging the pet.
                age++;
                ageValue.setText(Integer.toString(age));

                //Stops the idle face animation when the button is pressed.
                idleFaceMovement.stop();
                face.setBackgroundResource(R.drawable.blank);

                //Stops and clears the eating face animation.
                eatingFace.setBackgroundResource(R.drawable.blank);

                //Creates the sleep animation.
                ImageView sleepFace = findViewById(R.id.sleepface);
                sleepFace.setBackgroundResource(R.drawable.sleepanimation);
                sleepFaceMovement = (AnimationDrawable) sleepFace.getBackground();

                //Start the sleeping sound upon press.
                sleepSound.start();

                //Start the sleeping animation
                sleepFaceMovement.start();
                energyBarMovement.stop();

                //Test if age is a certain value, then give more treats
                //If age is a certain value, the background will change as the pet ages.

                if ((age == 5)) {

                    //Play the eating sound
                    //treatSound.start();
                    Toast.makeText(getApplicationContext(), "Toddler", Toast.LENGTH_SHORT).show();

                    //Adding treats upon sleep.
                    treatValue.setText(Integer.toString(treat));
                    treatMovement.stop();
                    treatMovement.start();

                    //Set the background
                    ImageView backgroundMain1 = findViewById(R.id.background2);
                    backgroundMain1.setVisibility(View.GONE);

                    //Clears the sleep face animation when the button is pressed.
                    sleepFace.setBackgroundResource(R.drawable.blank);

                    //Starts the talking face animation again
                    ImageView talk = findViewById(R.id.mainface);
                    talk.setBackgroundResource(R.drawable.talkinganimation);
                    talkFaceMovement = (AnimationDrawable) talk.getBackground();
                    talkFaceMovement.start();

                    //Start the talking sounds
                    sleepSound.pause();
                    talkSound.start();

                    //Starts the idle face animation again
                    ImageView face = findViewById(R.id.mainface);
                    idleFaceMovement = (AnimationDrawable) face.getBackground();

                } if (age >= 0) {
                    //Adding treats upon touch.
                    treatValue.setText(Integer.toString(treat));
                    treat = treat + 10;
                }

                if ((age == 10)) {

                    //Change the background
                    ImageView backgroundMain2 = findViewById(R.id.background4);
                    backgroundMain2.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "Teenager", Toast.LENGTH_SHORT).show();
                }

                if ((age == 20)) {

                    //Change the background
                    ImageView backgroundMain3 = findViewById(R.id.background5);
                    backgroundMain3.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "Adulthood!", Toast.LENGTH_SHORT).show();
                }

                if ((age == 30)) {

                    //Change the background
                    ImageView backgroundMain4 = findViewById(R.id.background6);
                    backgroundMain4.setVisibility(View.GONE);
                }

                if ((age == 35)) {

                    //Change the background
                    ImageView backgroundMain5 = findViewById(R.id.background7);
                    backgroundMain5.setVisibility(View.GONE);
                }

                if ((age == 40)) {

                    //Change the background
                    ImageView backgroundMain7 = findViewById(R.id.background8);
                    backgroundMain7.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "Senior Citizen!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        //Wake button
        wakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Rest button is now invisible until the wakeup button is pressed.
                restButton.setVisibility(View.VISIBLE);
                wakeButton.setVisibility(View.INVISIBLE);

                //Feed button is now visible so you can eat while awake.
                feedButton.setVisibility(View.VISIBLE);
                feedButtonDisabled.setVisibility(View.INVISIBLE);

                //Study button is now visible so study while awake.
                studyButton.setVisibility(View.VISIBLE);
                studyButtonDisabled.setVisibility(View.INVISIBLE);

                //Rest button is visible so you play while awake.
                //playButton.setVisibility(View.VISIBLE);

                //Clears the sleep face animation
                sleepFace.setBackgroundResource(R.drawable.ic_blank);
                sleepFaceMovement.stop();

                //Starts the idle face animation again
                ImageView face = findViewById(R.id.mainface);
                face.setBackgroundResource(R.drawable.idlefaceanimation);
                idleFaceMovement = (AnimationDrawable) face.getBackground();
                idleFaceMovement.start();
                energyBarMovement.start();
            }
        });

        //Study Button 1st grade.
        studyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Clears any other animations.
                face.setBackgroundResource(R.drawable.ic_blank);
                eatingFace.setBackgroundResource(R.drawable.ic_blank);
                sleepFace.setBackgroundResource(R.drawable.ic_blank);

                //Creates study animation
                studyFace.setBackgroundResource(R.drawable.studyanimation);
                studyFaceMovement = (AnimationDrawable) studyFace.getBackground();
                studyFaceMovement.start();
                startStop();
            }

            private void startStop() {
                if (studyRunning) {
                    stopTimer();
                } else {
                    startTimer();
                }
            }

            @SuppressLint("RestrictedApi")
            private void startTimer() {
                countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        timeLeftInMilliseconds = millisUntilFinished;

                        updateTimer();
                    }
                    @Override
                    public void onFinish() {

                    }
                }.start();
                //remove visibility of other buttons while studying.
                feedButton.setVisibility(View.INVISIBLE);
                restButton.setVisibility(View.INVISIBLE);

                //show disabled buttons while studying
                restButtonDisabled.setVisibility(View.VISIBLE);
                feedButtonDisabled.setVisibility(View.VISIBLE);

                readSound.start();
                studyRunning = true;
            }

            @SuppressLint("RestrictedApi")
            void stopTimer() {
                countDownTimer.cancel();

                //Stop the animation
                studyFaceMovement.stop();
                studyFace.setBackgroundResource(R.drawable.ic_blank);

                //Start the idle face movement again.
                face.setBackgroundResource(R.drawable.idlefaceanimation);
                idleFaceMovement = (AnimationDrawable) face.getBackground();
                idleFaceMovement.start();

                //return the visibility of other buttons.
                feedButton.setVisibility(View.VISIBLE);
                restButton.setVisibility(View.VISIBLE);

                //remove the disabled button since studying has been canceled.
                feedButtonDisabled.setVisibility(View.INVISIBLE);
                restButtonDisabled.setVisibility(View.INVISIBLE);
                studyRunning = false;
            }

            void updateTimer() {
                int minutes = (int) (timeLeftInMilliseconds / 1000) / 60;
                int seconds = (int) (timeLeftInMilliseconds / 1000) % 60;

                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                knowledgeValue.setText(timeLeftFormatted);

                if (minutes == 9 && seconds == 59){

                    //After certain time of studying, new grade level reached.
                    gradeLevel.setText("K");
                }
                if (minutes == 9 && seconds == 55){

                    gradeLevel.setText("1st");
                    gradeSound.start();

                    countDownTimer.cancel();

                    //Stop the animation
                    studyFaceMovement.stop();
                    studyFace.setBackgroundResource(R.drawable.ic_blank);

                    //Start the idle face movement again.
                    face.setBackgroundResource(R.drawable.idlefaceanimation);
                    idleFaceMovement = (AnimationDrawable) face.getBackground();
                    idleFaceMovement.start();

                    //return the visibility of other buttons.
                    feedButton.setVisibility(View.VISIBLE);
                    restButton.setVisibility(View.VISIBLE);

                    //remove the disabled button since studying has been canceled.
                    feedButtonDisabled.setVisibility(View.INVISIBLE);
                    restButtonDisabled.setVisibility(View.INVISIBLE);

                    /*Remove this button so the study
                    button 2 transitions and appears underneath.
                    -- Work around --
                     */

                    studyButton.setVisibility(View.INVISIBLE);

                    //pause music
                    themeSong.pause();

                    //Study Tutorial Dialog Box.
                    ImageView gradeImage = new ImageView(MainActivity.this);
                    gradeImage.setImageResource(R.drawable.certificate);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("1st Grade!")
                            .setMessage("By studying, you reach new grade levels. Each grade level provides bonus treats and your exposure level is increased; allowing you to explore more of the world.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which){
                                    //code if they select "yes"
                                    treatSound.start();
                                    treatMovement.start();
                                    treat = treat + 100;
                                    themeSong.start();

                                    studyButton2.setVisibility(View.VISIBLE);
                                }
                            }).setView(gradeImage);

                    AlertDialog alert = builder.create();
                    alert.show();
                    }
                }
        });

        //Study Button 2nd grade.
        studyButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Clears any other animations.
                face.setBackgroundResource(R.drawable.ic_blank);
                eatingFace.setBackgroundResource(R.drawable.ic_blank);
                sleepFace.setBackgroundResource(R.drawable.ic_blank);

                //Creates study animation
                studyFace.setBackgroundResource(R.drawable.studyanimation);
                studyFaceMovement = (AnimationDrawable) studyFace.getBackground();
                studyFaceMovement.start();
                startStop();
            }

            private void startStop() {
                if (studyRunning) {
                    stopTimer();
                } else {
                    startTimer();
                }
            }

            @SuppressLint("RestrictedApi")
            private void startTimer() {
                countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        timeLeftInMilliseconds = millisUntilFinished;

                        updateTimer();
                    }
                    @Override
                    public void onFinish() {

                    }
                }.start();
                //remove visibility of other buttons while studying.
                feedButton.setVisibility(View.INVISIBLE);
                restButton.setVisibility(View.INVISIBLE);

                //show disabled buttons while studying
                restButtonDisabled.setVisibility(View.VISIBLE);
                feedButtonDisabled.setVisibility(View.VISIBLE);

                readSound.start();
                studyRunning = true;
            }

            @SuppressLint("RestrictedApi")
            void stopTimer() {
                countDownTimer.cancel();

                //Stop the animation
                studyFaceMovement.stop();
                studyFace.setBackgroundResource(R.drawable.ic_blank);

                //Start the idle face movement again.
                face.setBackgroundResource(R.drawable.idlefaceanimation);
                idleFaceMovement = (AnimationDrawable) face.getBackground();
                idleFaceMovement.start();

                //return the visibility of other buttons.
                feedButton.setVisibility(View.VISIBLE);
                restButton.setVisibility(View.VISIBLE);

                //remove the disabled button since studying has been canceled.
                feedButtonDisabled.setVisibility(View.INVISIBLE);
                restButtonDisabled.setVisibility(View.INVISIBLE);
                studyRunning = false;
            }

            void updateTimer() {
                int minutes = (int) (timeLeftInMilliseconds / 1000) / 60;
                int seconds = (int) (timeLeftInMilliseconds / 1000) % 60;

                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                knowledgeValue.setText(timeLeftFormatted);

                if (minutes == 9 && seconds == 48){

                    gradeLevel.setText("2nd");
                    gradeSound.start();

                    countDownTimer.cancel();

                    //Stop the animation
                    studyFaceMovement.stop();
                    studyFace.setBackgroundResource(R.drawable.ic_blank);

                    //Start the idle face movement again.
                    face.setBackgroundResource(R.drawable.idlefaceanimation);
                    idleFaceMovement = (AnimationDrawable) face.getBackground();
                    idleFaceMovement.start();

                    //return the visibility of other buttons.
                    feedButton.setVisibility(View.VISIBLE);
                    restButton.setVisibility(View.VISIBLE);

                    //remove the disabled button since studying has been canceled.
                    feedButtonDisabled.setVisibility(View.INVISIBLE);
                    restButtonDisabled.setVisibility(View.INVISIBLE);

                    //pause music
                    themeSong.pause();
                }
            }
        });

        //House Button
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent houseIntent = new Intent(getApplicationContext(), HouseActivity.class);
                //startActivity(houseIntent);
                /*Snackbar.make(view, "Nothing happens yet!", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();*/
            }
        });
        //Map button
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View map) {
                //Intent mapIntent = new Intent(getApplicationContext(), MapActivity.class);
               // startActivity(mapIntent);
            }
        });
    }

    //Override to make the idle face animations start as soon as program launches and loops.
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        idleFaceMovement.start();
    }

    public void touch(View view) {
        //Method for Treat sound.
        //MediaPlayer treatSound = MediaPlayer.create( this, R.raw.treatsound);

        //Play the eating sound
        //treatSound.start();

        //Adding treats upon touch.
        //treatValue.setText(Integer.toString(treat));
        //treat++;
        //treatMovement.stop();
        //treatMovement.start();
    }
}