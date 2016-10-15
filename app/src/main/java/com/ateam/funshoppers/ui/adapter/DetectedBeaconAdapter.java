

package com.ateam.funshoppers.ui.adapter;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


import com.ateam.funshoppers.R;
import com.ateam.funshoppers.databinding.ItemDetectedBeaconBinding;
import com.ateam.funshoppers.model.DetectedBeacon;
import com.ateam.funshoppers.ui.fragment.BaseFragment;
import com.ateam.funshoppers.viewModel.DetectedBeaconViewModel;

import org.altbeacon.beacon.Beacon;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


public class DetectedBeaconAdapter extends BeaconAdapter<DetectedBeaconAdapter.BindingHolder> {
public static TextView textView;
//View view;
  // public static int flag=0;
   // public static ArrayList al = new ArrayList();
    public DetectedBeaconAdapter(BaseFragment fragment) {
        mFragment = fragment;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {

      //  textView = (TextView)parent.findViewById(R.id.beacon_item_uuid_value) ;
     //   textView.setText("Hello");
        ItemDetectedBeaconBinding beaconBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_detected_beacon,
                parent,
                false);

        return new BindingHolder(beaconBinding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, final int position) {
        final ItemDetectedBeaconBinding beaconBinding = holder.binding;


        beaconBinding.setViewModel(new DetectedBeaconViewModel(mFragment, (DetectedBeacon) getItem(position)));
    }


    public void insertBeacons(Collection<Beacon> beacons) {
        Iterator<Beacon> iterator = beacons.iterator();
        while (iterator.hasNext()) {
            DetectedBeacon dBeacon = new DetectedBeacon(iterator.next());

           // Log.d("suvo",dBeacon.getEddystoneURL() );
          //  Log.d("nicks", dBeacon.getEddystoneURL());
         /*   if (dBeacon.getEddystoneURL().compareTo("https://electronics")==0)
            {
                al.add(1);
            }
            if (dBeacon.getEddystoneURL().compareTo("https://sunglasses")==0)
            {
                al.add(2);
            }
*/

          dBeacon.setTimeLastSeen(System.currentTimeMillis());

            this.mBeacons.put(dBeacon.getId(), dBeacon);
        }

        notifyDataSetChanged();

    }



    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemDetectedBeaconBinding binding;

        public BindingHolder(ItemDetectedBeaconBinding binding) {
            super(binding.contentView);
            this.binding = binding;
        }

        public void setOnLongClickListener(View.OnLongClickListener listener) {
            binding.contentView.setOnLongClickListener(listener);
        }
    }
    static class myclass {
        TextView textView;
    }



}
