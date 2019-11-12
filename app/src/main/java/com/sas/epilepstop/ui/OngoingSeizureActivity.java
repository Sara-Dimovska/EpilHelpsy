package com.sas.epilepstop.ui;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sas.epilepstop.R;
import com.sas.epilepstop.models.Contacts;
import com.sas.epilepstop.models.Contacts_;
import com.sas.epilepstop.models.ObjectBox;
import com.sas.epilepstop.models.Seizure;
import com.sas.epilepstop.services.GPSTracker;
import com.sas.epilepstop.services.NotificationHelper;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.objectbox.Box;


public class OngoingSeizureActivity extends Activity {

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude,longitude;
    String locationLINK;
    List<Contacts> contactElements;
    List<String> numberList;
    String message;
    GPSTracker gpsTracker;


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
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_seizure);


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
        }.start();

        gpsTracker = new GPSTracker(OngoingSeizureActivity.this);
        setLocationLink();


        /*
        //Add permission
        ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

//        locationManager=(LocationManager) getSystemService(this.LOCATION_SERVICE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        //Check gps is enable or not
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            //Write Function To enable gps
            OnGPS();

        }
        else
        {
            //GPS is already On then
            getLocation();
        }




        */

        // get  emergency contacts
        Box<Contacts>  contactsBox = ObjectBox.get().boxFor(Contacts.class);
        contactElements = contactsBox.getAll();

        String[] numbers = contactsBox.query().build()
                .property(Contacts_.number)
                .findStrings();



        message = "Your patient is having a seizure right now on this location: " + locationLINK;
        for(int i=0; i< numbers.length; i++) {


            if (ContextCompat.checkSelfPermission(OngoingSeizureActivity.this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                // Ask for permision
                ActivityCompat.requestPermissions(this,new String[] { Manifest.permission.SEND_SMS}, 1);
            }
            else {
                // Permission has already been granted
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(numbers[i], null, message, null, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

           //
        }

        // send sms api
        // new SendSMSTask().execute();


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
                SimpleDateFormat curFormater = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                String date = curFormater.format(calendar.getTime());

                seizure.setDate(date);
                seizureBox.put(seizure);

                // TODO: save record and hanhle media  player
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                OngoingSeizureActivity.this.startActivity(intent);
                finish();

            }
        });

    }

    private void setLocationLink() {
        if (gpsTracker.getLocation() != null) {
            if (gpsTracker.getLatitude() != 0 && gpsTracker.getLongitude() != 0) {
                locationLINK = "https://maps.google.com/?q=" +  gpsTracker.getLatitude() + "," + gpsTracker.getLongitude();
            } else {
                buildAlertMessageNoGps();
            }
        } else {
            buildAlertMessageNoGps();
        }
    }
    private void buildAlertMessageNoGps() {
        if (!(OngoingSeizureActivity.this).isFinishing()) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Location not detected");
            builder1.setCancelable(true);

            builder1.setPositiveButton("Try again",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            setLocationLink();
                        }
                    });

            builder1.setNegativeButton(
                    android.R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            finish();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    private class SendSMSTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                sendMMS();
            } catch (Exception  e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(OngoingSeizureActivity.this, "SMS sent to emergensy contacts!", Toast.LENGTH_SHORT).show();
            super.onPostExecute(aVoid);
        }
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
                    + String.format("%02d", seconds) + " seconds";

            /*
            Log.i("TIME","" + minutes + ":"
                    + String.format("%02d", seconds) + ":"
                    + String.format("%03d", milliseconds));*/



            myHandler.postDelayed(this, 0);
        }

    };

    private void getLocation() {

        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,

                Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps !=null)
            {
                double lat=LocationGps.getLatitude();
                double longi=LocationGps.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                locationLINK = "https://maps.google.com/?q=" +  latitude + "," + longitude;
            }
            else if (LocationNetwork !=null)
            {
                double lat=LocationNetwork.getLatitude();
                double longi=LocationNetwork.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                locationLINK = "https://maps.google.com/?q=" +  latitude + "," + longitude;
            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                double longi=LocationPassive.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                locationLINK = "https://maps.google.com/?q=" +  latitude + "," + longitude;
            }
            else
            {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void OnGPS() {

        final AlertDialog.Builder builder= new AlertDialog.Builder(this);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                getLocation();
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    public void sendMMS() throws Exception {
        // This URL is used for sending messages
        String myURI = "https://api.bulksms.com/v1/messages";

        // change these values to match your own account
        String myUsername = "epilhelpsy";
        String myPassword = "Athcat!!d3t.bash";

        // the details of the message we want to send
        //String myData = "{to: \"" + numbers_string_forAPI+ "\", encoding: \"UNICODE\", body: \""+ "Your patient is having a seizure right now on this location: " + locationLINK + "\"}";


        // build the request based on the supplied settings
        URL url = new URL(myURI);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setDoOutput(true);

        // supply the credentials
        String authStr = myUsername + ":" + myPassword;
        String authEncoded = Base64.getEncoder().encodeToString(authStr.getBytes());
        request.setRequestProperty("Authorization", "Basic " + authEncoded);

        // we want to use HTTP POST
        request.setRequestMethod("POST");
        request.setRequestProperty( "Content-Type", "application/json");

        // write the data to the request
        OutputStreamWriter out = new OutputStreamWriter(request.getOutputStream());
        //out.write(myData);
        out.close();

        // try ... catch to handle errors nicely
        try {
            // make the call to the API
            InputStream response = request.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(response));
            String replyText;
            while ((replyText = in.readLine()) != null) {
                System.out.println(replyText);
            }
            in.close();
        } catch (IOException ex) {
            System.out.println("An error occurred:" + ex.getMessage());
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getErrorStream()));
            // print the detail that comes with the error
            String replyText;
            while ((replyText = in.readLine()) != null) {
                System.out.println(replyText);
            }
            in.close();
        }
        request.disconnect();

    }

}