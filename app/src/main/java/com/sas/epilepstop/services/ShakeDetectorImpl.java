package com.sas.epilepstop.services;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.github.tbouron.shakedetector.library.ShakeDetector;

public class ShakeDetectorImpl {

    private int count = 0;
    private long startMillis=0;

    private Context mContext;

    //get system current milliseconds
    long time = System.currentTimeMillis();

    public ShakeDetectorImpl(Context mContext) {
        this.mContext = mContext;
    }

    protected void init() {
        ShakeDetector.create(mContext, new ShakeDetector.OnShakeListener() {
            @Override
            public void OnShake() {

                //if it is the first time, or if it has been more than 5 seconds ( so it is like a new try), we reset everything
                if (startMillis==0 || (time-startMillis > 5000) ) {
                    startMillis=time;
                    count=1;
                }
                //it is not the first, and it has been  less than 5 seconds since the first
                else{ //  time-startMillis< 5000
                    count++;
                }

                if (count==5) {
                    //Toast.makeText(getApplicationContext(), "Device shaken!", Toast.LENGTH_SHORT).show();
                    count = 0;
                    startMillis=0;
                }

                // Toast.makeText(mContext, "Device shaken!", Toast.LENGTH_SHORT).show();
                Intent intentSync = new Intent("SHAKE_EVENT");
                mContext.sendBroadcast(intentSync);
            }
        });
        ShakeDetector.start();
    }

    protected void selfDestruct() {
        ShakeDetector.stop();
        ShakeDetector.destroy();
    }
}
