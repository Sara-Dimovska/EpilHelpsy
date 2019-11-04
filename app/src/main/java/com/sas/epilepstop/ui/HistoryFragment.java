package com.sas.epilepstop.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sas.epilepstop.R;
import com.sas.epilepstop.models.ObjectBox;
import com.sas.epilepstop.models.Seizure;
import com.sas.epilepstop.services.ListSingleton;

import java.util.List;

import io.objectbox.Box;

public class HistoryFragment extends Fragment {

    ListView listview_seizures;
    Box<Seizure> seizureBox;
    CustomAdapter adapter;
    Context context;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        context = getActivity().getApplicationContext();
        seizureBox = ObjectBox.get().boxFor(Seizure.class);

        adapter = new CustomAdapter(context, seizureBox.getAll());
        listview_seizures = view.findViewById(R.id.listView);
        listview_seizures.setAdapter(adapter);
        registerForContextMenu(listview_seizures);


        mSwipeRefreshLayout = view.findViewById(R.id.history_swipe);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.purpleveryLight, R.color.purpleLight, R.color.purpleDark);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //handling swipe refresh
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        listview_seizures.setAdapter(null);
                        adapter = new CustomAdapter(context, seizureBox.getAll());
                        listview_seizures.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        listview_seizures.smoothScrollToPosition(0);
                    }
                }, 1500);
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
                    Seizure seizure = (Seizure) listview_seizures.getAdapter().getItem(index);
                    seizureBox.remove(seizure);
                Toast.makeText(getActivity().getApplicationContext(), "Seizure record has been deleted", Toast.LENGTH_SHORT).show();

                listview_seizures.setAdapter(null);
                adapter = new CustomAdapter(context, seizureBox.getAll());
                listview_seizures.setAdapter(adapter);

                //}

                return false;
            }


        });

    }

}
