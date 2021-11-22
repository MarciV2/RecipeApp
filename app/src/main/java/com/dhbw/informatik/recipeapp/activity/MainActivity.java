package com.dhbw.informatik.recipeapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dhbw.informatik.recipeapp.CategoriesFragment;
import com.dhbw.informatik.recipeapp.FavoritesFragment;
import com.dhbw.informatik.recipeapp.HomeFragment;
import com.dhbw.informatik.recipeapp.R;
import com.dhbw.informatik.recipeapp.RecipeAPIService;
import com.dhbw.informatik.recipeapp.SelectArea;
import com.dhbw.informatik.recipeapp.SelectCategory;
import com.dhbw.informatik.recipeapp.SelectMainIngredient;
import com.dhbw.informatik.recipeapp.model.lists.MealCategoriesList;
import com.dhbw.informatik.recipeapp.model.lists.MealList;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    static public RecipeAPIService apiService = null;
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setSelectedItemId(R.id.bottom_nav_home);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnNavigationItemSelectedListener(navListener);

        initRetrofit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                    switch(item.getItemId()){
                        case R.id.bottom_nav_home:
                            ft.replace(R.id.fragment_container, new HomeFragment()).commit();
                            break;
                        case R.id.bottom_nav_categories:
                            ft.replace(R.id.fragment_container, new CategoriesFragment()).commit();
                            break;
                        case R.id.bottom_nav_favorites:
                            ft.replace(R.id.fragment_container, new FavoritesFragment()).commit();
                            break;
                    }


                    return true;
                }
            };

    /**
     * Created by Marcel Vidmar
     * zu testzwecken: fügt dem test-button einen sinn zu, derzeit:
     * API-Aufruf zum abfragen der kategorien, die dann wieder als json in die konsole geschrieben werden
     */
    public void StartApiCall(View v){
        Call<MealCategoriesList> call = apiService.getAllCategoriesDetailed();
        call.enqueue(new Callback<MealCategoriesList>() {
            @Override
            public void onResponse(Call<MealCategoriesList> call, Response<MealCategoriesList> response) {
                //TODO etwas mit den daten anfangen, hier nur beispielsweise in die konsole gehauen...
                //Abfangen/Ausgeben Fehlercode Bsp. 404
                if (!response.isSuccessful()) {
                    Log.d("ERROR", "Code: " + response.code());
                    return;
                }
                Log.d("TAG", new Gson().toJson(response.body().getCategories()));
            }

            @Override
            public void onFailure(Call<MealCategoriesList> call, Throwable t) {
                Log.d("TAG", "error: " + t.toString());
            }
        });


        Call<MealList> call2 = apiService.getRandomRecipe();
        call2.enqueue(new Callback<MealList>() {
            @Override
            public void onResponse(@NonNull Call<MealList> call, @NonNull Response<MealList> response) {
                //TODO etwas mit den daten anfangen, hier nur beispielsweise in die konsole gehauen...
                //Abfangen/Ausgeben Fehlercode Bsp. 404
                if (!response.isSuccessful()) {
                    Log.d("ERROR", "Code: " + response.code());
                    return;
                }
                Log.d("TAG", new Gson().toJson(response.body().getMeals()));

                //Speichern und öffnen von response zu Testzwecken
                save(new Gson().toJson(response.body().getMeals()),"test.txt");
                load("test.txt");
            }

            @Override
            public void onFailure(Call<MealList> call, Throwable t) {

            }
        });
    }

    public void save(String jsonString, String fileName) {
        FileOutputStream fos = null;
        try {
            fos = this.openFileOutput(fileName, MODE_PRIVATE);
            fos.write(jsonString.getBytes());
            Log.d("TAG", "Saved: "+ jsonString + "to " + getFilesDir() + "/" + fileName);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void ToCategories(View v){
        Intent i = new Intent(this, SelectCategory.class);
        startActivity(i);
    }

    public void ToAreas(View v){
        Intent i = new Intent(this, SelectArea.class);
        startActivity(i);
    }

    public void ToIngredients(View v){
        Intent i = new Intent(this, SelectMainIngredient.class);
        startActivity(i);
    }


    private void showToast(){
        showToast("Fallback text");
    }

    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public String load(String fileName)
    {
        FileInputStream fis = null;
        try {
            fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br= new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while((text = br.readLine())!=null){
                sb.append(text);
            }
            Log.d("TAG", "Read:"+sb.toString() +" from " + getFilesDir() + "/" + fileName);

            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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