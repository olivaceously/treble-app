package com.treble.www.treble;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Created by Olivia on 11/11/2016.
 */

public class AddSong extends AppCompatActivity {

    static protected ListView searchList;
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

        Log.d("addsong","inside addsong!");
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
        Log.d("addsong", data);
        new SearchSong(getApplicationContext()).execute(data);
    }
}