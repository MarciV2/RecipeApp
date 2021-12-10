package com.dhbw.informatik.recipeapp.activity;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.os.Bundle;

import com.dhbw.informatik.recipeapp.R;
import com.dhbw.informatik.recipeapp.RecipeAPIService;
import com.dhbw.informatik.recipeapp.model.Meal;
import com.dhbw.informatik.recipeapp.model.lists.MealAreaList;
import com.dhbw.informatik.recipeapp.model.lists.MealList;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SelectAreaActivity extends AppCompatActivity {
    static public RecipeAPIService apiService = null;
    public MealAreaList mealAreaList;
    private ListView listView;
    private SelectAreaActivity self=this;
    public String selectedArera=null;
    List<String> your_array_list = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_area);


        // Instanciating an array list (you don't need to do this,
        // you already have yours).

        initRetrofit();
        fillList();


        ActionBar ab = getSupportActionBar();
        if(ab != null){
            ab.setTitle("Select an area or country");
        }


    }


    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {


        super.onPostCreate(savedInstanceState, persistentState);
    }
    public void setAdapter(){
        listView = (ListView) findViewById(R.id.listArea);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list );
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedArera=your_array_list.get(position);
                Log.d("Test",selectedArera);
                Intent i = new Intent(self, MainActivity.class);
                i.putExtra("filter", selectedArera);
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
   private void fillList()
    {

        Call<MealAreaList> call = apiService.getAllAreas();
        call.enqueue(new Callback<MealAreaList>() {
            @Override
            public void onResponse(@NonNull Call<MealAreaList> call, @NonNull Response<MealAreaList> response) {

                //Abfangen/Ausgeben Fehlercode Bsp. 404
                if (!response.isSuccessful()) {
                    Log.d("ERROR", "Code: " + response.code());
                    return;
                }

                mealAreaList=response.body();
                Log.d("TAG", "Areas: "+new Gson().toJson(mealAreaList));


                for(int i=0; i<mealAreaList.getAreaList().size();i++)
                {
                    your_array_list.add(mealAreaList.getAreaList().get(i).getStrArea());
                }
                Log.d("test", String.valueOf(your_array_list.size()));
                // This is the array adapter, it takes the context of the activity as a
                // first parameter, the type of list view as a second parameter and your
                // array as a third parameter.
                setAdapter();


        }



            @Override
            public void onFailure(Call<MealAreaList> call, Throwable t) {

            }
        });

    }
}