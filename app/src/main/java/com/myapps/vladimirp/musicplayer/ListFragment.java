package com.myapps.vladimirp.musicplayer;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.support.v4.content.LocalBroadcastManager.*;

/**
 * Created by vladimir.pavlovic on 2/8/2017.
 */

public class ListFragment extends Fragment {
    public static final String TAG ="ListFragment";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SongBox mSongBox;
    private LocalReceiver mLocalReceiver;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v =inflater.inflate(R.layout.fragment_list, container, false);
        mLocalReceiver= new LocalReceiver();
        IntentFilter intentFilter = new IntentFilter(GetSongsIntentService.BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mLocalReceiver, intentFilter);
        mSongBox = SongBox.initiateSongBox(getActivity());
        Intent intent = new Intent(getActivity(), GetSongsIntentService.class);
        getActivity().startService(intent);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.list_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(mSongBox.getSongs());
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    public class MyAdapter extends RecyclerView.Adapter<Viewholder>{
        private List<Song> mDataset;

        public MyAdapter(List<Song> myDataset){
            mDataset=myDataset;
        }
        @Override
        public Viewholder onCreateViewHolder(ViewGroup parent, int viewType){
            TextView v = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.my_text_view, parent, false);
            Viewholder vh = new Viewholder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(Viewholder holder, int position) {
            holder.mTextView.setText(mSongBox.getSong(position).toString());
        }


        @Override
        public int getItemCount() {
            return 0;
        }

    }

    public  class Viewholder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public Viewholder(TextView v){
            super(v);
            mTextView = v;
        }
    }
    public class LocalReceiver extends BroadcastReceiver {
        public LocalReceiver(){}

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "LocalReceiver.onReceive()");
            updateUI();
        }
    }
    public void updateUI(){
        Log.i(TAG, "UpdateUI");
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mLocalReceiver);
    }
}
