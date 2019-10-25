package com.sas.epilepstop.services;

import android.content.Context;

import com.sas.epilepstop.models.Seizure;

import java.util.ArrayList;
import java.util.UUID;

public class ListSingleton {

    private ArrayList<Seizure> mSeizures;
    private Context mAppContext;
    private static ListSingleton sSeizuresLab;

    // private constructor and get() = singleton
    private ListSingleton(Context appContext) {
        mAppContext = appContext;
        mSeizures =  new ArrayList<>();
    }

    public void addSeizure(Seizure c) {
        mSeizures.add(c);
    }

    public static ListSingleton getInstance(Context c) {
        if (sSeizuresLab == null) {
            sSeizuresLab = new ListSingleton(c.getApplicationContext());
        }
        return sSeizuresLab;
    }
    public void deleteSeizure(Seizure c) {
        mSeizures.remove(c);
    }
    public ArrayList<Seizure> getmSeizures() {
        return mSeizures;
    }

    /*
    public Seizure getSeizure(long id) {
        for (Seizure c : mSeizures) {
            if (c.getId() == id)
                return c;
        }
        return null;
    }*/
}

