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
 * Created by tutlane on 09-01-2018.
 */

public class DogInfo extends Fragment 
{
    private static final String TAG = "DogInfo";
    private ArrayList<String> mNames = new ArrayList<>();
    private  ArrayList<String> mImageUrls = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        initImageBitmaps();
        View mView = inflater.inflate(R.layout.doginfo_layout, container, false);

        RecyclerView recyclerView = mView.findViewById(R.id.recyclerview);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mNames, mImageUrls, mView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext()));

    return mView;
    }
    
    private void initImageBitmaps()
    {
        Log.d(TAG, "initImageBitmaps: Preparing Bitmaps.");

        mImageUrls.add("https://upload.wikimedia.org/wikipedia/commons/d/de/Smooth_Dachshund_red_and_tan_portrait.jpg");
        mNames.add("Dachshund");

        mImageUrls.add("https://upload.wikimedia.org/wikipedia/commons/2/26/YellowLabradorLooking_new.jpg");
        mNames.add("Labrador");

        mImageUrls.add("https://upload.wikimedia.org/wikipedia/commons/2/2c/West_Highland_White_Terrier_Krakow.jpg");
        mNames.add("West Highland Terrier");

        mImageUrls.add("https://upload.wikimedia.org/wikipedia/commons/f/fa/Buster_the_red_grizzle_border_terrier_%282006%29.jpg");
        mNames.add("Border Terrier");

        mImageUrls.add("https://upload.wikimedia.org/wikipedia/commons/6/6d/Alaskan_malamut_465.jpg");
        mNames.add("Alaskan Malamut");

        mImageUrls.add("https://upload.wikimedia.org/wikipedia/commons/6/6c/Border_Collie_rojo_mirlo_tricolor_%28Birdy_de_los_Baganicos%29.jpg");
        mNames.add("Border Collie");

//        mImageUrls.add("https://en.wikipedia.org/wiki/English_Springer_Spaniel#/media/File:%C3%89pagneul_Anglais.jpg");
//        mNames.add("Springer Spaniel");

        mImageUrls.add("https://upload.wikimedia.org/wikipedia/commons/d/de/Beagle_Upsy.jpg");
        mNames.add("Beagle");
//
//        mImageUrls.add("https://en.wikipedia.org/wiki/Shiba_Inu#/media/File:Shiba_inu_taiki.jpg");
//        mNames.add("Shiba Inu");
//
//        mImageUrls.add("https://en.wikipedia.org/wiki/Samoyed_dog#/media/File:Samojed00.jpg");
//        mNames.add("Samoyed");
    }

}