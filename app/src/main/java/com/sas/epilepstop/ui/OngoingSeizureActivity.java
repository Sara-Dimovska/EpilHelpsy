package com.sas.epilepstop.ui;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

import com.sas.epilepstop.R;
import com.sas.epilepstop.models.ObjectBox;
import com.sas.epilepstop.models.Seizure;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.objectbox.Box;


public class OngoingSeizureActivity extends Activity {

    MediaPlayer mp = null;
    CountDownTimer timer;
    private Button pauseButton;
    private long startTime = 0L;
    private Handler myHandler = new Handler();
    long timeInMillies = 0L;
    long timeSwap = 0L;
    long finalTime = 0L;
    String time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_seizure);

        /*
        mp = MediaPlayer.create(this,R.raw.finall); // sound is inside res/raw/mysound
        mp.start();


        // for the song
        timer =  new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                mp.stop();
                mp.release();
            }
        }.start();*/


        startTime = SystemClock.uptimeMillis();
        myHandler.postDelayed(updateTimerMethod, 0);

        pauseButton = findViewById(R.id.btn_finish);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeSwap += timeInMillies;
                myHandler.removeCallbacks(updateTimerMethod);
                //Log.i("TIme", " " + time);

                Box<Seizure> seizureBox = ObjectBox.get().boxFor(Seizure.class);
                Seizure seizure = new Seizure();
                seizure.setDuration(time);


                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
                String date = curFormater.format(calendar.getTime());

                seizure.setDate(date);
                seizureBox.put(seizure);

                // TODO: save record and hanhle media  player
                //Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                //OngoingSeizureActivity.this.startActivity(intent);
                //finish();

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Runnable updateTimerMethod = new Runnable() {

        public void run() {
            timeInMillies = SystemClock.uptimeMillis() - startTime;
            finalTime = timeSwap + timeInMillies;

            int seconds = (int) (finalTime / 1000);

            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (finalTime % 1000);

            time = minutes + ":"
                    + String.format("%02d", seconds);

            /*
            Log.i("TIME","" + minutes + ":"
                    + String.format("%02d", seconds) + ":"
                    + String.format("%03d", milliseconds));*/



            myHandler.postDelayed(this, 0);
        }

    };

}

