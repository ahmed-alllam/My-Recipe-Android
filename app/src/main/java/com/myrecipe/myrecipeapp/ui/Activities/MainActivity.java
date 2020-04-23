/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 23/04/20 19:14
 */

package com.myrecipe.myrecipeapp.ui.Activities;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.ui.Adapters.MainViewPagerAdapter;
import com.myrecipe.myrecipeapp.ui.Fragments.FavouritesFragment;
import com.myrecipe.myrecipeapp.ui.Fragments.HomeFragment;
import com.myrecipe.myrecipeapp.ui.Fragments.ProfileFragment;
import com.myrecipe.myrecipeapp.ui.Fragments.SearchFragment;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    MainViewPagerAdapter viewPagerAdapter;
    ViewPager2 mainViewPager;

    private ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewPager = findViewById(R.id.mainViewPager);
        mainViewPager.setOffscreenPageLimit(4);
        mainViewPager.setUserInputEnabled(false);
        viewPagerAdapter = new MainViewPagerAdapter(this);
        viewPagerAdapter.addFragment(new HomeFragment());
        viewPagerAdapter.addFragment(new SearchFragment());
        viewPagerAdapter.addFragment(new FavouritesFragment());
        viewPagerAdapter.addFragment(new ProfileFragment());
        mainViewPager.setAdapter(viewPagerAdapter);

        TabLayout nav_view = findViewById(R.id.tab_layout);

        ColorStateList colors;
        if (Build.VERSION.SDK_INT >= 23) {
            colors = getResources().getColorStateList(R.color.tab_layout_colors, getTheme());
        } else {
            colors = getResources().getColorStateList(R.color.tab_layout_colors);
        }

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

            Drawable icon = tab.getIcon();

            if (icon != null) {
                icon = DrawableCompat.wrap(icon);
                DrawableCompat.setTintList(icon, colors);
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
                boolean subFragments = false;
                int position = tab.getPosition();
                Fragment parentFragment = viewPagerAdapter.createFragment(position);

                for (Fragment fragment : fragments) {
                    if (fragment.getView() != null && parentFragment.getView() != null) {
                        if (((ViewGroup) fragment.getView().getParent()).getId() ==
                                parentFragment.getView().getId()) {
                            subFragments = true;
                            fragment.getParentFragmentManager().beginTransaction()
                                    .remove(fragment)
                                    .commit();
                        }
                    }
                }
                if (!subFragments) {  // parent fragment doesn't contain sub fragments
                    if (parentFragment.getView() != null) {
                        if (position == 0 || position == 2) // home or favourites fragments
                            scrollToTop(parentFragment);
                    }
                }
            }
        });
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);

        fragments.add(fragment);
    }


    @Override
    public void onBackPressed() {
        System.out.println(fragments.size());
        Fragment parentFragment = viewPagerAdapter.createFragment(mainViewPager.getCurrentItem());
        for (int i = fragments.size() - 1; i >= 0; i--) {
            Fragment fragment = fragments.get(i);
            if (fragment != parentFragment && fragment.getView() != null) {
                if (parentFragment.getView().findViewById(fragment.getView().getId()) != null) {
                    fragment.getParentFragmentManager().beginTransaction()
                            .remove(fragment)
                            .commit();
                    return;
                }
            }
        }
        if (mainViewPager.getCurrentItem() != 0) mainViewPager.setCurrentItem(0);
        else {
            if (parentFragment.getView() != null) {
                RecyclerView homerecyclerView = parentFragment.getView().findViewById(R.id.recipesRecyclerView);
                if (homerecyclerView.computeVerticalScrollOffset() != 0)
                    scrollToTop(parentFragment);
                else
                    finish();
            }
        }
    }


    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
    }

    public void removeFragment(Fragment fragment) {
        fragments.remove(fragment);
    }

    public ArrayList<Fragment> getFragments() {
        return fragments;
    }

    private void scrollToTop(Fragment fragment) {
        RecyclerView recyclerView = fragment.getView().findViewById(R.id.recipesRecyclerView);
        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(this) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        smoothScroller.setTargetPosition(0);
        recyclerView.getLayoutManager().startSmoothScroll(smoothScroller);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) fragment.getView().findViewById(R.id.appbar).getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        if (behavior != null)
            behavior.setTopAndBottomOffset(0);
    }
}
