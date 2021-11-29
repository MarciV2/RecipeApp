package com.dhbw.informatik.recipeapp.activity;

import android.os.Bundle;

import com.dhbw.informatik.recipeapp.R;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.dhbw.informatik.recipeapp.databinding.ActivitySelectAreaBinding;

public class SelectAreaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_area);

        ActionBar ab = getSupportActionBar();
        if(ab != null){
            ab.setTitle("Select an area or country");
        }
    }

}