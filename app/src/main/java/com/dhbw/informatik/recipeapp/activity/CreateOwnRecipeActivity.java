package com.dhbw.informatik.recipeapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dhbw.informatik.recipeapp.ORIEadapter;
import com.dhbw.informatik.recipeapp.R;
import com.dhbw.informatik.recipeapp.model.Meal;
import com.dhbw.informatik.recipeapp.model.MealIngredient;
import com.dhbw.informatik.recipeapp.model.OwnRecipeIngredientElement;
import com.dhbw.informatik.recipeapp.model.lists.MealIngredientList;
import com.dhbw.informatik.recipeapp.model.lists.MealList;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Marcel Vidmar
 * Activity, um eigene Rezepte erstellen zu können. Verwendet Recycler view mit angehängtem Button, um bis zu 20 Zutaten dynamisch zufügen zu können.
 * Bei Eingabe werden als Auto-Vervollständigung bereits verwendete Zutaten (aus der API) vorgeschlagen
 */
public class CreateOwnRecipeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<OwnRecipeIngredientElement> ingredients;
    private ORIEadapter adapter;


    public CreateOwnRecipeActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_own_recipe);

        recyclerView=findViewById(R.id.rvIngredients);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
        ingredients=new ArrayList<OwnRecipeIngredientElement>();
        ingredients.add(new OwnRecipeIngredientElement("banane","0,75"));
        ingredients.add(new OwnRecipeIngredientElement());
        adapter=new ORIEadapter(ingredients,this);
        updateAllIngredientsInAdapter();
        recyclerView.setAdapter(adapter);


    }

    /**
     * Fügt neues Eingabefeld für zutat (mit menge) hinzu, bis maximal 20 vorhanden sind (limitierung durch datenmodell in api)
     */
    public void addIngredient(){
        if(ingredients.size()<20){
            ingredients.add(new OwnRecipeIngredientElement());
            adapter.notifyItemChanged(ingredients.size()+1);
        }
    }


    /**
     * Holt alle Zutaten aus der API und bereitet diese für die Verwendung zum Auto-Complete vor. der adapter wird hierdurch aufgerufen
     */
    private void updateAllIngredientsInAdapter(){

        List<String> ingredientList=new ArrayList<>();

        //API-Aufruf starten
        Call<MealIngredientList> call = MainActivity.apiService.getAllIngredients();
        call.enqueue(new Callback<MealIngredientList>() {
            @Override
            public void onResponse(@NonNull Call<MealIngredientList> call, @NonNull Response<MealIngredientList> response) {

                //Abfangen/Ausgeben Fehlercode Bsp. 404
                if (!response.isSuccessful()) {
                    Log.d("ERROR", "Code: " + response.code());
                    return;
                }
                List<MealIngredient> list=response.body().getIngredientList();

                //Liste in String-Liste umwandeln
                for(MealIngredient mi:list)
                    ingredientList.add(mi.getStrIngredient());

                adapter.setAvailableIngredients(ingredientList.toArray(new String[0]));
            }

            @Override
            public void onFailure(Call<MealIngredientList> call, Throwable t) {
                Snackbar.make(recyclerView,"Network Error, AutoComplete is not available.", BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });

    }



}