/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 14/04/20 21:12
 */

package com.myrecipe.myrecipeapp.ui.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.PreferencesManager;
import com.myrecipe.myrecipeapp.ui.CallBacks.OnUserProfileChangedListener;


public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.logout).setOnClickListener(v -> {
            PreferencesManager.setToken(getContext(), "");

            FragmentManager fm = getActivity().getSupportFragmentManager();
            for (Fragment f : fm.getFragments()) {
                if (f instanceof OnUserProfileChangedListener) {
                    ((OnUserProfileChangedListener) f).onUserProfileChanged(null);
                }
            }

            fm.beginTransaction().remove(this).commit();
            fm.popBackStack();
        });

        view.findViewById(R.id.backButton).setOnClickListener(v -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.beginTransaction().remove(this).commit();
            fm.popBackStack();
        });

        view.findViewById(R.id.changeLanguage).setOnClickListener(v -> {
            // todo add dialog fragment
        });
    }
}
