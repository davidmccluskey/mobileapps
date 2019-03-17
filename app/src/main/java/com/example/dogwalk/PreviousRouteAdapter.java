package com.example.dogwalk;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * A standard RecyclerView adapter class, which is used by the PreviousRoutes fragment.
 */
public class PreviousRouteAdapter extends RecyclerView.Adapter<PreviousRouteAdapter.ViewHolder>
{
    private static final String TAG = "PreviousRouteAdapter";

    //Defines arraylists for snapshots and snapshot information.
    private ArrayList<Bitmap> mMapSnapshots;
    private ArrayList<String> mInfo;
    private Context mContext;

    @NonNull
    @Override
    //Sets up the layout for the individual recyclerview items.
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_previouswalks, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //Constructor which is passed the arraylists that are defined in the PreviousRoute fragment class, and are
    //assigned to the arraylists in this class which are accessed by other methods.
    public PreviousRouteAdapter(ArrayList<Bitmap> mMapSnapshots, ArrayList<String> mInfo, Context mContext)
    {
        this.mMapSnapshots = mMapSnapshots;
        this.mInfo = mInfo;
        this.mContext = mContext;
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Log.d(TAG, "onBindViewHolder: Added");

        holder.mapSnapshot.setImageBitmap(mMapSnapshots.get(position));
        holder.mapInfo.setText(mInfo.get(position));
    }

    @Override
    //Returns the size of the arraylist, so the viewholder knows how many recyclerview items to populate.
    //I used mSnapshots, but any of my arraylists could've been used here. They all need to be the same size.
    public int getItemCount()
    {
        return mMapSnapshots.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView mapSnapshot;
        TextView mapInfo;
        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mapSnapshot = itemView.findViewById(R.id.mapImage);
            mapInfo = itemView.findViewById(R.id.routeInfo);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

}