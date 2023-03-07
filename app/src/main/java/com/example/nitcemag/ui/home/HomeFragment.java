package com.example.nitcemag.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.nitcemag.R;
import com.example.nitcemag.ui.home.HomeViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        tabLayout =view.findViewById(R.id.tabLayout);
        viewPager=view.findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Sports"));
        tabLayout.addTab(tabLayout.newTab().setText("Academic"));
        tabLayout.addTab(tabLayout.newTab().setText("Events"));
        tabLayout.addTab(tabLayout.newTab().setText("Notice"));

        viewPager.setAdapter(new FragmentPagerAdapter(getParentFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        {
        @NonNull
        public Fragment getItem ( int position){
        switch (position) {
            case 0:
                SportsFragment sportsFragment = new SportsFragment();
                return sportsFragment;
            case 1:
                AcademicsFragment academicsFragment = new AcademicsFragment();
                return academicsFragment;
            case 2:
                EventsFragment eventsFragment = new EventsFragment();
                return eventsFragment;
            case 3:
                NoticeFragment noticeFragment = new NoticeFragment();
                return noticeFragment;
            default:
                return null;
        }
    }

        @Override
        public int getCount () {
        return tabLayout.getTabCount();
    }
    });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
               // Toast.makeText(view.getContext(), ""+state, Toast.LENGTH_SHORT).show();
            }
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                TabLayout.Tab tab = tabLayout.getTabAt(position);
                tab.select();
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
    {
        @Override
        public void onTabSelected (TabLayout.Tab tab){
        viewPager.setCurrentItem(tab.getPosition());
    }

        @Override
        public void onTabUnselected (TabLayout.Tab tab){

    }

        @Override
        public void onTabReselected (TabLayout.Tab tab){

    }
    });
    return view;
    }
}