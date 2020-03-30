/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 30/03/20 23:47
 */

package com.myrecipe.myrecipeapp.ui.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class MainViewPagerAdapter extends FragmentStateAdapter {

    private ArrayList<Fragment> fragmentsList = new ArrayList<>();

    public MainViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentsList.get(position);
    }

    public void addFragment(Fragment fragment) {
        fragmentsList.add(fragment);
    }

    @Override
    public int getItemCount() {
        return fragmentsList.size();
    }

    public boolean contains(Fragment fragment) {
        return fragmentsList.contains(fragment);
    }
}
