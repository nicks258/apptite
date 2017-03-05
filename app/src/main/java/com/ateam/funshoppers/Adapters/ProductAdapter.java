package com.ateam.funshoppers.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ateam.funshoppers.R;
import com.ateam.funshoppers.model.BusinessProducts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 1/22/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    private Context mContext;
    private List<BusinessProducts> albumList;
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


    public ProductAdapter(Context mContext, List<BusinessProducts> albumList) {
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
        BusinessProducts album = albumList.get(position);
        holder.title.setText(album.getName());

        // loading album cover using picasso library
        // Drawable d=resizeDrawable(mContext.getResources().getDrawable(R.drawable.loading));
        Picasso.with(mContext).load(album.getCoverphotourl()).placeholder(R.drawable.loading).into(holder.thumbnail);

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
