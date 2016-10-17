package com.treble.www.treble;

/**
 * Created by Olivia on 10/16/2016.
 */

import java.util.ArrayList;
import android.*;
import android.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Song> songs;

    public CustomListAdapter(Context context, ArrayList<Song> songs) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = (View)inflater.inflate(R.layout.simple_list_item_2, null);
        }
        else {
            row = (View)convertView;
        }
        TextView v = (TextView)row.findViewById(android.R.id.text1);
        v.setTextColor(Color.BLACK);
        v.setText(songs.get(position).getTitle());
        v = (TextView) row.findViewById(android.R.id.text2);
        v.setTextColor(Color.BLACK);
        v.setText("by " + songs.get(position).getArtist() + " on " + songs.get(position).getAlbum());
        return row;
    }
}