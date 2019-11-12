package com.sas.epilepstop.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sas.epilepstop.R;
import com.sas.epilepstop.models.Contacts;
import com.sas.epilepstop.models.Contacts_;
import com.sas.epilepstop.models.ObjectBox;
import com.sas.epilepstop.services.GPSTracker;

import java.util.List;

import io.objectbox.Box;

public class HomeFragment extends Fragment {
    GPSTracker gpsTracker;
    String locationLINK;
    Context mContext;
    List<Contacts> contactElements;
    String message;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getActivity().getApplicationContext();

        Button help_now = view.findViewById(R.id.btn_help_now);
        help_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // set location link
                gpsTracker = new GPSTracker( mContext);
                setLocationLink();
                // send sms

                // get  emergency contacts
                Box<Contacts> contactsBox = ObjectBox.get().boxFor(Contacts.class);
                contactElements = contactsBox.getAll();

                String[] numbers = contactsBox.query().build()
                        .property(Contacts_.number)
                        .findStrings();


                message = "Your patient is having a seizure right now on this location: " + locationLINK;
                for(int i=0; i< numbers.length; i++) {


                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(numbers[i], null, message, null, null);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }


                }


            }
        });

        return  view;

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
        if (!(getActivity().isFinishing())) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
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
                            getActivity().finish();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }
}
