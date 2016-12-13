package com.treble.www.treble;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Created by Olivia on 11/11/2016.
 */

public class AddSong extends AppCompatActivity {

    static protected ListView searchList;

    double lat;
    double lng;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_song);
        Toolbar addToolbar = (Toolbar) findViewById(R.id.addToolbar);
        setSupportActionBar(addToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Bundle b  = this.getIntent().getExtras();

        lat = b.getDouble("lat");
        lng = b.getDouble("lng");

        Button search = (Button) findViewById(R.id.search_button);
        final EditText query   = (EditText)findViewById(R.id.userQuery);
        final InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        query.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    searchList = (ListView)findViewById(R.id.search_list);
                    parseSearch(query);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                searchList = (ListView)findViewById(R.id.search_list);
                parseSearch(query);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                setResult(RESULT_OK);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void parseSearch(EditText query) {
        String data = query.getText().toString();
        Log.d("addsong", data);
        new SearchSong(getApplicationContext(), lat, lng).execute(data);
    }
}