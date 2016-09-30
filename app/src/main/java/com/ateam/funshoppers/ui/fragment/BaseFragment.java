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

package com.ateam.funshoppers.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.ateam.funshoppers.BuildConfig;
import com.ateam.funshoppers.R;
import com.ateam.funshoppers.ui.activity.MainNavigationActivity;


import butterknife.Bind;
import butterknife.ButterKnife;



public class BaseFragment extends Fragment {

    protected boolean mNeedFab;

    protected boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof MainNavigationActivity) {
            ((MainNavigationActivity) getActivity()).swappingFloatingIcon();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() instanceof MainNavigationActivity) {
            if (mNeedFab) {
                ((MainNavigationActivity) getActivity()).swappingFabUp();
            } else {
                ((MainNavigationActivity) getActivity()).hideFab();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getActivity() instanceof MainNavigationActivity && mNeedFab) {
            ((MainNavigationActivity) getActivity()).swappingFabAway();
        }
    }

    public void setNeedFab(boolean mNeedFab) {
        this.mNeedFab = mNeedFab;
    }

    public class EmptyView {

        @Bind(R.id.empty_text)
        TextView text;

        public EmptyView(View view) {
            ButterKnife.bind(this, view);
        }
    }
}