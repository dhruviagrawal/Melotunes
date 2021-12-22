package com.example.melotunes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.FragmentBreadCrumbs;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class OfflineLibrary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_library);
        initViewPager();
    }

    private void initViewPager() {
        ViewPager viewpager=findViewById(R.id.viewpager);
        TabLayout tabLayout=findViewById(R.id.tabLayout);
        ScreenSlidePagerAdapter screenSlidePagerAdapter=new ScreenSlidePagerAdapter(getSupportFragmentManager());
        screenSlidePagerAdapter.addFragments(new HomeFragment(),"Home");
        screenSlidePagerAdapter.addFragments(new PlaylistFragment(),"Playlist");
        screenSlidePagerAdapter.addFragments(new DonateFragment(),"Donate");
        viewpager.setAdapter(screenSlidePagerAdapter);
        tabLayout.setupWithViewPager(viewpager);
        
    }
    public static class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment>fragments;
        private ArrayList<String>headings;
        public ScreenSlidePagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments=new ArrayList<>();
            this.headings=new ArrayList<>();

        }
        void addFragments(Fragment fragment,String heading)
        {
            fragments.add(fragment);
            headings.add(heading);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return headings.get(position);
        }
    }
}