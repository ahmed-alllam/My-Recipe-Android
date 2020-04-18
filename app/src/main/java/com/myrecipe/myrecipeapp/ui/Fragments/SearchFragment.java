/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 18/04/20 23:42
 */

package com.myrecipe.myrecipeapp.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.ui.Activities.MainActivity;

public class SearchFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((MainActivity) getActivity()).removeFragment(this);
    }
}
