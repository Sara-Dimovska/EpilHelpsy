package com.sas.epilepstop.services;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationManager;
import android.telephony.SmsManager;
import android.widget.TextView;

public class NotificationHelper {
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    private Context mContext;
    String phoneNumber = "+38978507995";

    private static final int REQUEST_LOCATION = 1;
    Button button;
    TextView textView;
    LocationManager locationManager;
    String lattitude,longitude;

    public NotificationHelper(Context mContext) {
        this.mContext = mContext;
    }

    public void makeACall(){
        if (checkPermission(Manifest.permission.CALL_PHONE)) {
            String dial = "tel:" + phoneNumber;
            Intent dialogIntent = new Intent(Intent.ACTION_CALL, Uri.parse(dial));
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(dialogIntent);
        } else {
            Log.e("NO DIAL PERMISSIONS","CALL DENIED");
        }
    }

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED;
    }


    public void sendSMS(String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, msg, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public String getLocation(){
        String locationLink ="";
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //buildAlertMessageNoGps();
            Log.e(" GPS ERROR", "GPS location not on");

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationLink =  getGPSCoords();
        }

        return locationLink;
    }

    public String getGPSCoords() {

        String URL = "https://maps.google.com/?q=";

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            //Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            //Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                URL += lattitude + "," + longitude;
            } else {
                Log.e("TRACE ERROR", "UNABLE TO TRACE YOUR LOCATION");
            }
        }
        return URL;
    }
}

