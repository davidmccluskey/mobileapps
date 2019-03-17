package com.example.dogwalk;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Tabs adapter class which is set up by main activity - and returns the position of each
 * of the tabs which are stored in this class.
 */
public class TabsAdapter extends FragmentStatePagerAdapter
{
    int mNumOfTabs;
    //sets up instances of all of my tabs
    public PreviousRoutes previousRoutes;
    public MapFragment mapFragment;
    public DogInfo dogInfo;

    public TabsAdapter(FragmentManager fm, int NoofTabs){
        super(fm);
        this.mNumOfTabs = NoofTabs;
    }
    @Override
    public int getCount() {
        return mNumOfTabs; //get count returns number of tabs to main activity, to determine how many tabs to render.
    }
    @Override
    public Fragment getItem(int position)
    {
        switch (position){
            case 0:
                mapFragment = new MapFragment();
                return mapFragment;
            case 1:
                previousRoutes = new PreviousRoutes();
                return previousRoutes;
            case 2:
                dogInfo = new DogInfo();
                return dogInfo;
            default:
                return null;
        }
    }
}