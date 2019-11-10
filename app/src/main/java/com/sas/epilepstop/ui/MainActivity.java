package com.sas.epilepstop.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sas.epilepstop.R;
import com.sas.epilepstop.models.ObjectBox;
import com.sas.epilepstop.services.DetectionService;
import com.sas.epilepstop.services.NotificationHelper;
import com.sas.epilepstop.services.ToggleButtonSingleton;

public class MainActivity extends FragmentActivity {
    boolean started = false;
    ImageButton btnToggleService;
    Button contacts, journal;
    public boolean on_off = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ObjectBox.get() == null) {
            ObjectBox.init(this);

        }



        // start first with homefragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_wrapper,new HomeFragment()).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selected_fragment = null;

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selected_fragment = new HomeFragment();
                        break;
                    case R.id.nav_contacts:
                        selected_fragment = new ContactsFragment();
                        break;
                    case R.id.nav_history:
                        selected_fragment = new HistoryFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_wrapper,selected_fragment).commit();
                return true;
            }
        });


       // boolean on_off =  ToggleButtonSingleton.getInstance();
        btnToggleService = findViewById(R.id.onOff);
        btnToggleService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DetectionService.class);
                if (on_off) {
                    getApplicationContext().startService(intent);
                   // ToggleButtonSingleton.toggle(true);

                } else {
                    getApplicationContext().stopService(intent);
                    //ToggleButtonSingleton.toggle(false);

                }

                //started = !started;


                if (on_off) {
                    btnToggleService.setBackgroundResource(R.drawable.on);
                    on_off = !on_off;
                   // ToggleButtonSingleton.toggle(false);
                } else {
                    btnToggleService.setBackgroundResource(R.drawable.off);
                    on_off = !on_off;
                    //ToggleButtonSingleton.toggle(true);
                }
            }
        });


        /*
        if (started) {
            Intent intent = new Intent(getApplicationContext(), DetectionService.class);
            getApplicationContext().startService(intent);
        }




        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, HistoryActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });*/
    }
}
