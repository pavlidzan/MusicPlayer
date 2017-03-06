package com.myapps.vladimirp.musicplayer;

import android.content.ContentResolver;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vladimir.pavlovic on 3/2/2017.
 */

public class SongBox {
    private static SongBox mSongBox;
    List<Song> mSongs;
//    private int mId=0;
    private SongBox(Context context){
         mSongs = new ArrayList<>();
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
//        ++mId;
    }

    public Song getSong(long songId){
      for (int i =1; i<=mSongs.size(); i++) {
          Song song = mSongs.get(i);
          if (songId == song.getColumnId()) {
              return song;
          }
      }
        return null;
    }

    public List<Song> getSongs(){
        return mSongs;
    }
}
