package com.sas.epilepstop.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.sas.epilepstop.models.Contacts;
import com.sas.epilepstop.models.ObjectBox;
import com.sas.epilepstop.models.Seizure;
import com.sas.epilepstop.R;

import io.objectbox.Box;

public class ContactsFragment extends android.support.v4.app.Fragment {

    Box<Contacts> contactsBox;
    ContactsCustomAdapter adapter;
    ListView listview_contacts;
    Context context;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_contacts, container, false);

        context = getActivity().getApplicationContext();


        contactsBox = ObjectBox.get().boxFor(Contacts.class);

        adapter = new ContactsCustomAdapter(context, contactsBox.getAll());

        listview_contacts = view.findViewById(R.id.listView_contacts);
        listview_contacts.setAdapter(adapter);
        registerForContextMenu(listview_contacts);


        mSwipeRefreshLayout = view.findViewById(R.id.contacts_tab_swipe);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.purpleveryLight, R.color.purpleLight, R.color.purpleDark);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //handling swipe refresh
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        listview_contacts.setAdapter(null);
                        adapter = new ContactsCustomAdapter(context, contactsBox.getAll());
                        listview_contacts.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        listview_contacts.smoothScrollToPosition(0);
                    }
                }, 1500);
            }
        });




        EditText entered_number = view.findViewById(R.id.enter_contact);

        Button btn_ok  = view.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!entered_number.getText().toString().equals("")) {

                    mSwipeRefreshLayout.setRefreshing(true);
                    Contacts contact = new Contacts();
                    contact.setNumber(entered_number.getText().toString());
                    contactsBox.put(contact);
                    entered_number.setText("");

                    listview_contacts.setAdapter(null);
                    adapter = new ContactsCustomAdapter(context, contactsBox.getAll());
                    listview_contacts.setAdapter(adapter);
                    mSwipeRefreshLayout.setRefreshing(false);

                }

            }
        });





        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.contextmenu_listview,menu);


        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // int index = item.getItemId();

                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int index = info.position;

                //if (item.ti == "Delete") {
                Contacts contact = (Contacts) listview_contacts.getAdapter().getItem(index);
                contactsBox.remove(contact);
                Toast.makeText(getActivity().getApplicationContext(), "Contact:" + contact.getNumber()+ " has been deleted", Toast.LENGTH_SHORT).show();
                listview_contacts.setAdapter(null);
                adapter = new ContactsCustomAdapter(context, contactsBox.getAll());
                listview_contacts.setAdapter(adapter);

                //}

                return false;
            }


        });

    }

}
