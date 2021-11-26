package com.dhbw.informatik.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SelectMainIngredient extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_main_ingredient);

        getSupportActionBar().hide();
    }
}