package com.dhbw.informatik.recipeapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dhbw.informatik.recipeapp.R;
import com.dhbw.informatik.recipeapp.RecipeAPIService;
import com.dhbw.informatik.recipeapp.model.lists.MealAreaList;
import com.dhbw.informatik.recipeapp.model.lists.MealCategoriesList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SelectCategoryActivity extends AppCompatActivity {
    static public RecipeAPIService apiService = null;
    public MealCategoriesList mealCategoriesList;
    private ListView listView;
    private SelectCategoryActivity self=this;
    public String selectedCategory=null;
    List<String> your_array_list = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);
        initRetrofit();
        fillList();
        ActionBar ab = getSupportActionBar();
        if(ab != null){
            ab.setTitle("Select a category");
        }

    }
    public void setAdapter(){
        listView = (ListView) findViewById(R.id.listCategory);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list );
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory=your_array_list.get(position);
                Log.d("Test",selectedCategory);
                Intent i = new Intent(self, MainActivity.class);
                i.putExtra("filter", "category:"+selectedCategory);
                startActivity(i);
            }
        });
    }
    private void fillList()
    {

        Call<MealCategoriesList> call = apiService.getAllCategoriesDetailed();
        call.enqueue(new Callback<MealCategoriesList>() {
            @Override
            public void onResponse(@NonNull Call<MealCategoriesList> call, @NonNull Response<MealCategoriesList> response) {

                //Abfangen/Ausgeben Fehlercode Bsp. 404
                if (!response.isSuccessful()) {
                    Log.d("ERROR", "Code: " + response.code());
                    return;
                }

                mealCategoriesList=response.body();
                Log.d("TAG", "Areas: "+new Gson().toJson(mealCategoriesList));


                for(int i=0; i<mealCategoriesList.getCategories().size();i++)
                {
                    your_array_list.add(mealCategoriesList.getCategories().get(i).getStrCategory());
                }
                Log.d("test", String.valueOf(your_array_list.size()));
                // This is the array adapter, it takes the context of the activity as a
                // first parameter, the type of list view as a second parameter and your
                // array as a third parameter.
                setAdapter();


            }



            @Override
            public void onFailure(Call<MealCategoriesList> call, Throwable t) {

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