package com.example.dogwalk;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabsAdapter extends FragmentStatePagerAdapter
{
    int mNumOfTabs;
    public PreviousWalks previousWalks;
    public MapFragment mapFragment;
    public DogInfo dogInfo;

    public TabsAdapter(FragmentManager fm, int NoofTabs){
        super(fm);
        this.mNumOfTabs = NoofTabs;
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                previousWalks = new PreviousWalks();
                return previousWalks;
            case 1:
                mapFragment = new MapFragment();
                return mapFragment;
            case 2:
                dogInfo = new DogInfo();
                return dogInfo;
            default:
                return null;
        }
    }
}