package com.dhbw.informatik.recipeapp;

import static androidx.core.content.ContextCompat.getSystemService;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.dhbw.informatik.recipeapp.activity.MainActivity;

public class HomeFragment extends Fragment {
    private MainActivity mainActivity;

    public HomeFragment(MainActivity mainActivity) {
        this.mainActivity=mainActivity;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        mainActivity.queryFunctionality();
        super.onViewStateRestored(savedInstanceState);
    }

}