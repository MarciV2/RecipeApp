package com.dhbw.informatik.recipeapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhbw.informatik.recipeapp.R;
import com.dhbw.informatik.recipeapp.activity.SelectAreaActivity;
import com.dhbw.informatik.recipeapp.activity.SelectCategoryActivity;
import com.dhbw.informatik.recipeapp.activity.SelectMainIngredientActivity;

public class SelectFilterFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_filters, container, false);

        //Buttons zum Aufrufen der SelectArea/Category/Ingredient-Activity
        root.findViewById(R.id.toMainIngredients).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getActivity(), SelectMainIngredientActivity.class);
                startActivity(i);
            }
        });

        root.findViewById(R.id.toCategories).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getActivity(), SelectCategoryActivity.class);
                startActivity(i);
            }
        });

        root.findViewById(R.id.toCountries).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getActivity(), SelectAreaActivity.class);
                startActivity(i);
            }
        });

        return root;
    }
}