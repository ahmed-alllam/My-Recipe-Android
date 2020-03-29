/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 29/03/20 17:27
 */

package com.myrecipe.myrecipeapp.ui.Activities;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.ui.Adapters.MainViewPagerAdapter;
import com.myrecipe.myrecipeapp.ui.Fragments.FavouritesFragment;
import com.myrecipe.myrecipeapp.ui.Fragments.HomeFragment;
import com.myrecipe.myrecipeapp.ui.Fragments.ProfileFragment;
import com.myrecipe.myrecipeapp.ui.Fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {
    MainViewPagerAdapter viewPagerAdapter;
    ViewPager2 mainViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewPager = findViewById(R.id.mainViewPager);
        viewPagerAdapter = new MainViewPagerAdapter(this);
        viewPagerAdapter.addFragment(new HomeFragment());
        viewPagerAdapter.addFragment(new SearchFragment());
        viewPagerAdapter.addFragment(new FavouritesFragment());
        viewPagerAdapter.addFragment(new ProfileFragment());
        mainViewPager.setAdapter(viewPagerAdapter);

        TabLayout nav_view = findViewById(R.id.tab_layout);
        new TabLayoutMediator(nav_view, mainViewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setIcon(R.drawable.home);
                    break;
                case 1:
                    tab.setIcon(R.drawable.search);
                    break;
                case 2:
                    tab.setIcon(R.drawable.favourite);
                    break;
                case 3:
                    tab.setIcon(R.drawable.profile);
            }
        }).attach();

        nav_view.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // returns to the main fragment if it has subfragments
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    Fragment parentFragment = viewPagerAdapter.createFragment(tab.getPosition());
                    if (((ViewGroup) fragment.getView().getParent()).getId() ==
                            parentFragment.getView().getId()) {
                        getSupportFragmentManager().beginTransaction().
                                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).
                                remove(fragment).
                                commit();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            Fragment parentFragment = viewPagerAdapter.createFragment(mainViewPager.getCurrentItem());
            if (((ViewGroup) fragment.getView().getParent()).getId() ==
                    parentFragment.getView().getId()) {
                super.onBackPressed();
                return;
            }
        }
        finish();
    }
}
