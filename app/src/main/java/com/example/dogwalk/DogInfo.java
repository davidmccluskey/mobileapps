package com.example.dogwalk;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * This fragment holds information on various breeds of dogs. The images and descriptions are hardcoded, and
 * and the data is presented using a RecyclerView.
 * The images are imported from Wikipedia, and parsed using Glide. The descriptions were from Google.
 */
public class DogInfo extends Fragment 
{
    //Setting up variables;
    private static final String TAG = "DogInfo";

    //RecyclerView data is held in these Arraylists
    public ArrayList<String> mNames = new ArrayList<>();
    public  ArrayList<String> mImageUrls = new ArrayList<>();
    public ArrayList<String> mDogInfo = new ArrayList<>();

    //RecyclerView, adapter and layout manager defined.
    public RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        init(); //Adds the data to the lists before the RecyclerView is initialised.
        View mView = inflater.inflate(R.layout.doginfo_layout, container, false); //Inflates layout.
        mRecyclerView = mView.findViewById(R.id.recyclerview); //Assigns RecyclerView to the one in the fragment.
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL)); //Adds a line between each reyclerView object, for aesthetic reasons.
        mAdapter = new DogInfoAdapter(mNames, mImageUrls, mDogInfo, mView.getContext()); //Creates new instance of the RecyclerView adapter class I created.
        mLayoutManager = new LinearLayoutManager(getContext()); //Defines layout manager.
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return mView;
    }
    
    private void init()
    {
        //This class defines all the images, names, and information for each of the instances in the recyclerView.
        //Each instance HAS to have an image, data and name otherwise the data would shift up or down (as it assigns
        //each array index individually.)
        //Glide uses direct image URLS, hence the upload.wikimedia.org, as this is a direct link to the image.

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