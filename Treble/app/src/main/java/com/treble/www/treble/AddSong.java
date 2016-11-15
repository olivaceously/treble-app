package com.treble.www.treble;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Created by Olivia on 11/11/2016.
 */

public class AddSong extends Activity {

    static protected ListView searchList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_song);

        Button search = (Button) findViewById(R.id.search_button);
        final EditText query   = (EditText)findViewById(R.id.userQuery);
        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                searchList = (ListView)findViewById(R.id.search_list);
                parseSearch(query);
            }
        });

    }

    private void parseSearch(EditText query) {
        String data = query.getText().toString();
        new SearchSong(getApplicationContext(), data).execute();
    }
}