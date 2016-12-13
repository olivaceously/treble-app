package com.treble.www.treble;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Olivia on 11/15/2016.
 */

public class AddSongListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Song> songs;
    private double lat;
    private double lng;

    private static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    InputStream is = null;
    private final String USER_AGENT = "Mozilla/5.0";



    public AddSongListAdapter(Context context, ArrayList<Song> songs, double latitude, double longitude) {
        this.context = context;
        this.songs = songs;
        this.lat = latitude;
        this.lng = longitude;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return songs.get(position).id;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled (int position) {
        return false;
    }

    public class Holder
    {
        TextView song;
        TextView artist;
        ImageView album;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final Holder holder = new Holder();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //noinspection RedundantCast
            row = (View)inflater.inflate(R.layout.addsong_list, null);
        }
        else {
            //noinspection RedundantCast
            row = (View)convertView;
        }
        holder.song =(TextView) row.findViewById(R.id.song);
        holder.artist =(TextView) row.findViewById(R.id.artist);
        holder.album =(ImageView) row.findViewById(R.id.album);

        holder.song.setTextColor(Color.BLACK);
        holder.artist.setTextColor(Color.BLACK);
        holder.song.setText(songs.get(position).getTitle());
        holder.artist.setText(songs.get(position).getArtist());
        JSONArray song = songs.get(position).getArt();
        try {
            holder.album.setImageBitmap(getImageBitmap(song.getJSONObject(1).getString("url")));
        } catch(JSONException e) {
            Log.d("err", "error in addsonglistadapter");
        }

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    URL api = new URL(MainActivity.ADD_API_URL);
                    HttpURLConnection conn = (HttpURLConnection) api.openConnection();

                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("User-Agent", USER_AGENT);
                    conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                    String data = "lat=" + lat + "&lng=" + lng + "&spotid=" + songs.get(position).getspotify_id();

                    conn.setDoInput(true);
                    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                    wr.writeBytes(data);
                    wr.flush();
                    wr.close();

                    int responseCode = conn.getResponseCode();
                    System.out.println("\nSending 'POST' request to URL : " + MainActivity.ADD_API_URL);
                    System.out.println("Post parameters : " + data);
                    System.out.println("Response Code : " + responseCode);

                    //noinspection UnnecessaryLocalVariable
                    Toast.makeText(context, "Added song to feed!", Toast.LENGTH_LONG).show();
                    return;
                }
                catch (MalformedURLException e) {
                    Log.e("GetFeed doInBG(): ", e.toString());
                    return;
                }
                catch (IOException e) {
                    Log.e("GetFeed doInBG(): ", e.toString());
                    return;
                }

            }
        });

        return row;
    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("err", "Error getting bitmap", e);
        }
        return bm;
    }

}