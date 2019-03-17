package com.example.dogwalk;

import android.graphics.Bitmap;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

/**
 * Main activity class which just sets up the tab view - everything else is handled using fragments.
 * (A choice made due to the Google Map view being a fragment.)
 *
 * Class also incorporates mapInterface, which transfers a map screenshot an information from the MapFragment
 * to the previousRoutes fragment. Fragments cannot natively communicate with eachother, and an interface
 * has to be used in the connecting class.
 *
 *
 * NOTE: IF APP CRASHES DURING MARKING, THE GOOGLE MAPS API KEY WILL NEED TO BE REFRESHED. THIS NEEDS TO BE CHANGED IN
 * R.values.google_maps_api AND R.values.strings. IF THERE ARE ANY ISSUES EMAIL ME AT davidmccluskey1@gmail.com
 **/

public class MainActivity extends AppCompatActivity implements mapInterface
{
    TabsAdapter tabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sets the view to the main activity layout (which holds the tabs and a viewpager)
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        //adds tabs to the tab layout(sequentially according to the TabsAdapter class.
        //Sets the icons for the tabs.
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.generate_map_icon));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.previous_walks_icon));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.dog_info_icon));


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager =findViewById(R.id.view_pager);
        tabsAdapter = new TabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            //these are required constructor methods, but they don't need to function.
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    //My sendSnapshot method is defined in the mapInterface interface, and takes a Bitmap screenshot and information
    //on the screenshot as arguments, and passes them to the previousRoutes fragment.
    @Override
    public void sendSnapshot(Bitmap snapshot, String info)
    {
        tabsAdapter.previousRoutes.add(snapshot, info);
    }
}