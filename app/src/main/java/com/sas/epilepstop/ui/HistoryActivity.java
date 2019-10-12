package com.sas.epilepstop.ui;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.sas.epilepstop.R;
import com.sas.epilepstop.services.ListSingleton;

public class HistoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        CustomAdapter adapter = new CustomAdapter(this, ListSingleton.getInstance(this).getmSeizures());
        ListView list = findViewById(R.id.listView);
        list.setAdapter(adapter);
    }
}
