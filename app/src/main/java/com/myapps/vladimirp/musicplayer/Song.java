package com.myapps.vladimirp.musicplayer;

/**
 * Created by vladimir.pavlovic on 3/2/2017.
 */

public class Song {
    private String mTitle;
    private String mArtist;
    private long mColumnId;
    private String mDisplayName;


    public Song(String title, String artist, long columnId, String displayName){
        mTitle = title;
        mArtist = artist;
        mColumnId = columnId;
        mDisplayName = displayName;
    }
    public String getTitle(){
        return mTitle;
    }

    public String getArtist(){
        return mArtist;
    }

    public long getColumnId(){
        return mColumnId;
    }

    public String getDisplayName(){
        return mDisplayName;
    }

    public String toString(){
        return mDisplayName;
    }
}
