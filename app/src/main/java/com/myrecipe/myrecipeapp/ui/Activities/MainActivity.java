/*
 * Copyright (c) Code Written and Tested by Ahmed Emad in 16/04/20 19:32
 */

package com.myrecipe.myrecipeapp.ui.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.myrecipe.myrecipeapp.R;
import com.myrecipe.myrecipeapp.data.PreferencesManager;
import com.myrecipe.myrecipeapp.ui.Adapters.MainViewPagerAdapter;
import com.myrecipe.myrecipeapp.ui.CallBacks.OnLanguageChangedListner;
import com.myrecipe.myrecipeapp.ui.Fragments.FavouritesFragment;
import com.myrecipe.myrecipeapp.ui.Fragments.HomeFragment;
import com.myrecipe.myrecipeapp.ui.Fragments.ProfileFragment;
import com.myrecipe.myrecipeapp.ui.Fragments.SearchFragment;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnLanguageChangedListner {
    MainViewPagerAdapter viewPagerAdapter;
    ViewPager2 mainViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String lang;
        if (!(lang = PreferencesManager.getStoredLanguage(this)).isEmpty()) {
            onLanguageChanged(lang, false);
        }

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
                boolean subFragments = false;
                int position = tab.getPosition();
                Fragment parentFragment = viewPagerAdapter.createFragment(position);

                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    if (fragment.getView() != null && parentFragment.getView() != null) {
                        if (((ViewGroup) fragment.getView().getParent()).getId() ==
                                parentFragment.getView().getId()) {
                            subFragments = true;
                            getSupportFragmentManager().beginTransaction().
                                    remove(fragment).
                                    commit();
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
    public void onBackPressed() {
        Fragment parentFragment = viewPagerAdapter.createFragment(mainViewPager.getCurrentItem());
        for (int i = getSupportFragmentManager().getFragments().size() - 1; i >= 0; i--) {
            Fragment fragment = getSupportFragmentManager().getFragments().get(i);
            if (fragment.getView() != null) {
                if (parentFragment.getView() == fragment.getView().getParent()) {
                    getSupportFragmentManager().beginTransaction()
                            .remove(fragment)
                            .commit();
                    return;
                }
            }
        }
        if (mainViewPager.getCurrentItem() != 0)
            mainViewPager.setCurrentItem(0);
        else {
            RecyclerView homerecyclerView = parentFragment.getView().findViewById(R.id.recipesRecyclerView);
            if (homerecyclerView.computeVerticalScrollOffset() != 0)
                scrollToTop(parentFragment);
            else
                finish();
        }
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

    @Override
    public void onLanguageChanged(String language, boolean refresh) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        if (refresh) {
            startActivity(new Intent(this, this.getClass()));
            finish();
        }
    }
}
