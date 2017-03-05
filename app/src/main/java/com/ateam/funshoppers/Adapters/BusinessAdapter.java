package com.ateam.funshoppers.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ateam.funshoppers.R;
import com.ateam.funshoppers.model.Businesses;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Home on 1/21/2017.
 */

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.MyViewHolder>{
    private Context mContext;
    private List<Businesses> albumList;
    private int width;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public BusinessAdapter(Context mContext, List<Businesses> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Businesses album = albumList.get(position);
        holder.title.setText(album.getName());

        // loading album cover using picasso library
       // Drawable d=resizeDrawable(mContext.getResources().getDrawable(R.drawable.loading));
        Picasso.with(mContext).load(album.getCoverPhotoUrl()).placeholder(R.drawable.loading).into(holder.thumbnail);

    }
   /* private Drawable resizeDrawable(Drawable image){
        Bitmap b=((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized=Bitmap.createScaledBitmap(b,width,width,false);
        return new BitmapDrawable(mContext.getResources(),bitmapResized);
    }*/


    @Override
    public int getItemCount() {
        return albumList.size();
    }


}
