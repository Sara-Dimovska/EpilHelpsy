package com.sas.epilepstop.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.sas.epilepstop.ui.MainActivity;
import com.sas.epilepstop.ui.SeizureActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class DetectionService extends Service {
   // FallDetectorImpl mFallDetector;
    ShakeDetectorImpl mShakeDetector;
    MainActivity ob;

    public DetectionService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        //mFallDetector = new FallDetectorImpl(getApplicationContext());
        mShakeDetector = new ShakeDetectorImpl(getApplicationContext());

        //getApplicationContext().registerReceiver(mFallReceiver, new IntentFilter("FALL_EVENT"));
        getApplicationContext().registerReceiver(mShakeReceiver, new IntentFilter("SHAKE_EVENT"));

        ob= new MainActivity();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // mFallDetector.init();
        mShakeDetector.init();

        Toast.makeText(this, "Detection Service Started", Toast.LENGTH_SHORT).show();
       // ob.on_off = false;

        return super.onStartCommand(intent,flags,startId);
    }

    public void onDestroy() {
        super.onDestroy();

        //getApplicationContext().unregisterReceiver(mFallReceiver);
        getApplicationContext().unregisterReceiver(mShakeReceiver);
        //mFallDetector.selfDestruct();
        mShakeDetector.selfDestruct();


        Toast.makeText(this, "Detection Service Stopped", Toast.LENGTH_SHORT).show();
        //ob.on_off = true;
    }


    Handler handler;
    Runnable falseAlarmRunnable = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), "False fall alarm" , Toast.LENGTH_LONG).show();
        }
    };

    /*
    private BroadcastReceiver mFallReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mShakeDetector.init();
            //mFallDetector.selfDestruct();

            Toast.makeText(getApplicationContext(), "Fall detected" , Toast.LENGTH_LONG).show();

            // handler = new Handler(Looper.getMainLooper());
             //handler.postDelayed(falseAlarmRunnable, 7000);
        }
    };
*/
    Boolean startedOnce = false;

    private BroadcastReceiver mShakeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //if (startedOnce) return;

           // if (handler != null) handler.removeCallbacks(falseAlarmRunnable);


            //mShakeDetector.selfDestruct();


         if (SeizureActivity.active == false) {
                Intent dialogIntent = new Intent(DetectionService.this, SeizureActivity.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);

                //startedOnce = true;
                Toast.makeText(getApplicationContext(), "Seizure detected", Toast.LENGTH_LONG).show();
            }
        }
    };
}
