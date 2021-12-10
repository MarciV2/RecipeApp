package com.dhbw.informatik.recipeapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dhbw.informatik.recipeapp.R;
import com.dhbw.informatik.recipeapp.RecipeAPIService;
import com.dhbw.informatik.recipeapp.model.lists.MealIngredientList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//Activity zur Auswahl eines Filters in Form von einer Hauptzutat
public class SelectMainIngredientActivity extends AppCompatActivity {
    static public RecipeAPIService apiService = null;
    public MealIngredientList mealIngredientList;
    private ListView listView;
    private final SelectMainIngredientActivity self=this;
    public String selectedIngridient=null;
    List<String> your_array_list = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_main_ingredient);
        //Apiservice und Listenbefüllung werden aufgerufen
        initRetrofit();
        fillList();
        ActionBar ab = getSupportActionBar();
        if(ab != null){
            ab.setTitle("Select your main ingredient");
        }
    }



    /**
     * Funktion macht einen API Aufruf welcher alle Zutaten abfrägt
     */
    private void fillList()
    {

        Call<MealIngredientList> call = apiService.getAllIngredients();
        call.enqueue(new Callback<MealIngredientList>() {
            @Override
            public void onResponse(@NonNull Call<MealIngredientList> call, @NonNull Response<MealIngredientList> response) {

                //Abfangen/Ausgeben Fehlercode Bsp. 404
                if (!response.isSuccessful()) {
                    Log.e("ERROR", "Code: " + response.code());
                    return;
                }

                mealIngredientList=response.body();
                Log.d("ZutatenListe:", new Gson().toJson(mealIngredientList));

                //Durchgehen der Zutateniste und hinzufügen der einzelnen Zutaten zu der Arraylist
                for(int i=0; i<mealIngredientList.getIngredientList().size();i++)
                {
                    your_array_list.add(mealIngredientList.getIngredientList().get(i).getStrIngredient());
                }
                Log.d("Anzahl Zutaten:", String.valueOf(your_array_list.size()));
                //Mit setAdapter wird die Liste mit dem nun befüllten array befüllt
                setAdapter();


            }
            @Override
            public void onFailure(Call<MealIngredientList> call, Throwable t) {

            }
        });

    }
    /**
     * Adapter zum befüllen der Liste mit Zutatenarray
     */
    public void setAdapter(){
        listView = (ListView) findViewById(R.id.listIngridient);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list );
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedIngridient=your_array_list.get(position);
                Intent i = new Intent(self, MainActivity.class);
                //Extra mit prefix und ausgewählter Zutat weitergeben an MainActivity
                i.putExtra("filter", "ingredient:"+selectedIngridient);
                startActivity(i);
            }
        });
    }
    /**
     * Created by Marcel Vidmar
     * initialisiert den globalen API-Service, soll nur durch onCreate aufgerufen werden!
     */
    private void initRetrofit() {
        //API-Retrofit initialisieren
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(RecipeAPIService.class);
    }

}