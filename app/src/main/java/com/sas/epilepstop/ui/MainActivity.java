package com.sas.epilepstop.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.sas.epilepstop.R;
import com.sas.epilepstop.services.DetectionService;

public class MainActivity extends Activity {
    boolean started = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageButton btnToggleService = findViewById(R.id.onOff);
        final Button btnHistory = findViewById(R.id.history);

        if (started) {
            Intent intent = new Intent(getApplicationContext(), DetectionService.class);
            getApplicationContext().startService(intent);
        }

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

                if (!started) {
                    btnToggleService.setBackgroundResource(R.drawable.off);
                } else {
                    btnToggleService.setBackgroundResource(R.drawable.on);
                }
            }
        });


        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, HistoryActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
    }
}
