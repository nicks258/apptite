package com.ateam.funshoppers.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ateam.funshoppers.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Home on 1/22/2017.
 */

public class ImageAdapter extends BaseAdapter {
    Context mContext;
    private ArrayList<String> image;
    private int width;
    // private ArrayList<String> name;

    public ImageAdapter(Context mContext, ArrayList<String> image, int width) {
        this.mContext = mContext;
        this.image = image;
        this.width = width;
        //this.name = name;

    }

    @Override
    public int getCount() {
        return image.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Viewholder holder;

        if(convertView==null){
            LayoutInflater inflater=((Activity)mContext).getLayoutInflater();
            convertView=inflater.inflate(R.layout.photo_grid_row,parent,false);
            holder=new Viewholder();
            //  holder.nameTextView=(TextView)convertView.findViewById(R.id.firstline);
            //  holder.characterTextView=(TextView)convertView.findViewById(R.id.secondLine);
            holder.characterImage=(ImageView)convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        }
        else{
            holder=(Viewholder)convertView.getTag();

        }
        Drawable d=resizeDrawable(mContext.getResources().getDrawable(R.drawable.loading));
        Picasso.with(mContext).load(image.get(position)).resize(width,width).placeholder(d)
                .into(holder.characterImage);
        //   holder.nameTextView.setText(name.get(position));
        //  holder.characterTextView.setText(character.get(position));


        return convertView;
    }
    private Drawable resizeDrawable(Drawable image){
        Bitmap b=((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized= Bitmap.createScaledBitmap(b,width,width,false);
        return new BitmapDrawable(mContext.getResources(),bitmapResized);
    }


    static class Viewholder{
        // private TextView nameTextView;
        private ImageView characterImage;


    }
}

