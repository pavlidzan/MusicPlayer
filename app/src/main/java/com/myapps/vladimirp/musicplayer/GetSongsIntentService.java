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
import android.util.Log;
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
    public static final String TAG = "GetSongsIntentService";
    public static final String BROADCAST_ACTION="com.myapps.vladimirp.musicplayer.BROADCAST";
//    public static final String EXTENDED_DATA_STATUS="com.myapps.vladimirp.musicplayer.STATUS";

    public GetSongsIntentService() {
        super("com.myapps.vladimirp.musicplayer.GetSongsIntentService");
    }

    private SongBox mSongBox;

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i(TAG, "Service started");

        mSongBox = SongBox.initiateSongBox(getApplicationContext());
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        Log.i(TAG, "pre cursor");
        if (cursor == null){
            Log.i(TAG, "Query failed");
        } else if (!cursor.moveToFirst()){
            Log.i(TAG, "No songs on your device");
        } else {
            Log.i(TAG, "There is some songs");
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int displayName = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
            int artistName = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            //dodati ciscenje mSongBox-a da se ne bi multiplicirao sadrzaj u recycler view
            mSongBox.emptyBox();
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
            Log.i(TAG, "mSongBox.getSongs()");
            Intent localIntent = new Intent(GetSongsIntentService.BROADCAST_ACTION);
            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
        }
        cursor.close();
        Log.i(TAG, "Service finish handling intent");
    }

    }

