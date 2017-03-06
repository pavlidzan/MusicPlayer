package com.myapps.vladimirp.musicplayer;

import android.app.IntentService;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

/**
 * Created by vladimir.pavlovic on 2/8/2017.
 */

public class GetSongsIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public static final String BROADCAST_ACTION="com.myapps.vladimirp.musicplayer.BROADCAST";
//    public static final String EXTENDED_DATA_STATUS="com.myapps.vladimirp.musicplayer.STATUS";

    public GetSongsIntentService() {
        super("com.myapps.vladimirp.musicplayer.GetSongsIntentService");
    }
    private SongBox mSongBox;
    @Override
    protected void onHandleIntent(Intent intent) {



        mSongBox = SongBox.initiateSongBox(getApplicationContext());
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null){
            Toast.makeText(this, "Query failed", Toast.LENGTH_SHORT);
        } else if (!cursor.moveToFirst()){
            Toast.makeText(this, "No songs on your device", Toast.LENGTH_SHORT);
        } else {
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int displayName = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
            int artistName = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            do {
                long thisColumnId = cursor.getLong(idColumn);
                String thisTitle = cursor.getString(titleColumn);
                String thisDisplayName = cursor.getString(displayName);
                String thisArtistName = cursor.getString(artistName);
                Song song = new Song(thisTitle,thisArtistName,thisColumnId,thisDisplayName);
                mSongBox.addSong(song);
            } while (cursor.moveToNext());
        }
        if (mSongBox.getSongs()!=null) {
//            boolean status = true;
            Intent localIntent = new Intent(GetSongsIntentService.BROADCAST_ACTION);//.putExtra(GetSongsIntentService.EXTENDED_DATA_STATUS, status);
            sendBroadcast(localIntent);
        }
    }

    }

