package com.dhbw.informatik.recipeapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhbw.informatik.recipeapp.R;
import com.dhbw.informatik.recipeapp.activity.CreateOwnRecipeActivity;

public class PersonalCreatedRecipesFragment extends Fragment {

    public PersonalCreatedRecipesFragment() {
        // Required empty public constructor
    }

    
    public static PersonalCreatedRecipesFragment newInstance(String param1, String param2) {
        PersonalCreatedRecipesFragment fragment = new PersonalCreatedRecipesFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_personal_created_recipes, container, false);

        root.findViewById(R.id.btnCreateOwn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getActivity(), CreateOwnRecipeActivity.class);
                startActivityForResult(i,1);
            }
        });
        return root;
    }
}