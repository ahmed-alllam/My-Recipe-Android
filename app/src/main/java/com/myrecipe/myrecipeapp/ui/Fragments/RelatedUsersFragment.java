/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 15/04/20 17:37
 */

package com.myrecipe.myrecipeapp.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.myrecipe.myrecipeapp.R;


public class RelatedUsersFragment extends Fragment {
    static final int FOLLOWERS_TYPE = 0;
    static final int FOLLOWINGS_TYPE = 0;
    private int type;


    RelatedUsersFragment(int fragmentType) {
        this.type = fragmentType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_related_users, container, false);
    }
}
