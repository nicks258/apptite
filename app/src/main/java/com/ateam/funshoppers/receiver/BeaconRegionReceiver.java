/*
 *
 *  Copyright (c) 2016 SameBits UG. All rights reserved.
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

package com.ateam.funshoppers.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


import com.ateam.funshoppers.BeaconLocatorApp;
import com.ateam.funshoppers.action.ActionExecutor;
import com.ateam.funshoppers.action.IAction;
import com.ateam.funshoppers.data.DataManager;
import com.ateam.funshoppers.model.ActionBeacon;
import com.ateam.funshoppers.model.RegionName;
import com.ateam.funshoppers.util.Constants;

import org.altbeacon.beacon.Region;

import java.util.List;



public class BeaconRegionReceiver extends BroadcastReceiver {

    ActionExecutor mActionExecutor;
    DataManager mDataManager;


    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO
        if (intent.hasExtra("REGION")) {
            Region region = intent.getParcelableExtra("REGION");
            if (region != null) {
                RegionName regionName = RegionName.parseString(region.getUniqueId());

                mDataManager = BeaconLocatorApp.from(context).getComponent().dataManager();
                List<ActionBeacon> actions = mDataManager.getEnabledBeaconActionsByEvent(regionName.getEventType(), regionName.getBeaconId());
                if (actions.size() > 0) {

                    mActionExecutor = BeaconLocatorApp.from(context).getComponent().actionExecutor();
                    for (ActionBeacon actionBeacon : actions) {
                        // load action from db
                        IAction action = ActionExecutor.actionBuilder(actionBeacon.getActionType(), actionBeacon.getActionParam(), actionBeacon.getNotification());
                        if (action != null) {
                            String resMessage = mActionExecutor.storeAndExecute(action);
                            if (resMessage != null ) {
                                Toast.makeText(context, resMessage, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Log.w(Constants.TAG, "Action not found for " + actionBeacon);
                        }
                    }
                }
            }
        }
    }

}
