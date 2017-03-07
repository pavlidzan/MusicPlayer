package com.myapps.vladimirp.musicplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by vladimir.pavlovic on 3/2/2017.
 */

public class SongBox {
    public static final String TAG = "SongBox";
    private static SongBox mSongBox;
    private static List<Song> mSongs;
    private SongBox(Context context){
         mSongs = new Vector<>();
    }
    public static SongBox initiateSongBox(Context context){
        if (mSongBox==null){
            return mSongBox = new SongBox(context);
        } else {
            return mSongBox;
        }
    }
    public void addSong(Song song){
        mSongs.add(song);
    }

    public Song getSong(long songId){
      for (int i =1; i<=mSongs.size(); i++) {
          Song song = mSongs.get(i);
          if (songId == song.getColumnId()) {
              Log.i(TAG, "song with requested id is found!");
              return song;
          }
      }
        Log.i(TAG, "Song with requested id isn't found");
        return null;
    }

    public List<Song> getSongs(){
        return mSongs;
    }
}
