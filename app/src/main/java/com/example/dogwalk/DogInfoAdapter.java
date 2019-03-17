package com.example.dogwalk;

import android.content.Context;
import android.support.annotation.NonNull;
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
    /**
     * A standard RecyclerView adapter class, which is used by the dog info fragment.
     */
    private static final String TAG = "DogInfoAdapter";

    //Defines arraylists for each of the values used in the recyclerview.
    private ArrayList<String> mDogType;
    private ArrayList<String> mImages;
    private ArrayList<String> mDogInfo;
    private Context mContext;


    //Constructor which is passed the arraylists that are defined in the dog info fragment class, and are
    //assigned to the arraylists in this class which are accessed by other methods.
    public DogInfoAdapter(ArrayList<String> mDogType, ArrayList<String> mImages,
                          ArrayList<String> mDogInfo, Context mContext)
    {
        this.mDogType = mDogType;
        this.mImages = mImages;
        this.mDogInfo = mDogInfo;
        this.mContext = mContext;
    }

    //Sets up the layout for the individual recyclerview items.
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

        //Glide takes the context, builds a bitmap, and loads the image from the arraylist into the bitmap at
        //whichever postition called the onBindHolder. It's then places into the recyclerview item.
        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.imageView);

        holder.dogType.setText(mDogType.get(position)); //Sets the text to the value in the arraylist at the postition that called the viewholder.
        holder.dogInfo.setText(mDogInfo.get(position));
    }

    //Returns the size of the arraylist, so the viewholder knows how many recyclerview items to populate.
    //I used mDogType, but any of my arraylists could've been used here. They all need to be the same size.
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
        //Defines the parent layout as well as the layout for the individual recyclerview item.

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
