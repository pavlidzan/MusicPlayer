package com.myapps.vladimirp.musicplayer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by vladimir.pavlovic on 2/8/2017.
 */

public class ListFragment extends Fragment {
    public static final String TAG = "ListFragment";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SongBox mSongBox;
    private LocalReceiver mLocalReceiver;
    private Callbacks mCallbacks;

    /**
     *Required interface for hosting activities
     */
    public interface Callbacks{
        void onSongSelected(Song song);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        mLocalReceiver = new LocalReceiver();
        IntentFilter intentFilter = new IntentFilter(GetSongsIntentService.BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mLocalReceiver, intentFilter);
        mSongBox = SongBox.initiateSongBox(getActivity());
        Intent intent = new Intent(getActivity(), GetSongsIntentService.class);
        getActivity().startService(intent);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.list_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
//        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(mSongBox.getSongs());
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    public class MyAdapter extends RecyclerView.Adapter<Viewholder> {
        private List<Song> mDataset;

        public MyAdapter(List<Song> myDataset) {
            mDataset = myDataset;
        }

        @Override
        public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.my_text_view, parent, false);
            Viewholder vh = new Viewholder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(Viewholder holder, int position) {
            Song song = mDataset.get(position);
            holder.bindSong(song);
        }


        @Override
        public int getItemCount() {
            return mDataset.size();
        }

    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mDisplayName;
        private Song mSong;

        public Viewholder(View v) {
            super(v);
            v.setOnClickListener(this);
            mDisplayName = (TextView) v.findViewById(R.id.view_id);
        }
        public void bindSong (Song song){
            mSong = song;
            mDisplayName.setText(mSong.getDisplayName());
        }

        @Override
        public void onClick(View v) {
            mCallbacks.onSongSelected(mSong);
        }
    }

    public class LocalReceiver extends BroadcastReceiver {
        public LocalReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "LocalReceiver.onReceive()");
            updateUI();
        }
    }

    public void updateUI() {
        Log.i(TAG, "UpdateUI");
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mLocalReceiver);
    }
}
