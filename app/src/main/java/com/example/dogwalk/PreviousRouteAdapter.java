

package com.example.dogwalk;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class PreviousRouteAdapter extends RecyclerView.Adapter<PreviousRouteAdapter.ViewHolder>
{
    private static final String TAG = "PreviousRouteAdapter";


    private ArrayList<Bitmap> mMapSnapshots = new ArrayList<>();
    private ArrayList<String> mInfo = new ArrayList<>();
    private Context mContext;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_previouswalks, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

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
    public int getItemCount() {
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