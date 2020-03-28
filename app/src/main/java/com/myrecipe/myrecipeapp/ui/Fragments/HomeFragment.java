/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 28/03/20 20:01
 */

package com.myrecipe.myrecipeapp.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.myrecipe.myrecipeapp.R;

public class HomeFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CardView userPhoto = view.findViewById(R.id.userPhoto);
        ViewPager2 mainViewPager = getActivity().findViewById(R.id.mainViewPager);
        userPhoto.setOnClickListener(v -> mainViewPager.setCurrentItem(3));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}
