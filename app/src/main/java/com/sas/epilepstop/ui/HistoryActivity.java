package com.sas.epilepstop.ui;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.sas.epilepstop.R;
import com.sas.epilepstop.models.ObjectBox;
import com.sas.epilepstop.models.Seizure;
import com.sas.epilepstop.services.ListSingleton;

import java.util.List;

import io.objectbox.Box;

public class HistoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Box<Seizure> seizureBox = ObjectBox.get().boxFor(Seizure.class);

        CustomAdapter adapter = new CustomAdapter(this, seizureBox.getAll());
        ListView list = findViewById(R.id.listView);
        list.setAdapter(adapter);
    }
}
