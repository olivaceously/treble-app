package com.treble.www.treble;

/**
 * Created by Olivia on 10/16/2016.
 */


//import android.*;
import android.R.layout;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

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

    public class Holder
    {
        TextView tv1;
        TextView tv2;
        ImageView img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        Holder holder = new Holder();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = (View)inflater.inflate(R.layout.song_list, null);
        }
        else {
            row = (View)convertView;
        }
        holder.tv1=(TextView) row.findViewById(R.id.textView1);
        holder.img=(ImageView) row.findViewById(R.id.imageView1);
        holder.tv1.setTextColor(Color.BLACK);
        holder.tv1.setText(songs.get(position).getTitle());
        holder.tv2=(TextView) row.findViewById(R.id.textView2);
        holder.tv2.setTextColor(Color.BLACK);
        holder.tv2.setText(songs.get(position).getArtist());
        JSONArray song = songs.get(position).getArt();
        try {
            holder.img.setImageBitmap(getImageBitmap(song.getJSONObject(1).getString("url")));
        } catch(JSONException e) {
            Log.d("okay", "wtf man");
        }

        Button upVoteBtn = (Button)row.findViewById(R.id.upvote_button);
        Button downVoteBtn = (Button)row.findViewById(R.id.downvote_button);

        upVoteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Upvoted!", Toast.LENGTH_LONG).show();
//                //do something
//                list.remove(position); //or some other task
//                notifyDataSetChanged();
            }
        });
        downVoteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Downvoted!", Toast.LENGTH_LONG).show();
                //do something
                //notifyDataSetChanged();
            }
        });


        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "When you click on me, I will play the song!", Toast.LENGTH_LONG).show();
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