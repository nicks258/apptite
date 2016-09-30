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

package com.ateam.funshoppers.data;

import android.content.Context;


import com.ateam.funshoppers.BeaconLocatorApp;
import com.ateam.funshoppers.injection.component.DaggerDataComponent;
import com.ateam.funshoppers.injection.module.DataModule;
import com.ateam.funshoppers.model.ActionBeacon;
import com.ateam.funshoppers.model.TrackedBeacon;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by vitas on 20/12/15.
 */
public class DataManager {

    @Inject
    protected StoreService mStoreService;
    private List<ActionBeacon> mActionBeaconCache = new ArrayList<>();

    public DataManager(Context context) {
        injectDependencies(context);
    }

    protected void injectDependencies(Context context) {
        DaggerDataComponent.builder()
                .applicationComponent(BeaconLocatorApp.from(context).getComponent())
                .dataModule(new DataModule(context))
                .build()
                .inject(this);
    }

    public boolean createBeacon(TrackedBeacon beacon) {
        return mStoreService.createBeacon(beacon);
    }

    public boolean updateBeacon(TrackedBeacon beacon) {
        return mStoreService.updateBeacon(beacon);
    }

    public List<TrackedBeacon> getAllBeacons() {
        return mStoreService.getBeacons();
    }

    public boolean createBeaconAction(ActionBeacon beacon) {
        return mStoreService.createBeaconAction(beacon);
    }

    public boolean updateBeaconAction(ActionBeacon beacon) {
        return mStoreService.updateBeaconAction(beacon);
    }

    public boolean deleteBeaconAction(int id) {
        return mStoreService.deleteBeaconAction(id);
    }

    public boolean deleteBeacon(String beaconId, boolean cascade) {
        return mStoreService.deleteBeacon(beaconId, cascade);
    }

    public List<ActionBeacon> getAllEnabledBeaconActions() {
        List<ActionBeacon> actions = mStoreService.getAllEnabledBeaconActions();
        mActionBeaconCache.clear();
        mActionBeaconCache.addAll(actions);
        return actions;
    }

    public boolean enableBeaconAction(int id, boolean enable) {
        return mStoreService.updateBeaconActionEnable(id, enable);
    }

    public List<ActionBeacon> getEnabledBeaconActionsByEvent(ActionBeacon.EventType eventType, String beaconId) {
        if (!mActionBeaconCache.isEmpty()) {
            List<ActionBeacon> actionBeacons = new ArrayList<>();
            for (ActionBeacon action : mActionBeaconCache) {
                if (action.getBeaconId().equals(beaconId) && action.getEventType() == eventType) {
                    actionBeacons.add(action);
                }
            }
            if (actionBeacons.size() > 0) {
                return actionBeacons;
            }
        }
        return mStoreService.getEnabledBeaconActionsByEvent(eventType.getValue(), beaconId);
    }

    public TrackedBeacon getBeacon(String id) {
        return mStoreService.getBeacon(id);
    }

    public boolean updateBeaconDistance(final String id, double distance) {
        return mStoreService.updateBeaconDistance(id, distance);
    }
}