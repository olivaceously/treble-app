package com.treble.www.treble;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Olivia on 11/11/2016.
 */

public class SearchSong extends AsyncTask<Void, Integer, JSONArray> {

        private static String convertStreamToString(java.io.InputStream is) {
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }

        private Context copyOfContext;

        public SearchSong (Context context, EditText query) {
            super();
            copyOfContext = context;
        }

        @Override
        protected JSONArray doInBackground(Void... params) {

            @SuppressWarnings("UnusedAssignment") InputStream is = null;


            try {
                URL api = new URL(MainActivity.SEARCH_API_URL + "?song=");//query.getText().toString());
                HttpURLConnection conn = (HttpURLConnection) api.openConnection();

                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                conn.connect();
                @SuppressWarnings("UnusedAssignment") int response = conn.getResponseCode();
                is = conn.getInputStream();

                String contentAsString = convertStreamToString(is);

                //noinspection UnnecessaryLocalVariable
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
            @SuppressWarnings("UnusedAssignment") int numSongs = 0, count = 0;
            @SuppressWarnings("Convert2Diamond") ArrayList<Song> songs = new ArrayList<Song>();

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
                    AddSong.searchList.setAdapter(new CustomListAdapter(copyOfContext, songs));
                }
            }
            catch (JSONException e) {
                Log.d("okay", "wtf man");

            }
        }

    }


