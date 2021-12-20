package com.dhbw.informatik.recipeapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dhbw.informatik.recipeapp.FileHandler;
import com.dhbw.informatik.recipeapp.R;


public class PersonalActionsFragment extends Fragment {
    private FileHandler fileHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        fileHandler = FileHandler.getInstance();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_actions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.deleteOwnRecipes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileHandler.deleteOwnRecipes();
            }
        });
        view.findViewById(R.id.deleteSearchHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileHandler.deleteLastClicked();
            }
        });
        view.findViewById(R.id.deleteMyFavorites).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileHandler.deleteFavourites();
            }
        });
    }
}
