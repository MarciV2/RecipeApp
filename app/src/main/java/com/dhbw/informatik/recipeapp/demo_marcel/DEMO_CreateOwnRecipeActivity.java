package com.dhbw.informatik.recipeapp.demo_marcel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dhbw.informatik.recipeapp.activity.MainActivity;
import com.dhbw.informatik.recipeapp.adapter.ORIEadapter;
import com.dhbw.informatik.recipeapp.model.MealIngredient;
import com.dhbw.informatik.recipeapp.model.OwnRecipeIngredientElement;
import com.dhbw.informatik.recipeapp.model.lists.MealIngredientList;

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
@SuppressWarnings("all")
public class DEMO_CreateOwnRecipeActivity extends AppCompatActivity {


    private List<OwnRecipeIngredientElement> ingredients;
    private ORIEadapter adapter;


    /**
     *Bereinigt die Liste der Zutaten und zugehörigen Mengen von leeren Zeilen und prüft auf volle zeilen
     * @return true, wenn alle entweder leer oder voll, false, wenn in mind. einer zeile nur 1 wert steht
     */
    private boolean trimIngredients(){
        ArrayList<OwnRecipeIngredientElement> emptyOries=new ArrayList<>();
        for(OwnRecipeIngredientElement orie:ingredients){
            //beide leer
            if(orie.getIngredient().isEmpty()&&orie.getMeasurement().isEmpty()) {
                emptyOries.add(orie);
            }
            //genau eines leer  -  Wenn eingabe unvollständig, false zurückgeben ^=XOR
            if(orie.getMeasurement().isEmpty()^orie.getIngredient().isEmpty())
                return false;

        }
        ingredients.removeAll(emptyOries);
        return true;
    }


    /**
     * Fügt neues Eingabefeld für zutat (mit menge) hinzu, bis maximal 20 vorhanden sind (limitierung durch datenmodell in api)
     */
    public void addIngredient() {
        if (ingredients.size() < 20) {
            ingredients.add(new OwnRecipeIngredientElement());
            adapter.notifyItemChanged(ingredients.size() + 1);
        }
    }


    /**
     * Holt alle Zutaten aus der API und bereitet diese für die Verwendung zum Auto-Complete vor. der adapter wird hierdurch aufgerufen
     */
    private void updateAllIngredientsInAdapter() {

        List<String> ingredientList = new ArrayList<>();

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
                List<MealIngredient> list = response.body().getIngredientList();

                //Liste in String-Liste umwandeln
                for (MealIngredient mi : list)
                    ingredientList.add(mi.getStrIngredient());

                adapter.setAvailableIngredients(ingredientList.toArray(new String[0]));
            }
        });

    }

}