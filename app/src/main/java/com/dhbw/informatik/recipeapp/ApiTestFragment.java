package com.dhbw.informatik.recipeapp;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dhbw.informatik.recipeapp.activity.CreateOwnRecipeActivity;
import com.dhbw.informatik.recipeapp.activity.MainActivity;
import com.dhbw.informatik.recipeapp.model.Meal;
import com.dhbw.informatik.recipeapp.model.lists.MealList;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiTestFragment extends Fragment {

    private MainActivity mainActivity;

    BottomNavigationView navigationView;


    public ApiTestFragment(MainActivity mainActivity) {
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
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_api_test, container, false);
        root.findViewById(R.id.btnCreateOwn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getActivity(), CreateOwnRecipeActivity.class);
                startActivityForResult(i,1);
            }
        });
        return root;
    }

    /**
     * CREATED BY Marcel Vidmar
     * Callback, für wenn die CreateOwnRecipeActivity fertig ist und ein rezept liefert
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_OK)
        {
            Meal m= (Meal) data.getSerializableExtra("meal");
            if(m!=null) {
                mainActivity.ownRecipes.getMeals().add(m);
            mainActivity.saveFiles();
            }
        }
    }

}