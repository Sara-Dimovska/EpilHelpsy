package com.sas.epilepstop.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.sas.epilepstop.R;
import com.sas.epilepstop.models.ObjectBox;
import com.sas.epilepstop.services.DetectionService;

public class MainActivity extends Activity {
    boolean started = false;
    ImageButton btnToggleService;
    Button contacts, journal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ObjectBox.init(this);


/*
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Toast.makeText(MainActivity.this, "Recents", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_contacts:
                        Toast.makeText(MainActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_history:
                        Toast.makeText(MainActivity.this, "Nearby", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });*/



        btnToggleService = findViewById(R.id.onOff);
        btnToggleService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DetectionService.class);
                if (!started) {
                    getApplicationContext().startService(intent);
                } else {
                    getApplicationContext().stopService(intent);
                }

                started = !started;

                if (started) {
                    btnToggleService.setBackgroundResource(R.drawable.on);
                } else {
                    btnToggleService.setBackgroundResource(R.drawable.off);
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
