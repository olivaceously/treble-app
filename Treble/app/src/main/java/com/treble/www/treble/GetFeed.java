package com.treble.www.treble;

/**
 * Created by David McConnell on 10/16/16.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

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

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private Context copyOfContext;

    public GetFeed (Context context) {
        super();
        copyOfContext = context;
    }

    double lat = 50;
    double lng = 50;

    @Override
    protected JSONArray doInBackground(Void... params) {

        InputStream is = null;

        try {
            URL api = new URL(MainActivity.SONG_API_URL + "?lat=" + lat + "&lng=" + lng);
            HttpURLConnection conn = (HttpURLConnection) api.openConnection();

            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();

            String contentAsString = convertStreamToString(is);
            Log.d("hi", contentAsString);

            JSONArray array = new JSONArray(contentAsString);
            return array;
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

    protected void onPostExecute(JSONArray songsRaw) {
        JSONObject song;
        int numSongs = 0, count = 0;
        ArrayList<Song> songs = new ArrayList<Song>();

        try {
            numSongs = songsRaw.length();
            if (numSongs > 0) {
                while (count < numSongs) {
                    song = songsRaw.getJSONObject(count);
                    Song s = new Song();
                    s.setId(count);
                    s.setSpotify_id(song.getString("id"));
                    s.setUri(song.getString("uri"));
                    s.setToken(song.getString("token"));
                    s.setLat(song.getDouble("lat"));
                    s.setLng(song.getDouble("lng"));
                    s.setDateAdded(song.getString("dateAdded"));
                    s.setTitle(song.getString("title"));
                    s.setArtist(song.getString("artist"));
                    s.setAlbum(song.getString("album"));
                    s.setArt(song.getJSONArray("art"));
                    songs.add(s);
                    Log.d("hi", s.getAlbum());
                    count++;
                }
                Collections.reverse(songs);
                MainActivity.feedView.setAdapter(new CustomListAdapter(copyOfContext, songs));
            }
        }
        catch (JSONException e) {
            Log.d("okay", "wtf man");

        }
    }

}
