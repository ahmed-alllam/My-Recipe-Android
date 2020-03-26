/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 26/03/20 19:48
 */

package com.myrecipe.myrecipeapp.ui.Activities;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 mainViewPager = findViewById(R.id.mainViewPager);
        mainViewPager.setUserInputEnabled(false);
        MainViewPagerAdapter viewPagerAdapter = new MainViewPagerAdapter(this);
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
            int color = getResources().getColor(R.color.colorPrimary);
            tab.getIcon().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        }).attach();
    }
}
