package com.sas.epilepstop.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sas.epilepstop.R;
import com.sas.epilepstop.models.Seizure;
import com.sas.epilepstop.services.ListSingleton;
import com.sas.epilepstop.services.NotificationHelper;
import com.sas.epilepstop.services.PrefHelper;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SeizureActivity extends Activity {
    Boolean isFalse = false;
    Boolean hasFinished = false;
    public static boolean active = false;
    Timer timer;
    MediaPlayer mp = null;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        active = true;
        setContentView(R.layout.activity_seizure);

        TextView dos = findViewById(R.id.txtDos);
        progressBar = findViewById(R.id.progressBar);
        dos.setMovementMethod(new ScrollingMovementMethod());

        Button btnFalse = findViewById(R.id.btnGeneral);
        btnFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ongoing) { // false alarm
                    isFalse = true;
                } else { // finish seizure
                    hasFinished = true;
                }
            }
        });

        runFalseAlarmTimer();
    }

    Integer progress = 0;
    Boolean ongoing = false;

    public void showToast(final String msg) {
        SeizureActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(SeizureActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class FalseAlarmTimer extends TimerTask {
        @Override
        public void run() {

            if(progress < 100) {
                progress += 20;
                progressBar.setProgress(progress);

                if(isFalse) {
                    timer.cancel();
                    Intent myIntent = new Intent(SeizureActivity.this, MainActivity.class);
                    SeizureActivity.this.startActivity(myIntent);

                    finish();
                }

            } else if( progress == 100 ) {
                if(!hasFinished) {
                    toggleUI();
                    setStartTime();
                } else  if(hasFinished){
                    saveSeizure();
                    Intent myIntent = new Intent(SeizureActivity.this, HistoryActivity.class);
                    timer.cancel();
                    SeizureActivity.this.startActivity(myIntent);

                    finish();
                }
            }

        }
    }

    public void toggleUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ongoing = true;
                TextView txtDos = findViewById(R.id.txtDos);
                txtDos.setVisibility(View.VISIBLE);
                Button btnFalse = findViewById(R.id.btnGeneral);
                btnFalse.setText("Finish");
            }
        });
    }

    public void setStartTime() {
        DateTime curDate = new DateTime();
        String curDateStr = curDate.toString(DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss"));
        SharedPreferences editor = getApplicationContext().getSharedPreferences("StartTime", MODE_PRIVATE);
        new PrefHelper("startTime", curDateStr, editor, "STRING").execute();
    }

    public void saveSeizure() {
        //if (hasFinished) return;

        SharedPreferences editor = getApplicationContext().getSharedPreferences("StartTime", MODE_PRIVATE);
        String startTime = editor.getString("startTime", "");

        DateTime curDate = new DateTime();

        editor = getApplicationContext().getSharedPreferences("EndTime", MODE_PRIVATE);
        String curDateStr = curDate.toString(DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss"));
        new PrefHelper("endTime", curDateStr, editor, "STRING").execute();
        String endTime = curDateStr;

        if (startTime.length() > 0 && endTime.length() > 0) {
            // showToast(startTime + " - " + endTime);

            DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
            DateTime startTimeObj = dtf.parseDateTime(startTime);
            DateTime endTimeObj = dtf.parseDateTime(endTime);

            Period period = new Period(startTimeObj, endTimeObj);

            PeriodFormatter formatter = new PeriodFormatterBuilder()
                    .appendMinutes().appendSuffix(" minutes ")
                    .appendSeconds().appendSuffix(" seconds ")
                    .printZeroNever()
                    .toFormatter();

            Seizure newSeizure = new Seizure();
            newSeizure.setDate(startTimeObj);
            newSeizure.setDuration(formatter.print(period));
            newSeizure.setId(8);

            ListSingleton.getInstance(getApplicationContext()).addSeizure(newSeizure);

            Log.d("TimeElapsed", formatter.print(period));

            /* MediaPlayer mp = MediaPlayer.create(this); // sound is inside res/raw/mysound
            mp.start(); */




        }
    }


    public void runFalseAlarmTimer() {
        timer = new Timer();
        timer.schedule(new FalseAlarmTimer(), 1000, 1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        active = false;
    }
}
