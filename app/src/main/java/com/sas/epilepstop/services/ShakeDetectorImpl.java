package com.sas.epilepstop.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.github.tbouron.shakedetector.library.ShakeDetector;

public class ShakeDetectorImpl implements ShakeDetector.OnShakeListener  {

    private int count = 0;

    private Context mContext;

    //get system current milliseconds
    long time = System.currentTimeMillis();

    public ShakeDetectorImpl(Context mContext) {
        this.mContext = mContext;


        ShakeDetector.create(mContext, this);
        ShakeDetector.updateConfiguration(4F,4);



    }

    @Override
    public void OnShake() {


        Toast.makeText(mContext, "Device shaken!", Toast.LENGTH_SHORT).show();
        Log.d("SHAKE"," ");


        Intent intentSync = new Intent("SHAKE_EVENT");
        mContext.sendBroadcast(intentSync);
    }

    protected void init() {

        ShakeDetector.start();
    }

    protected void selfDestruct() {
        ShakeDetector.stop();
        ShakeDetector.destroy();
    }
}
