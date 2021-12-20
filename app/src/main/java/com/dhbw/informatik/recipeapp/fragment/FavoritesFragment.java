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
import com.dhbw.informatik.recipeapp.activity.MainActivity;
import com.dhbw.informatik.recipeapp.adapter.MealPreviewAdapter;
import com.dhbw.informatik.recipeapp.model.Meal;

import java.util.List;

/**
 * Erstellt von Marcel Vidmar
 * Listet die Favoriten als previews auf
 */
public class FavoritesFragment extends Fragment {

    private MainActivity mainActivity;
    private List<Meal> mealList;
    private MealPreviewAdapter mealPreviewAdapter;
    private RecyclerView mealPreviewRecyclerView;
    private FileHandler fileHandler;

    public FavoritesFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        fileHandler = FileHandler.getInstance();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileHandler.readFiles();
        mealList = fileHandler.favourites.getMeals();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mealPreviewRecyclerView = view.findViewById(R.id.rvFavs);

        mealPreviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mealPreviewAdapter = new MealPreviewAdapter(mealList, mainActivity);
        mealPreviewRecyclerView.setAdapter(mealPreviewAdapter);

    }

}
