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

package com.ateam.funshoppers.viewModel;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.ateam.funshoppers.model.IManagedBeacon;
import com.ateam.funshoppers.model.TrackedBeacon;
import com.ateam.funshoppers.ui.activity.MainNavigationActivity;
import com.ateam.funshoppers.ui.fragment.BaseFragment;
import com.ateam.funshoppers.util.Constants;


public class DetectedBeaconViewModel extends BeaconViewModel {

    public DetectedBeaconViewModel(@NonNull BaseFragment fragment, @NonNull IManagedBeacon managedBeacon) {
        super(fragment, managedBeacon);
    }

    protected void launchBeaconDetailsActivity() {
        //find better way to change fragment from scan to tracked
        Intent intent = MainNavigationActivity.getStartIntent(mFragment.getActivity());
        intent.putExtra(Constants.ARG_BEACON, new TrackedBeacon(mManagedBeacon));
        mFragment.startActivity(intent);
    }
}
