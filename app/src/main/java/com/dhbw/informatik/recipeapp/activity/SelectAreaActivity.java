package com.dhbw.informatik.recipeapp.activity;


import android.content.Intent;
import android.os.Bundle;

import com.dhbw.informatik.recipeapp.R;
import com.dhbw.informatik.recipeapp.RecipeAPIService;
import com.dhbw.informatik.recipeapp.model.lists.MealAreaList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//Activity zur Auswahl eines Filters in Form von einer Ortsangabe
public class SelectAreaActivity extends AppCompatActivity {
    static public RecipeAPIService apiService = null;
    public MealAreaList mealAreaList;
    private ListView listView;
    private SelectAreaActivity self=this;
    public String selectedArea=null;
    List<String> your_array_list = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_area);
        //Apiservice und Listenbefüllung werden aufgerufen
        initRetrofit();
        fillList();


        ActionBar ab = getSupportActionBar();
        if(ab != null){
            ab.setTitle(getString(R.string.filter_action_bar_title_country));
        }


    }
    /**
     * Adapter zum befüllen der Liste mit Ortsarray
     */
    public void setAdapter(){
        //Sortierung
        Collections.sort(your_array_list);

        listView = (ListView) findViewById(R.id.listArea);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list );
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedArea=your_array_list.get(position);
                Intent i = new Intent(self, MainActivity.class);
                //Extra mit prefix und ausgewähltem Ort weitergeben an MainActivity
                i.putExtra("filter", "area:"+selectedArea);
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
    /**
     * Funktion macht einen API Aufruf welcher alle Kategorien abfrägt
     */
   private void fillList()
    {

        Call<MealAreaList> call = apiService.getAllAreas();
        call.enqueue(new Callback<MealAreaList>() {
            @Override
            public void onResponse(@NonNull Call<MealAreaList> call, @NonNull Response<MealAreaList> response) {

                //Abfangen/Ausgeben Fehlercode Bsp. 404
                if (!response.isSuccessful()) {
                    Log.e("ERROR", "Code: " + response.code());
                    return;
                }

                mealAreaList=response.body();
                Log.d("Arealiste:", new Gson().toJson(mealAreaList));

                //Durchgehen der Arealist und hinzufügen der einzelnen Areas zu der Arraylist
                for(int i=0; i<mealAreaList.getAreaList().size();i++)
                {
                    your_array_list.add(mealAreaList.getAreaList().get(i).getStrArea());
                }
                Log.d("Anzahl Areas:", String.valueOf(your_array_list.size()));
                setAdapter();
        }

            @Override
            public void onFailure(Call<MealAreaList> call, Throwable t) {

            }
        });

    }
}
