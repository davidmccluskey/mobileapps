package com.example.dogwalk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class DogInfoAdapter extends RecyclerView.Adapter<DogInfoAdapter.ViewHolder>
{
    private static final String TAG = "DogInfoAdapter";

    private ArrayList<String> mDogType = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mDogInfo = new ArrayList<>();
    private Context mContext;

    public DogInfoAdapter(ArrayList<String> mDogType, ArrayList<String> mImages,
                          ArrayList<String> mDogInfo, Context mContext)
    {
        this.mDogType = mDogType;
        this.mImages = mImages;
        this.mDogInfo = mDogInfo;
        this.mContext = mContext;
    }

    public DogInfoAdapter()
    {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem_doginfo, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.imageView);

        holder.dogType.setText(mDogType.get(position));
        holder.dogInfo.setText(mDogInfo.get(position));
    }

    @Override
    public int getItemCount() {
        if(mDogType!=null){
            return mDogType.size();
        } else{
            return 0;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {

        ImageView imageView;
        TextView dogType;
        TextView dogInfo;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            dogType = itemView.findViewById(R.id.dogType);
            dogInfo = itemView.findViewById(R.id.dogInfo);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }



}
