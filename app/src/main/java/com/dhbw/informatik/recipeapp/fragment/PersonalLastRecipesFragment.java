package com.dhbw.informatik.recipeapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dhbw.informatik.recipeapp.FileHandler;
import com.dhbw.informatik.recipeapp.R;
import com.dhbw.informatik.recipeapp.adapter.MealPreviewAdapter;
import com.dhbw.informatik.recipeapp.model.Meal;

import java.util.List;

public class PersonalLastRecipesFragment extends Fragment {
    private List<Meal> mealList;
    private MealPreviewAdapter mealPreviewAdapter;
    private RecyclerView mealPreviewRecyclerView;
    private FileHandler fileHandler;

    public PersonalLastRecipesFragment() {
        fileHandler = FileHandler.getInstance();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_last_recipes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateMeals();
    }

    /**
     * Erstellt von Marcel Vidmar
     * befüllt das recycler view
     */
    private void updateMeals() {
        fileHandler.readFiles();

        if (fileHandler.lastClicked == null) return;
        mealList = fileHandler.lastClicked.getMeals();

        mealPreviewRecyclerView = getView().findViewById(R.id.recyclerViewLastClicked);
        mealPreviewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext(), RecyclerView.VERTICAL, false));
        mealPreviewAdapter = new MealPreviewAdapter(mealList, getActivity());
        mealPreviewRecyclerView.setAdapter(mealPreviewAdapter);
    }
}