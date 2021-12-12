package com.dhbw.informatik.recipeapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dhbw.informatik.recipeapp.activity.MainActivity;
import com.dhbw.informatik.recipeapp.fragment.PersonalActionsFragment;
import com.dhbw.informatik.recipeapp.fragment.PersonalCreatedRecipesFragment;
import com.dhbw.informatik.recipeapp.fragment.PersonalLastRecipesFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final Fragment[] mFragments = new Fragment[] {//Initialize fragments views
//Fragment views are initialized like any other fragment (Extending Fragment)
            new PersonalCreatedRecipesFragment(),//First fragment to be displayed within the pager tab number 1
            new PersonalLastRecipesFragment(),
            new PersonalActionsFragment()
    };
    public final String[] mFragmentNames = new String[] {//Tabs names array
            "My Recipes",
            "Last Visited",
            "edit"
    };

    public ViewPagerAdapter(FragmentActivity fa){//Pager constructor receives Activity instance
        super(fa);
    }

    @Override
    public int getItemCount() {
        return mFragments.length;//Number of fragments displayed
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments[position];
    }
}
