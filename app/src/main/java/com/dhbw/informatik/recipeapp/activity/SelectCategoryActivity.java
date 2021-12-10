package com.dhbw.informatik.recipeapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.dhbw.informatik.recipeapp.R;

public class SelectCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        ActionBar ab = getSupportActionBar();
        if(ab != null){
            ab.setTitle("Select a category");
        }

    }

}