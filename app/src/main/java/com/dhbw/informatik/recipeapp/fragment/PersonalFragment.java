package com.dhbw.informatik.recipeapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.dhbw.informatik.recipeapp.FileHandler;
import com.dhbw.informatik.recipeapp.R;
import com.dhbw.informatik.recipeapp.activity.MainActivity;
import com.dhbw.informatik.recipeapp.adapter.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class PersonalFragment extends Fragment {

    private MainActivity mainActivity;
    private FileHandler fileHandler;
    BottomNavigationView navigationView;

    public PersonalFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileHandler = FileHandler.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_personal, container, false);


        ViewPager2 mViewPager = root.findViewById(R.id.viewPager);//Get ViewPager2 view
        mViewPager.setAdapter(new ViewPagerAdapter(getActivity()));//Attach the adapter with our ViewPagerAdapter passing the host activity

        TabLayout tabLayout = root.findViewById(R.id.tabs);
        new TabLayoutMediator(tabLayout, mViewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(((ViewPagerAdapter) (mViewPager.getAdapter())).mFragmentNames[position]);//Sets tabs names as mentioned in ViewPagerAdapter fragmentNames array, this can be implemented in many different ways.
                    }
                }
        ).attach();


        return root;
    }


}