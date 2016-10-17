package com.treble.www.treble;

/**
 * Created by David McConnell on 10/16/16.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class GetFeed extends AsyncTask<Void, Integer, JSONArray> {

    double lat = 50;
    double lng = 50;

    @Override
    protected JSONArray doInBackground(Void... params) {
        try {
            URL api = new URL(MainActivity.SONG_API_URL + "?lat=" + lat + "&lng=" + lng);
            HttpURLConnection conn = (HttpURLConnection)api.openConnection();

            // See http://stackoverflow.com/questions/2492076/android-reading-from-an-input-stream-efficiently
            InputStream is = conn.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            r.close();
            is.close();
            return new JSONArray(total.toString());
        }
        catch (MalformedURLException e) {
            Log.e("GetFeed doInBG(): ", e.toString());
            return null;
        }
        catch (IOException e) {
            Log.e("GetFeed doInBG(): ", e.toString());
            return null;
        }
        catch (JSONException e) {
            Log.e("GetFeed doInBG(): ", e.toString());
            return null;
        }
    }

}
