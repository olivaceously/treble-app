package com.treble.www.treble;

/**
 * Created by Olivia on 10/16/2016.
 * For CustomListAdapter
 */


//import android.*;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

@SuppressWarnings("ALL")
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
        TextView song;
        TextView artist;
        TextView votes;
        ImageView album;
        ImageButton upbtn;
        ImageButton downbtn;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final Holder holder = new Holder();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //noinspection RedundantCast
            row = (View)inflater.inflate(R.layout.song_list, null);
        }
        else {
            //noinspection RedundantCast
            row = (View)convertView;
        }
        holder.song =(TextView) row.findViewById(R.id.song);
        holder.artist =(TextView) row.findViewById(R.id.artist);
        holder.album =(ImageView) row.findViewById(R.id.album);
        holder.votes = (TextView) row.findViewById(R.id.votes);
        holder.upbtn = (ImageButton)row.findViewById(R.id.upvote_button);
        holder.downbtn = (ImageButton)row.findViewById(R.id.downvote_button);

        final int votes = songs.get(position).getCount();
        holder.song.setTextColor(Color.BLACK);
        holder.artist.setTextColor(Color.BLACK);
        holder.votes.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        holder.votes.setText(String.valueOf(songs.get(position).getCount()));
        holder.song.setText(songs.get(position).getTitle());
        holder.artist.setText(songs.get(position).getArtist());
        JSONArray song = songs.get(position).getArt();
        try {
            holder.album.setImageBitmap(getImageBitmap(song.getJSONObject(1).getString("url")));
        } catch(JSONException e) {
            Log.d("okay", "wtf man");
        }

        holder.upbtn.setOnClickListener(new View.OnClickListener(){
            boolean clicked = false;
            InputStream is = null;
            @Override
            public void onClick(View v) {
                if (!clicked) {
                    clicked = true;

                    try {
                        URL api = new URL(MainActivity.UPVOTE_API_URL + "?id=" + songs.get(position).getId());
                        HttpURLConnection conn = (HttpURLConnection) api.openConnection();

                        conn.setRequestMethod("GET");
                        conn.setDoInput(true);

                        conn.connect();
                        @SuppressWarnings("UnusedAssignment") int response = conn.getResponseCode();
                        if (response != 200) {
                            Toast.makeText(context, "Something went wrong, try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (MalformedURLException e) {
                        Log.e("GetFeed doInBG(): ", e.toString());
                    }
                    catch (IOException e) {
                        Log.e("GetFeed doInBG(): ", e.toString());
                    }
                    songs.get(position).setCount(votes+1);
                    holder.votes.setText(String.valueOf(songs.get(position).getCount()));
                }
                else {
                    Toast.makeText(context, "Already Voted!", Toast.LENGTH_LONG).show();
                }
            }
        });

        holder.downbtn.setOnClickListener(new View.OnClickListener(){
            boolean clicked = false;
            @Override
            public void onClick(View v) {
                if (!clicked) {
                    Toast.makeText(context, "Downvoted!", Toast.LENGTH_LONG).show();
                    clicked = true;
                    songs.get(position).setCount(votes-1);
                    holder.votes.setText(String.valueOf(songs.get(position).getCount()));
                }
                else {
                    Toast.makeText(context, "Already Voted!", Toast.LENGTH_LONG).show();
                }
            }
        });


        row.findViewById(R.id.album).setOnClickListener(new View.OnClickListener() {
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