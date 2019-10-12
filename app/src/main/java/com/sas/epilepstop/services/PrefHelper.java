package com.sas.epilepstop.services;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Date;

public class PrefHelper extends AsyncTask<Void, Void, Boolean> {
    String name;
    Object value;
    SharedPreferences prefs;
    String type;

    public PrefHelper(String name, Object value, SharedPreferences prefs, String type) {
        this.name = name;
        this.value = value;
        this.prefs = prefs;
        this.type = type;
    }

    public enum DataType {
        INT,
        STRING,
        BOOL,
        DATE
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        DataType dataType = DataType.valueOf(type.toUpperCase());
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        switch (dataType) {
            case STRING:
                editor.putString(name, value.toString());
                break;
            case INT:
                editor.putInt(name, Integer.parseInt(value.toString()));
                break;
            case BOOL:
                editor.putBoolean(name, Boolean.parseBoolean(value.toString()));
                break;
        }
        editor.apply();
        return editor.commit();
    }
}
