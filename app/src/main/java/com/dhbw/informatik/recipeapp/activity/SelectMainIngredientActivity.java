package com.dhbw.informatik.recipeapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.dhbw.informatik.recipeapp.R;

public class SelectMainIngredientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_main_ingredient);

        ActionBar ab = getSupportActionBar();
        if(ab != null){
            ab.setTitle("Select your main ingredient");
        }
    }


}