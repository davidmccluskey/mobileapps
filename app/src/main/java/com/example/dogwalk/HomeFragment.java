package com.example.dogwalk;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    private GoogleMap mMap;
    private static Button slideButton, b1, b2, b3;
    private static TextView textView;
    private static SlidingDrawer slidingDrawer;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.homelayout, null, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        slideButton = mView.findViewById(R.id.slideButton);
        slidingDrawer = mView.findViewById(R.id.SlidingDrawer);
        b1 = mView.findViewById(R.id.Button01);
        b2 = mView.findViewById(R.id.Button02);
        b3 = mView.findViewById(R.id.Button03);
        textView = mView.findViewById(R.id.text);

        final View parent = (View) slideButton.getParent();  // button: the view you want to enlarge hit area
        parent.post(new Runnable() {
            public void run() {
                final Rect rect = new Rect();
                slideButton.getHitRect(rect);
                rect.top -= 200;    // increase top hit area
                rect.bottom += 200; // increase bottom hit area
                parent.setTouchDelegate(new TouchDelegate(rect, slideButton));
            }
        });


        setListeners();

        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                final View parent = (View) slideButton.getParent();  // button: the view you want to enlarge hit area
                parent.post(new Runnable() {
                    public void run() {
                        final Rect rect = new Rect();
                        slideButton.getHitRect(rect);
                        rect.top -= 300;    // increase top hit area
                        rect.left -= 100;   // increase left hit area
                        rect.bottom += 300; // increase bottom hit area
                        rect.right += 100;  // increase right hit area
                        parent.setTouchDelegate(new TouchDelegate(rect, slideButton));
                    }
                });
                // Change button text when slider is open
                slideButton.setText("Close");
            }
        });

        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                final View parent = (View) slideButton.getParent();  // button: the view you want to enlarge hit area
                parent.post(new Runnable() {
                    public void run() {
                        final Rect rect = new Rect();
                        slideButton.getHitRect(rect);
                        rect.top -= 300;    // increase top hit area
                        rect.left -= 100;   // increase left hit area
                        rect.bottom += 300; // increase bottom hit area
                        rect.right += 100;  // increase right hit area
                        parent.setTouchDelegate(new TouchDelegate(rect, slideButton));
                    }
                });
                // Change button text when slider is close
                slideButton.setText("Open");
            }
        });
        return mView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onClick(View v) {

        // Toast shown on sliding drawer items click
        if (v.getId() == R.id.text) {
            Toast.makeText(getActivity(), textView.getText() + " Clicked",
                    Toast.LENGTH_SHORT).show();
        } else {
            Button b = (Button) v;
            Toast.makeText(getActivity(), b.getText() + " Clicked",
                    Toast.LENGTH_SHORT).show();
        }
    }

    void padDrawer()
    {
        final View parent = (View) slideButton.getParent();  // button: the view you want to enlarge hit area
        parent.post(new Runnable() {
            public void run() {
                final Rect rect = new Rect();
                slideButton.getHitRect(rect);
                rect.top -= 300;    // increase top hit area
                rect.left -= 100;   // increase left hit area
                rect.bottom += 300; // increase bottom hit area
                rect.right += 100;  // increase right hit area
                parent.setTouchDelegate(new TouchDelegate(rect, slideButton));
            }
        });
    }

    void setListeners() {
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        textView.setOnClickListener(this);
    }

}