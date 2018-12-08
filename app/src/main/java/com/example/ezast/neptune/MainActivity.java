package com.example.ezast.neptune;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;

/**
 * Created by: Chris Butler
 * TuCyute - A Mobile Casual Game
 */

public class MainActivity extends AppCompatActivity {

    //Animation Variables declared
    public AnimationDrawable idleFaceMovement;
    public AnimationDrawable sleepFaceMovement;
    public AnimationDrawable energyBarMovement;
    public AnimationDrawable hungerBarMovement;
    public AnimationDrawable eatingMovement;
    public AnimationDrawable treatMovement;
    public AnimationDrawable talkFaceMovement;
    public AnimationDrawable studyFaceMovement;

    private static long START_TIME_IN_MILLIS = 86400000; //24 hours
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    public static final String SHARED_PREFS2 = "sharedPrefs2";
    public static final String TEXT2 = "text2";

    private String gradeText;
    private String statusText;

    private SoundPlayer sound;
    private Handler greetingHandler = new Handler();
    private Handler howareyouHandler = new Handler();


    //Age and treats / reward variables
    TextView ageValue, treatValue, knowledgeValue, gradeLevel, eatClickValue, studyClickValue, ageStatusText, restClickValue;
    int age, treat, eatClicks, studyClicks, restClicks;
    Switch switch1;

    ImageView studyLoadK;
    ImageView studyLoadFirst;

    @SuppressLint({"WrongViewCast", "RestrictedApi"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Greeting
            final Runnable greetingRunnable = new Runnable() {
                @Override
                public void run() {

                    //Starts the talking face animation again
                    ImageView talk = findViewById(R.id.mainface);
                    talk.setBackgroundResource(R.drawable.talkinganimation);
                    talkFaceMovement = (AnimationDrawable) talk.getBackground();
                    talkFaceMovement.start();

                    //Start the talking sounds
                    sound.playTalkSound();
                }
            };
            greetingHandler.postDelayed(greetingRunnable, 2000);
            final Runnable howareyouRunnable = new Runnable() {
                @Override
                public void run() {

                    //Starts the talking face animation again
                    ImageView talk = findViewById(R.id.mainface);
                    talk.setBackgroundResource(R.drawable.talkinganimation);
                    talkFaceMovement = (AnimationDrawable) talk.getBackground();
                    talkFaceMovement.stop();
                    talkFaceMovement.start();

                    //Start the talking sounds
                    sound.playHowAreYouSound();
                }
            };
            howareyouHandler.postDelayed(howareyouRunnable, 4000);
            //End of Greeting

        ConstraintLayout constraintLayout = findViewById(R.id.layout);
        AnimationDrawable backgroundDrawable1 = (AnimationDrawable) constraintLayout.getBackground();
        backgroundDrawable1.setEnterFadeDuration(2000);
        backgroundDrawable1.setExitFadeDuration(2000);
        backgroundDrawable1.start();

        sound = new SoundPlayer(this);

        //Method for the switch to toggle music on and off
        switch1 = findViewById(R.id.togglebutton);

        //Link for the background music while starting.
        final MediaPlayer themeSong;
        themeSong = MediaPlayer.create(this, R.raw.theme);
        themeSong.setLooping(false);
        themeSong.setVolume(1, 1);
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

        //Links to text views
        ageValue = findViewById(R.id.agetext);
        treatValue = findViewById(R.id.treatText);
        knowledgeValue = findViewById(R.id.knowledgetext);
        gradeLevel = findViewById(R.id.gradeLevel);
        eatClickValue = findViewById(R.id.eatClickText);
        studyClickValue = findViewById(R.id.studyClickText);
        ageStatusText = findViewById(R.id.statusText);
        restClickValue = findViewById(R.id.restClickText);

        // Animations to start.
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
        mapButtonDisabled.setVisibility(View.INVISIBLE);
        playButtonDisabled.setVisibility(View.INVISIBLE);
        studyButton2.setVisibility(View.INVISIBLE);

        //Start with the following buttons visible
        studyButton.setVisibility(View.VISIBLE);
        playButton.setVisibility(View.VISIBLE);
        mapButton.setVisibility(View.VISIBLE);

        //Set the image on startup to the idle face by default
        face.setBackgroundResource(R.drawable.idlefaceanimation);
        idleFaceMovement = (AnimationDrawable) face.getBackground();

        //Load Age
        SharedPreferences myAge = this.getSharedPreferences("MyAge", MODE_PRIVATE);
        age = myAge.getInt("age", 0);
        ageValue.setText("" + age);

        //Load Age Status
        SharedPreferences sharedPreferences2 = getSharedPreferences(SHARED_PREFS2, MODE_PRIVATE);
        statusText = sharedPreferences2.getString(TEXT2, "");
        ageStatusText.setText(statusText);

        //Load Treats
        SharedPreferences myTreat = this.getSharedPreferences("MyTreat", MODE_PRIVATE);
        treat = myTreat.getInt("treat", 0);
        treatValue.setText("" + treat);

        //Load Study Clicks
        SharedPreferences myStudyClick = this.getSharedPreferences("MyStudyClick", MODE_PRIVATE);
        studyClicks = myStudyClick.getInt("studyClick", 0);
        studyClickValue.setText("" + studyClicks);

        //Load Eat Clicks
        SharedPreferences myEatClick = this.getSharedPreferences("MyEatClick", MODE_PRIVATE);
        eatClicks = myEatClick.getInt("eatClick", 0);
        eatClickValue.setText("" + eatClicks);

        //Load Rest Clicks
        SharedPreferences myRestClick = this.getSharedPreferences("MyRestClick", MODE_PRIVATE);
        restClicks = myRestClick.getInt("restClick", 0);
        restClickValue.setText("" + eatClicks);

        //Load Grade
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        gradeText = sharedPreferences.getString(TEXT, "");
        gradeLevel.setText(gradeText);

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

                //Counts study clicks and saves.
                eatClicks += 1;
                //Save eat button clicks amount.
                SharedPreferences myEatClick = getSharedPreferences("MyEatClick", MODE_PRIVATE);
                SharedPreferences.Editor eatClickEditor = myEatClick.edit();
                eatClickEditor.putInt("eatClick", eatClicks);
                eatClickEditor.apply();
                eatClickValue.setText("" + eatClicks);

                if (eatClicks == 1) {

                    sound.playNoticeSound();
                    //Study Tutorial Dialog Box.
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Eating!")
                            .setIcon(R.drawable.eatbutton)
                            .setMessage("Maintain the overall health of TuCyute by eating regularly.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //code if they select "yes"
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                }

                //Stops the idle face animation
                face.setBackgroundResource(R.drawable.blank);
                studyFace.setBackgroundResource(R.drawable.blank);
                sleepFace.setBackgroundResource(R.drawable.blank);

                ImageView eatFace = findViewById(R.id.eatingface);
                eatFace.setBackgroundResource(R.drawable.eatinganimation);
                eatingMovement = (AnimationDrawable) eatFace.getBackground();
                //starts eating animation
                eatingMovement.stop();
                eatingMovement.start();

                //Reset Hunger
                hungerBarMovement.stop();
                hungerBarMovement.start();

                //Play the sound of eating
                sound.playEatingSound();

            }
        });

        //Rest Button
        restButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"RestrictedApi", "SetTextI18n"})
            public void onClick(View sleeping) {

                //Counts study clicks and saves.
                restClicks += 1;
                //Save eat button clicks amount.
                SharedPreferences myRestClick = getSharedPreferences("MyRestClick", MODE_PRIVATE);
                SharedPreferences.Editor restClickEditor = myRestClick.edit();
                restClickEditor.putInt("restClick", restClicks);
                restClickEditor.apply();
                restClickValue.setText("" + restClicks);

                if (restClicks == 1) {

                    sound.playNoticeSound();
                    //Study Tutorial Dialog Box.
                    ImageView restImage = new ImageView(MainActivity.this);
                    restImage.setImageResource(R.drawable.restbutton);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Sleeping!")
                            .setIcon(R.drawable.restbutton)
                            .setMessage("TuCyute has to rest sometimes. Sleeping restores energy and provides you with extra treats! Remember, sleeping ages your TuCyute. Don't want TuCyute to pass away pre-maturely!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //code if they select "yes"
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                }

                //Buttons no longer visible until the wake button is pressed.
                restButton.setVisibility(View.INVISIBLE);
                feedButton.setVisibility(View.INVISIBLE);
                studyButton.setVisibility(View.INVISIBLE);
                playButton.setVisibility(View.INVISIBLE);
                mapButton.setVisibility(View.INVISIBLE);

                wakeButton.setVisibility(View.VISIBLE);
                mapButtonDisabled.setVisibility(View.VISIBLE);
                feedButtonDisabled.setVisibility(View.VISIBLE);
                studyButtonDisabled.setVisibility(View.VISIBLE);
                playButtonDisabled.setVisibility(View.VISIBLE);

                //Stops the idle face animation when the button is pressed.
                idleFaceMovement.stop();
                face.setBackgroundResource(R.drawable.blank);

                //Stops and clears the eating face animation.
                eatingFace.setBackgroundResource(R.drawable.blank);

                studyFace.setBackgroundResource(R.drawable.blank);

                //Creates the sleep animation.
                ImageView sleepFace = findViewById(R.id.sleepface);
                sleepFace.setBackgroundResource(R.drawable.sleepanimation);
                sleepFaceMovement = (AnimationDrawable) sleepFace.getBackground();

                //Start the sleeping sound upon press.
                sound.playSleepSound();

                //Start the sleeping animation
                sleepFaceMovement.start();
                energyBarMovement.stop();

                //Counts aging and saves.
                age += 1;
                //Save age
                SharedPreferences myAge = getSharedPreferences("MyAge", MODE_PRIVATE);
                SharedPreferences.Editor ageEditor = myAge.edit();
                ageEditor.putInt("age", age);
                ageEditor.apply();
                ageValue.setText("" + age);

                //Counts treats and saves.
                treat += 10;
                //Save age
                SharedPreferences myTreat = getSharedPreferences("MyTreat", MODE_PRIVATE);
                SharedPreferences.Editor treatEditor = myTreat.edit();
                treatEditor.putInt("treat", treat);
                treatEditor.apply();
                treatValue.setText("" + treat);

                //Saves grade level
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor gradeEditor = sharedPreferences.edit();
                gradeEditor.putString(TEXT, gradeLevel.getText().toString());
                gradeEditor.apply();

                //Test if age is a certain value, then give more treats
                //If age is a certain value, the background will change as the pet ages.

                if ((age >= 0)) {
                    ageStatusText.setText("Infant");
                }
                if ((age >= 3)) {
                    ageStatusText.setText("Toddler");
                }
                if ((age >= 13)) {
                    ageStatusText.setText("Teenager");
                }
                if ((age >= 20)) {
                    ageStatusText.setText("Adult");
                }
                if ((age >= 30)) {
                }
                if ((age >= 35)) {
                }
                if ((age >= 60)) {
                    ageStatusText.setText("Senior Citizen");
                    }
            }
        });

        //Wake button
        wakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Buttons now invisible until the wakeup button is pressed.
                restButton.setVisibility(View.VISIBLE);
                studyButton.setVisibility(View.VISIBLE);
                feedButton.setVisibility(View.VISIBLE);
                playButton.setVisibility(View.VISIBLE);
                mapButton.setVisibility(View.VISIBLE);


                wakeButton.setVisibility(View.INVISIBLE);
                feedButtonDisabled.setVisibility(View.INVISIBLE);
                studyButtonDisabled.setVisibility(View.INVISIBLE);

                //Clears the sleep face animation
                sleepFace.setBackgroundResource(R.drawable.ic_blank);
                sleepFaceMovement.stop();

                //Starts the idle face animation again
                ImageView face = findViewById(R.id.mainface);
                face.setBackgroundResource(R.drawable.idlefaceanimation);
                idleFaceMovement = (AnimationDrawable) face.getBackground();
                idleFaceMovement.start();
                energyBarMovement.start();

                if ((age == 1)) {

                    //Adding treats upon sleep.
                    treatValue.setText(Integer.toString(treat));
                    treatMovement.stop();
                    treatMovement.start();

                    //Clears the sleep face animation when the button is pressed.
                    sleepFace.setBackgroundResource(R.drawable.blank);
                }

                //Saves age status level
                SharedPreferences sharedPreferences2 = getSharedPreferences(SHARED_PREFS2, MODE_PRIVATE);
                SharedPreferences.Editor agestatusEditor = sharedPreferences2.edit();
                agestatusEditor.putString(TEXT2, ageStatusText.getText().toString());
                agestatusEditor.apply();
            }
        });

        //Study Button.
        studyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sound.playReadSound();
                sound.playTouchSound();
                //Clears any other animations.
                face.setBackgroundResource(R.drawable.ic_blank);
                eatingFace.setBackgroundResource(R.drawable.ic_blank);
                sleepFace.setBackgroundResource(R.drawable.ic_blank);

                //Creates study animation
                studyFace.setBackgroundResource(R.drawable.studyanimation);
                studyFaceMovement = (AnimationDrawable) studyFace.getBackground();
                studyFaceMovement.start();

                //Disable Buttons
                mapButton.setVisibility(View.INVISIBLE);
                playButton.setVisibility(View.INVISIBLE);
                restButton.setVisibility(View.INVISIBLE);
                feedButton.setVisibility(View.INVISIBLE);

                //Shows Second study button
                playButtonDisabled.setVisibility(View.VISIBLE);
                mapButtonDisabled.setVisibility(View.VISIBLE);
                feedButtonDisabled.setVisibility(View.VISIBLE);

                //Counts study clicks and saves.
                studyClicks += 1;
                //Save study button clicks amount.
                SharedPreferences myStudyClick = getSharedPreferences("MyStudyClick", MODE_PRIVATE);
                SharedPreferences.Editor studyClickEditor = myStudyClick.edit();
                studyClickEditor.putInt("studyClick", studyClicks);
                studyClickEditor.apply();
                studyClickValue.setText("" + studyClicks);

                //code here for timer
                if (mTimerRunning) {
                    pauseTimer();
                    //Clears any other animations.
                    studyFace.setBackgroundResource(R.drawable.idlefaceanimation);
                    studyFaceMovement.stop();

                    //Enable Buttons back.
                    mapButton.setVisibility(View.VISIBLE);
                    playButton.setVisibility(View.VISIBLE);
                    restButton.setVisibility(View.VISIBLE);
                    feedButton.setVisibility(View.VISIBLE);

                    //Remove disabled buttons
                    playButtonDisabled.setVisibility(View.INVISIBLE);
                    mapButtonDisabled.setVisibility(View.INVISIBLE);

                    idleFaceMovement.start();

                    if (studyClicks == 1) {

                        sound.playNoticeSound();
                        //Study Tutorial Dialog Box.
                        ImageView bookImage = new ImageView(MainActivity.this);
                        bookImage.setImageResource(R.drawable.ic_book);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Study!")
                                .setMessage("By studying, you reach new grade levels. Try studying longer to reach Kindergarten.")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //code if they select "yes"
                                    }
                                })
                                .setView(bookImage);

                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                } else {
                    startTimer();
                }
                //Saves grade level
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor gradeEditor = sharedPreferences.edit();
                gradeEditor.putString(TEXT, gradeLevel.getText().toString());
                gradeEditor.apply();
            }
        });

        //Play Button
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (age >= 3) {
                    //If player is older than 3, .....

                    // setup the alert builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Pick a game");
                    builder.setIcon(R.drawable.playbuton);
                    // add a list
                    String[] activities = {"Tic Tac Cute", "Connect Me", "I Spy", "Tu-Color"};
                    builder.setItems(activities, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0: {Toast.makeText(getApplicationContext(), "Coming Soon!", Toast.LENGTH_SHORT).show();}
                                break;
                                case 1: {Toast.makeText(getApplicationContext(), "Coming Soon!", Toast.LENGTH_SHORT).show();}
                                break;
                                case 2: {Toast.makeText(getApplicationContext(), "Coming Soon!", Toast.LENGTH_SHORT).show();}
                                break;
                                case 3: {Toast.makeText(getApplicationContext(), "Coming Soon!", Toast.LENGTH_SHORT).show();}
                                break;
                                case 4: //
                            }
                        }
                    });

                    // create and show the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else {
                    //themeSong.pause();
                    //Show age requirement dialog.
                    sound.playNoticeSound();
                    ImageView clockImage = new ImageView(MainActivity.this);
                    clockImage.setImageResource(R.drawable.ic_timer);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Oops!")
                            .setMessage("Games available at age 3.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //code if they select "Okay"
                                    //themeSong.start();
                                }
                            })
                            .setView(clockImage);

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        //Map button
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View map) {

                if (gradeLevel.getText().toString().contentEquals("5th")) {
                    //If player is in 5th grade, .....

                    Toast.makeText(getApplicationContext(), "Soon to come!", Toast.LENGTH_SHORT).show();
                }
                else if (gradeLevel.getText().toString().contentEquals("6th")) {
                    //If player is in 5th grade, .....

                    Toast.makeText(getApplicationContext(), "Soon to come!", Toast.LENGTH_SHORT).show();
                }
                else if (gradeLevel.getText().toString().contentEquals("7th")) {
                    //If player is in 5th grade, .....

                    Toast.makeText(getApplicationContext(), "Soon to come!", Toast.LENGTH_SHORT).show();
                }
                else if (gradeLevel.getText().toString().contentEquals("8th")) {
                    //If player is in 5th grade, .....

                    Toast.makeText(getApplicationContext(), "Soon to come!", Toast.LENGTH_SHORT).show();

                }
                else if (gradeLevel.getText().toString().contentEquals("9th")) {
                    //If player is in 5th grade, .....

                    Toast.makeText(getApplicationContext(), "Soon to come!", Toast.LENGTH_SHORT).show();
                }
                else if (gradeLevel.getText().toString().contentEquals("10th")) {
                    //If player is in 5th grade, .....

                    Toast.makeText(getApplicationContext(), "Soon to come!", Toast.LENGTH_SHORT).show();
                }
                else if (gradeLevel.getText().toString().contentEquals("11th")) {
                    //If player is in 5th grade, .....

                    Toast.makeText(getApplicationContext(), "Soon to come!", Toast.LENGTH_SHORT).show();
                }
                else if (gradeLevel.getText().toString().contentEquals("12th")) {
                    //If player is in 5th grade, .....

                    Toast.makeText(getApplicationContext(), "Soon to come!", Toast.LENGTH_SHORT).show();
                }
                else if (gradeLevel.getText().toString().contentEquals("C")) {
                    //If player is in 5th grade, .....

                    Toast.makeText(getApplicationContext(), "Soon to come!", Toast.LENGTH_SHORT).show();
                }
                else  if (gradeLevel.getText().toString().contentEquals("M")) {
                    //If player is in 5th grade, .....

                    Toast.makeText(getApplicationContext(), "Soon to come!", Toast.LENGTH_SHORT).show();
                }else {
                    //themeSong.pause();
                    //Show age requirement dialog.
                    sound.playNoticeSound();
                    ImageView bookImage = new ImageView(MainActivity.this);
                    bookImage.setImageResource(R.drawable.ic_book);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Gain More Experience!")
                            .setMessage("You don't have enough knowledge to explore yet. Exploration is available at 5th grade.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //code if they select "Okay"
                                    //themeSong.start();
                                }
                            })
                            .setView(bookImage);

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                //Code when timer isn't running.
            }
        }.start();

        mTimerRunning = true;

        //Code for when timer IS running (every tick)
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        //code when timer is paused and not studying
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        knowledgeValue.setText(timeLeftFormatted);

        if (gradeLevel.getText().toString().contentEquals("")) {

            if (mTimeLeftInMillis <= 86399999) {
                treat += 10;
                sound.playGradeSound();
                gradeLevel.setText("K");
                //Eating Tutorial Dialog Box.
                ImageView gradeImage = new ImageView(MainActivity.this);
                gradeImage.setImageResource(R.drawable.certificate);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Kindergarten")
                        .setMessage("Each grade level provides extra treats. Your exposure level is also increased which allows you to explore more of the world.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //code if they select "yes"

                                //Saves grade level
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor gradeEditor = sharedPreferences.edit();
                                gradeEditor.putString(TEXT, gradeLevel.getText().toString());
                                gradeEditor.apply();
                            }
                        })
                        .setView(gradeImage);

                AlertDialog alert = builder.create();
                alert.show();
            }
        }

        if (gradeLevel.getText().toString().contentEquals("K")) {

            if (mTimeLeftInMillis <= 86370000) {
                treat += 20;
                sound.playGradeSound();
                gradeLevel.setText("1st");
                //Eating Tutorial Dialog Box.
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("1st Grade!")
                        .setMessage("")
                        .setIcon(R.drawable.studybuttonattributes)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //code if they select "yes"

                                //Saves grade level
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor gradeEditor = sharedPreferences.edit();
                                gradeEditor.putString(TEXT, gradeLevel.getText().toString());
                                gradeEditor.apply();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }

        if (gradeLevel.getText().toString().contentEquals("1st")) {

            if (mTimeLeftInMillis <= 86318000) {
                treat += 30;
                sound.playGradeSound();
                gradeLevel.setText("2nd");
                //Eating Tutorial Dialog Box.
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("2nd Grade!")
                        .setMessage("")
                        .setIcon(R.drawable.studybuttonattributes)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //code if they select "yes"
                                //Saves grade level
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor gradeEditor = sharedPreferences.edit();
                                gradeEditor.putString(TEXT, gradeLevel.getText().toString());
                                gradeEditor.apply();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }

        if (gradeLevel.getText().toString().contentEquals("2nd")) {

            if (mTimeLeftInMillis <= 85900000) {
                treat += 50;
                sound.playGradeSound();
                gradeLevel.setText("3rd");
                //Eating Tutorial Dialog Box.
                ImageView badgeImage = new ImageView(MainActivity.this);
                badgeImage.setImageResource(R.drawable.badge1);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("3rd Grade!")
                        .setMessage("")
                        .setIcon(R.drawable.studybuttonattributes)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //code if they select "yes"
                                //Saves grade level
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor gradeEditor = sharedPreferences.edit();
                                gradeEditor.putString(TEXT, gradeLevel.getText().toString());
                                gradeEditor.apply();
                            }
                        })
                        .setView(badgeImage);

                AlertDialog alert = builder.create();
                alert.show();
            }
        }

        if (gradeLevel.getText().toString().contentEquals("3rd")) {

            if (mTimeLeftInMillis <= 85400000) {
                treat += 50;
                sound.playGradeSound();
                gradeLevel.setText("4th");
                //Eating Tutorial Dialog Box.
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("4th Grade!")
                        .setMessage("")
                        .setIcon(R.drawable.studybuttonattributes)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //code if they select "yes"
                                //Saves grade level
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor gradeEditor = sharedPreferences.edit();
                                gradeEditor.putString(TEXT, gradeLevel.getText().toString());
                                gradeEditor.apply();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }

        if (gradeLevel.getText().toString().contentEquals("4th")) {

            if (mTimeLeftInMillis <= 84400000) {
                treat += 50;
                sound.playGradeSound();
                gradeLevel.setText("5th");
                //Eating Tutorial Dialog Box.
                ImageView badgeImage = new ImageView(MainActivity.this);
                badgeImage.setImageResource(R.drawable.ic_map);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("5th Grade!")
                        .setIcon(R.drawable.studybuttonattributes)
                        .setMessage("You are now able to travel. Click the map to begin your journey")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //code if they select "yes"
                                //Saves grade level
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor gradeEditor = sharedPreferences.edit();
                                gradeEditor.putString(TEXT, gradeLevel.getText().toString());
                                gradeEditor.apply();
                            }
                        })
                        .setView(badgeImage);

                AlertDialog alert = builder.create();
                alert.show();
            }
        }

        if (gradeLevel.getText().toString().contentEquals("5th")) {

            if (mTimeLeftInMillis <= 83400000) {
                treat += 50;
                sound.playGradeSound();
                gradeLevel.setText("6th");
                //Eating Tutorial Dialog Box.
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("6th Grade!")
                        .setMessage("")
                        .setIcon(R.drawable.studybuttonattributes)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //code if they select "yes"
                                //Saves grade level
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor gradeEditor = sharedPreferences.edit();
                                gradeEditor.putString(TEXT, gradeLevel.getText().toString());
                                gradeEditor.apply();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }

        if (gradeLevel.getText().toString().contentEquals("6th")) {

            if (mTimeLeftInMillis <= 82400000) {
                treat += 50;
                sound.playGradeSound();
                gradeLevel.setText("7th");
                //Eating Tutorial Dialog Box.
                ImageView badgeImage = new ImageView(MainActivity.this);
                badgeImage.setImageResource(R.drawable.badge3);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("7th Grade!")
                        .setMessage("")
                        .setIcon(R.drawable.studybuttonattributes)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //code if they select "yes"
                                //Saves grade level
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor gradeEditor = sharedPreferences.edit();
                                gradeEditor.putString(TEXT, gradeLevel.getText().toString());
                                gradeEditor.apply();
                            }
                        })
                        .setView(badgeImage);

                AlertDialog alert = builder.create();
                alert.show();
            }
        }

        if (gradeLevel.getText().toString().contentEquals("7th")) {

            if (mTimeLeftInMillis <= 80400000) {
                treat += 50;
                sound.playGradeSound();
                gradeLevel.setText("8th");
                //Eating Tutorial Dialog Box.
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("8th Grade!")
                        .setMessage("")
                        .setIcon(R.drawable.studybuttonattributes)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //code if they select "yes"
                                //Saves grade level
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor gradeEditor = sharedPreferences.edit();
                                gradeEditor.putString(TEXT, gradeLevel.getText().toString());
                                gradeEditor.apply();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }

        if (gradeLevel.getText().toString().contentEquals("8th")) {

            if (mTimeLeftInMillis <= 79400000) {
                treat += 100;
                sound.playGradeSound();
                gradeLevel.setText("9th");
                //Eating Tutorial Dialog Box.
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("9th Grade!")
                        .setMessage("")
                        .setIcon(R.drawable.studybuttonattributes)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //code if they select "yes"
                                //Saves grade level
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor gradeEditor = sharedPreferences.edit();
                                gradeEditor.putString(TEXT, gradeLevel.getText().toString());
                                gradeEditor.apply();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }

        if (gradeLevel.getText().toString().contentEquals("9th")) {

            if (mTimeLeftInMillis <= 78400000) {
                treat += 100;
                sound.playGradeSound();
                gradeLevel.setText("10th");
                //Eating Tutorial Dialog Box.
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("10th Grade!")
                        .setMessage("")
                        .setIcon(R.drawable.studybuttonattributes)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //code if they select "yes"
                                //Saves grade level
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor gradeEditor = sharedPreferences.edit();
                                gradeEditor.putString(TEXT, gradeLevel.getText().toString());
                                gradeEditor.apply();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }

        if (gradeLevel.getText().toString().contentEquals("10th")) {

            if (mTimeLeftInMillis <= 70400000) {
                treat += 100;
                sound.playGradeSound();
                gradeLevel.setText("11th");
                //Eating Tutorial Dialog Box.
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("11th Grade!")
                        .setMessage("")
                        .setIcon(R.drawable.studybuttonattributes)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //code if they select "yes"
                                //Saves grade level
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor gradeEditor = sharedPreferences.edit();
                                gradeEditor.putString(TEXT, gradeLevel.getText().toString());
                                gradeEditor.apply();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }

        if (gradeLevel.getText().toString().contentEquals("11th")) {

            if (mTimeLeftInMillis <= 67613000) {
                treat += 100;
                sound.playGradeSound();
                gradeLevel.setText("12th");
                //Eating Tutorial Dialog Box.
                ImageView badgeImage = new ImageView(MainActivity.this);
                badgeImage.setImageResource(R.drawable.badge4);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("12th Grade!")
                        .setMessage("")
                        .setIcon(R.drawable.studybuttonattributes)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //code if they select "yes"
                                //Saves grade level
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor gradeEditor = sharedPreferences.edit();
                                gradeEditor.putString(TEXT, gradeLevel.getText().toString());
                                gradeEditor.apply();
                            }
                        })
                        .setView(badgeImage);

                AlertDialog alert = builder.create();
                alert.show();
            }
        }

        if (gradeLevel.getText().toString().contentEquals("12th")) {

            if (mTimeLeftInMillis <= 66400000) {
                treat += 200;
                sound.playGradeSound();
                gradeLevel.setText("C");
                //Eating Tutorial Dialog Box.
                ImageView badgeImage = new ImageView(MainActivity.this);
                badgeImage.setImageResource(R.drawable.badge5);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("College!")
                        .setIcon(R.drawable.studybuttonattributes)
                        .setMessage("Congratulations! You have successfully graduated high school!")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //code if they select "yes"
                                //Saves grade level
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor gradeEditor = sharedPreferences.edit();
                                gradeEditor.putString(TEXT, gradeLevel.getText().toString());
                                gradeEditor.apply();
                            }
                        })
                        .setView(badgeImage);

                AlertDialog alert = builder.create();
                alert.show();
            }
        }

        if (gradeLevel.getText().toString().contentEquals("C")) {

            if (mTimeLeftInMillis <= 46400000) {
                treat += 500;
                sound.playGradeSound();
                gradeLevel.setText("M");
                //Eating Tutorial Dialog Box.
                ImageView badgeImage = new ImageView(MainActivity.this);
                badgeImage.setImageResource(R.drawable.badge6);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Masters Degree!")
                        .setIcon(R.drawable.studybuttonattributes)
                        .setMessage("Wow! You now have your masters! Not much more to learn!")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //code if they select "yes"
                                //Saves grade level
                                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                SharedPreferences.Editor gradeEditor = sharedPreferences.edit();
                                gradeEditor.putString(TEXT, gradeLevel.getText().toString());
                                gradeEditor.apply();
                            }
                        })
                        .setView(badgeImage);

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

    }
}