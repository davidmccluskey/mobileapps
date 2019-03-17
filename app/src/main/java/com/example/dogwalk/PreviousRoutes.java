package com.example.dogwalk;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;


public class PreviousRoutes extends Fragment
{
    private ArrayList<String> mInfo = new ArrayList<>();
    private ArrayList<Bitmap> mMapSnapshots = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private View mView;
    private TextView noRoutesText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.previouswalks_layout, container, false);

        RecyclerView recyclerView = mView.findViewById(R.id.routesRecycler);
        PreviousRouteAdapter adapter = new PreviousRouteAdapter(mMapSnapshots, mInfo, mView.getContext());
        mAdapter = adapter;
        noRoutesText = mView.findViewById(R.id.noviewtext);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext()));
        return mView;
    }

    public void add(Bitmap snapshot, String info)
    {
        mInfo.add(info);
        mMapSnapshots.add(snapshot);
        noRoutesText.setText(" ");
        mAdapter.notifyDataSetChanged();
    }

}