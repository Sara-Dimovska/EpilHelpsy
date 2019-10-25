package com.sas.epilepstop.services;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.SENSOR_SERVICE;

public class FallDetectorImpl implements SensorEventListener {
    private SensorManager sensorManager;
    public boolean min,max;
    public int i=0;

    private Context mContext;

    public FallDetectorImpl(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onSensorChanged(SensorEvent arg0) {
        // TODO Auto-generated method stub
        if (arg0.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
            double gvt=SensorManager.STANDARD_GRAVITY;

            float vals[] = arg0.values;
            //int sensor=arg0.sensor.getType();
            double x = arg0.values[0];
            double y = arg0.values[1];
            double z= arg0.values[2];



            double magnitude =Math.round(Math.sqrt(Math.pow(x, 2)
                    +Math.pow(y, 2)
                    +Math.pow(z, 2)));



            if (magnitude >= 23) {
               // Toast.makeText(mContext, "Fall!", Toast.LENGTH_SHORT).show();
                Log.d("FALL"," " + magnitude);
            }

            /*
            if (aaa<=3d0) { // free fall
                min=true;
                //mintime=System.currentTimeMillis();
            }

            if (min==true) {
                i++;
                if(aaa>=15.5) { // landing
                    max=true;
                }
            }

            // Log.d("Test",new Double(aaa).toString() + ' ' + new Double(aaa).toString());

            if (min==true && max==true) {
                Intent intentSync = new Intent("FALL_EVENT");

                mContext.sendBroadcast(intentSync);

                i=0;
                min=false;
                max=false;
            }

            if (i>4) {
                i=0;
                min=false;
                max=false;
            }*/
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void init() {
        sensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
    }

    public void selfDestruct() {
        sensorManager.unregisterListener(this);
    }

}
