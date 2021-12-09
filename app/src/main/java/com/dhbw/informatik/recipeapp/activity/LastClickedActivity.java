package com.dhbw.informatik.recipeapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import com.dhbw.informatik.recipeapp.FileHandler;
import com.dhbw.informatik.recipeapp.activity.MainActivity;
import com.dhbw.informatik.recipeapp.R;
import com.dhbw.informatik.recipeapp.adapter.MealPreviewAdapter;
import com.dhbw.informatik.recipeapp.model.Meal;
import com.dhbw.informatik.recipeapp.model.lists.MealList;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LastClickedActivity extends AppCompatActivity {
    private List<Meal> mealList;
    private MealPreviewAdapter mealPreviewAdapter;
    private RecyclerView mealPreviewRecyclerView;
    private MainActivity mainActivity;
    LastClickedActivity lastClickedActivity;
    private FileHandler fileHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        fileHandler=FileHandler.getInstance();
        setContentView(R.layout.activity_last_clicked);
        mainActivity = new MainActivity();
        updateMeals();
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {

        super.onPostCreate(savedInstanceState, persistentState);
    }
    /**
     * befüllt das recycler view
     */
    private void updateMeals() {

        if(fileHandler.lastClicked==null)return;
        mealList=fileHandler.lastClicked.getMeals();


        mealPreviewRecyclerView=findViewById(R.id.recyclerViewLastClicked);
        mealPreviewRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(),RecyclerView.VERTICAL,false));
        mealPreviewAdapter=new MealPreviewAdapter(mealList,this);
        mealPreviewAdapter.update(mealList);
        mealPreviewRecyclerView.setAdapter(mealPreviewAdapter);
        //API-Aufrufe starten
    }

}