package com.example.ezast.neptune;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class SoundPlayer {

    private AudioAttributes audioAttributes;
    final int SOUND_POOL_MAX = 2;

    private static SoundPool soundPool;
    private static int talkSound;
    private static int eatingSound;
    private static int sleepSound;
    private static int readSound;
    private static int gradeSound;
    private static int noticeSound;
    private static int treatSound;
    private static int touchSound;
    private static int themeSound;


    public SoundPlayer(Context context){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(SOUND_POOL_MAX)
                    .build();
        } else {
            soundPool = new SoundPool(SOUND_POOL_MAX, AudioManager.STREAM_MUSIC, 0);
        }
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,0);

        talkSound = soundPool.load(context, R.raw.fullintro, 1);
        eatingSound = soundPool.load(context, R.raw.tucyuteate, 1);
        sleepSound = soundPool.load(context, R.raw.sleeping, 1);
        readSound = soundPool.load(context, R.raw.readingsound, 1);
        gradeSound = soundPool.load(context, R.raw.gradelevelsound, 1);
        noticeSound = soundPool.load(context, R.raw.notification, 1);
        treatSound = soundPool.load(context, R.raw.treatsound, 1);
        themeSound = soundPool.load(context, R.raw.theme, 1);

    }

    public void playTalkSound(){
        soundPool.play(talkSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playEatingSound(){
        soundPool.play(eatingSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    public void playSleepSound(){
        soundPool.play(sleepSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    public void playReadSound(){
        soundPool.play(readSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    public void playGradeSound(){
        soundPool.play(gradeSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    public void playNoticeSound(){
        soundPool.play(noticeSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    public void playTreatSound(){
        soundPool.play(treatSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    public void playTouchSound(){
        soundPool.play(touchSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    public void playThemeSound(){
        soundPool.play(themeSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }


}
