/*
 *
 *  Copyright (c) 2015 SameBits UG. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ateam.funshoppers.ui.adapter;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ateam.funshoppers.R;
import com.ateam.funshoppers.databinding.ItemDetectedBeaconBinding;
import com.ateam.funshoppers.model.DetectedBeacon;
import com.ateam.funshoppers.ui.fragment.BaseFragment;
import com.ateam.funshoppers.viewModel.DetectedBeaconViewModel;

import org.altbeacon.beacon.Beacon;

import java.util.Collection;
import java.util.Iterator;


public class DetectedBeaconAdapter extends BeaconAdapter<DetectedBeaconAdapter.BindingHolder> {
Beacon beacon;

    int flag=0;
    public DetectedBeaconAdapter(BaseFragment fragment) {
        mFragment = fragment;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        holder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onBeaconLongClickListener != null) {
                    onBeaconLongClickListener.onBeaconLongClick(position);

                    {

                        AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());

                        alertbox.setTitle("Warning");


                        //detectedBeacon.getEddystoneURL();
                        alertbox.setIcon(R.drawable.ic_action_tasker);




                        alertbox.setNeutralButton("OK",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {

                                    }
                                });

                        alertbox.show();
                    }

                }
                return false;
            }
        });
        beaconBinding.setViewModel(new DetectedBeaconViewModel(mFragment, (DetectedBeacon) getItem(position)));
    }


    public void insertBeacons(Collection<Beacon> beacons) {
        Iterator<Beacon> iterator = beacons.iterator();
        while (iterator.hasNext()) {
            DetectedBeacon dBeacon = new DetectedBeacon(iterator.next());

            Log.d("nicks", dBeacon.getEddystoneURL());
            if (dBeacon.getEddystoneURL().compareTo("https://sahil.com")==0) {
                flag=1;
                Log.d("suvo",dBeacon.getEddystoneURL() );
           /* AlertDialog.Builder alertbox = new AlertDialog.Builder(View.getRootView().getContext());
            alertbox.setMessage("No Internet Connection");
            alertbox.setTitle("Warning");
            alertbox.setIcon(R.drawable.ic_action_tasker);

            alertbox.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0,
                                            int arg1) {

                        }
                    });

            alertbox.show();
*/
        }


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

}
