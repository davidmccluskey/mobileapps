package com.example.dogwalk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;


public class PreviousWalks extends Fragment
{
    private static final String TAG = "PreviousWalks";

    private ArrayList<String> mInfo = new ArrayList<>();
    private ArrayList<Bitmap> mMapSnapshots = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;


    //Method isn't working as intended, I had issues communicating between fragments (Couldn't pass map data
    //from generation screen to previous routes recycler, causing crashes.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.previouswalks_layout, container, false);

        RecyclerView recyclerView = mView.findViewById(R.id.routesRecycler);
        PreviousRouteAdapter adapter = new PreviousRouteAdapter(mMapSnapshots, mInfo, mView.getContext());
        mAdapter = adapter;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext()));
        return mView;
    }
    public void add(Bitmap snapshot, String info)
    {
        mInfo.add(info);
        mMapSnapshots.add(snapshot);
        mAdapter.notifyDataSetChanged();
    }

}