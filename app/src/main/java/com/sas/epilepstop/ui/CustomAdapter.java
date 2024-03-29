package com.sas.epilepstop.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sas.epilepstop.R;
import com.sas.epilepstop.models.Seizure;

import org.joda.time.format.DateTimeFormat;
import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<Seizure> {


    List<Seizure> seizures;

    public CustomAdapter(Context context, List<Seizure> seizures) {
        super(context, R.layout.seizure_item, seizures);
        this.seizures = seizures;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater =  LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.seizure_item, parent,false);
        }

        final Seizure seizure = getItem(position);


        TextView seizureDate = (TextView) convertView.findViewById(R.id.txtDate);
        seizureDate.setText(seizure.getDate());
        //seizureDate.setText(seizure.getDate().toString(DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss")));

        TextView seizureDuration = (TextView) convertView.findViewById(R.id.txtDuration);
        seizureDuration.setText("Duration: "+ seizure.getDuration());

        return convertView;
    }

    @Override
    public Seizure getItem(int position)
    {
        return seizures.get(position);
    }
}
