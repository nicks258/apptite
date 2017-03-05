package com.ateam.funshoppers.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ateam.funshoppers.R;
import com.ateam.funshoppers.Utility;
import com.ateam.funshoppers.model.BusinessReviews;

import java.util.ArrayList;

/**
 * Created by Home on 1/22/2017.
 */

public class ReviewAdapter extends BaseAdapter {
    Context mContext;
  ArrayList<BusinessReviews> reviewses;
    // private ArrayList<String> name;

    public ReviewAdapter(Context mContext, ArrayList<BusinessReviews> reviewses) {
        this.mContext = mContext;
        this.reviewses=reviewses;

    }

    @Override
    public int getCount() {
        return reviewses.size();

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
        String ago;

        if(convertView==null){
            LayoutInflater inflater=((Activity)mContext).getLayoutInflater();
            convertView=inflater.inflate(R.layout.activity_review_row,parent,false);
            holder=new Viewholder();
            holder.reviewer=(TextView) convertView.findViewById(R.id.reviewer);
            holder.body=(TextView)convertView.findViewById(R.id.body);
            holder.rate=(TextView)convertView.findViewById(R.id.rate);
            holder.longago=(TextView)convertView.findViewById(R.id.longago);
            convertView.setTag(holder);
        }
        else{
            holder=(Viewholder)convertView.getTag();

        }
        // Drawable d=resizeDrawable(mContext.getResources().getDrawable(R.drawable.loading));


        long res= Utility.convertToTimestamp(reviewses.get(position).getCreatedOn());
        ago= Utility.getTimeAgo(res);
        holder.reviewer.setText(reviewses.get(position).getCreatedBy().getFullName());
        holder.body.setText(reviewses.get(position).getBody());
        holder.rate.setText(reviewses.get(position).getRating()+"/5");
        holder.longago.setText(ago);


        return convertView;
    }



    static class Viewholder{
        // private TextView nameTextView;
        private TextView reviewer;
        private TextView body;
        private TextView rate;
        private TextView longago;


    }
}
