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
import com.sas.epilepstop.models.Contacts;
import com.sas.epilepstop.models.Seizure;

import org.joda.time.format.DateTimeFormat;
import java.util.ArrayList;
import java.util.List;

public class ContactsCustomAdapter extends ArrayAdapter<Contacts> {


    List<Contacts> contacts;

    public ContactsCustomAdapter(Context context, List<Contacts> contacts) {
        super(context, R.layout.contact_item, contacts);
        this.contacts = contacts;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater =  LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.contact_item, parent,false);
        }

        final Contacts contact = getItem(position);


        TextView contact_number = (TextView) convertView.findViewById(R.id.contact_number);

        contact_number.setText(contact.getNumber());


        return convertView;
    }

    @Override
    public Contacts getItem(int position)
    {
        return contacts.get(position);
    }
}
