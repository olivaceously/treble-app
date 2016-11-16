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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Olivia on 11/15/2016.
 */

public class AddSongListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Song> songs;

    public AddSongListAdapter(Context context, ArrayList<Song> songs) {
        this.context = context;
        this.songs = songs;
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

        final int votes = songs.get(position).getCount();
        holder.song.setTextColor(Color.BLACK);
        holder.artist.setTextColor(Color.BLACK);
        holder.song.setText(songs.get(position).getTitle());
        holder.artist.setText(songs.get(position).getArtist());
        JSONArray song = songs.get(position).getArt();
        try {
            holder.album.setImageBitmap(getImageBitmap(song.getJSONObject(1).getString("url")));
        } catch(JSONException e) {
            Log.d("addsonglistadapter", "wtf man");
        }

        row.findViewById(R.id.album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "Adding song to feed!", Toast.LENGTH_LONG).show();
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
            Log.e("yo", "Error getting bitmap", e);
        }
        return bm;
    }

}