package com.example.dogwalk;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class DogInfo extends Fragment 
{
    private static final String TAG = "DogInfo";
    public ArrayList<String> mNames = new ArrayList<>();
    public  ArrayList<String> mImageUrls = new ArrayList<>();
    public ArrayList<String> mDogInfo = new ArrayList<>();
    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;

    public static DogInfo newInstance() {
        DogInfo fragment = new DogInfo();
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        initImageBitmaps();
        View mView = inflater.inflate(R.layout.doginfo_layout, container, false);
        mRecyclerView = mView.findViewById(R.id.recyclerview);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        mAdapter = new DogInfoAdapter(mNames, mImageUrls, mDogInfo, mView.getContext());
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return mView;
    }
    
    private void initImageBitmaps()
    {
        Log.d(TAG, "initImageBitmaps: Preparing Bitmaps.");

        mImageUrls.add("https://upload.wikimedia.org/wikipedia/commons/d/de/Smooth_Dachshund_red_and_tan_portrait.jpg");
        mNames.add("Dachshund");
        mDogInfo.add("You should be giving your dog a 45-60 minute walk a day (maybe split in two lots). Once adult, your Dachshund will take any amount of exercise you care to give. ");


        mImageUrls.add("https://upload.wikimedia.org/wikipedia/commons/2/26/YellowLabradorLooking_new.jpg");
        mNames.add("Labrador");
        mDogInfo.add("A normal, healthy adult Labrador Retriever will need 1 hour of exercise every day. The more relaxed Labs just 45 minutes per day, the more energetic 1.5 hours+.");

        mImageUrls.add("https://upload.wikimedia.org/wikipedia/commons/2/2c/West_Highland_White_Terrier_Krakow.jpg");
        mNames.add("West Highland Terrier");
        mDogInfo.add("The length of the walk is only limited by your time and energy as a West Highland Terrier can go all day long. Ideally, the minimum should be 30 to 60 minutes daily. This can be just once a day, but ideally split over two walks a day");

        mImageUrls.add("https://upload.wikimedia.org/wikipedia/commons/f/fa/Buster_the_red_grizzle_border_terrier_%282006%29.jpg");
        mNames.add("Border Terrier");
        mDogInfo.add("The length of the walk is only limited by your time and energy as a Border Terrier can go all day long. Ideally, the minimum should be 45 to 60 minutes daily. This can be just once a day, but two walks a day would be better.");


        mImageUrls.add("https://upload.wikimedia.org/wikipedia/commons/6/6d/Alaskan_malamut_465.jpg");
        mNames.add("Alaskan Malamut");
        mDogInfo.add("A couple of daily walks (one is at least 45 minutes) or a shorter walk and a vigorous play session (30 minutes or more). Malamutes can be stubborn, but they are also eager to please. Training early on will make play and exercise and life in general easier.");


        mImageUrls.add("https://upload.wikimedia.org/wikipedia/commons/6/6c/Border_Collie_rojo_mirlo_tricolor_%28Birdy_de_los_Baganicos%29.jpg");
        mNames.add("Border Collie");
        mDogInfo.add("Although as much as they possibly can should be the right answer, but at least 20-30 minutes of running everyday is ideal. However, undoubtedly your Border Collie would definitely prefer a lot more.");


        mImageUrls.add("https://upload.wikimedia.org/wikipedia/commons/d/de/Beagle_Upsy.jpg");
        mNames.add("Beagle");
        mDogInfo.add("You should walk your Beagle for a minimum of 20 minutes, ideally 30, and some Beagles may need up to 40 minutes.");

    }


}