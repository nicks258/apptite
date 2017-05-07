package com.ateam.funshoppers.BeaconDistance;

/**
 * Created by sumitm on 07-Apr-17.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.ateam.funshoppers.R;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Distance extends ArrayAdapter<String> {
    public View v;
    Context mContext;
    private static final Pattern urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    List<String> beaconList = new ArrayList<>();
    public Distance(Context context, int item_layout, List<String> list) {
        super(context, item_layout,list);
        this.mContext = context;
        this.beaconList = list;
    }
    @Override
    public int getCount() {
        return super.getCount();
    }
    public View getView(int pos, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.item_layout, null);
        TextView tv = (TextView) v.findViewById(R.id.Itemname);
        tv.setText(getItem(pos));
        final int Pos = pos;
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int matchStart=0,matchEnd=0;
                String Intenturl="";
                String data=getItem(Pos);
                Log.i("POS" + getItem(Pos),"LION");
                Logger.i("Position->>Data"+data);
                String Rese ="http://" + data.substring(27) ;
                Log.i("Nick",Rese.substring(7,Rese.length()-4));
//                Matcher matcher = urlPattern.matcher(Rese);
//                while (matcher.find()) {
//                    matchStart = matcher.start(1);
//                    matchEnd = matcher.end();
//                    com.orhanobut.logger.Logger.i("Position"+ matchStart + " "  +matchEnd);
//                    Intenturl = data.substring(matchStart,matchEnd);
//                    // now you have the offsets of a URL match
//                }
                Intenturl = Rese.substring(7,Rese.length()-5);
                Log.i("Nick->>",Intenturl);
                Intent intent = new Intent(getContext(), WebviewActivity.class);

//                Log.i("opo",Intenturl.substring(7,Intenturl.lastIndexOf(".")));
                intent.putExtra("url",Intenturl);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        });
        if (pos == 0) {
            //TextView tv = (TextView)v.findViewById(R.id.list_content);
            //tv.setText(str[pos]);
            tv.setTextColor(Color.BLACK);
            tv.setTypeface(null, Typeface.BOLD);

        } else {
            //TextView tv = (TextView)v.findViewById(R.id.list_content);
            //tv.setText(str[pos]);
//            tv.setTextColor(Color.RED);
        }

        return v;
    }
}