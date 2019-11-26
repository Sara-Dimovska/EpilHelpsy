package com.sas.epilepstop.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sas.epilepstop.R;
import com.sas.epilepstop.models.Seizure;
import com.sas.epilepstop.services.PrefHelper;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SeizureActivity extends Activity {

    Chronometer chronometer_up;

    Boolean isFalse = false;
    Boolean hasFinished = false;
    public static boolean active = false;
    MediaPlayer mp = null;
    ProgressBar progressBar;
    TextView time_remaining;
    int counter=20;
    Button btn_disarm;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        active = true;
        setContentView(R.layout.activity_seizure);


        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }


         time_remaining = findViewById(R.id.time_txt);
         timer =  new CountDownTimer(20000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time_remaining.setText("Time remaining:" + String.valueOf(counter));
                counter--;
            }
            @Override
            public void onFinish() {

                time_remaining.setText("Finished");
                Intent intent = new Intent(getApplicationContext(), OngoingSeizureActivity.class);
                SeizureActivity.this.startActivity(intent);
                finish();
            }
        }.start();


        btn_disarm =findViewById(R.id.buttonDisarm);
        btn_disarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(SeizureActivity.this, MainActivity.class);
                SeizureActivity.this.startActivity(myIntent);
                timer.cancel();
                finish();
            }
        });



    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        timer.cancel();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        active = false;
    }
}
