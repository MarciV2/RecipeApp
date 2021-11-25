package com.dhbw.informatik.recipeapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dhbw.informatik.recipeapp.ApiTestFragment;
import com.dhbw.informatik.recipeapp.CategoriesFragment;
import com.dhbw.informatik.recipeapp.FavoritesFragment;
import com.dhbw.informatik.recipeapp.HomeFragment;
import com.dhbw.informatik.recipeapp.R;
import com.dhbw.informatik.recipeapp.RecipeAPIService;
import com.dhbw.informatik.recipeapp.model.Meal;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

     public final String FILENAME_OWN_RECIPES="ownRecipes.json";
     public final String FILENAME_FAVOURITES="favourites.json";


    static public RecipeAPIService apiService = null;
    BottomNavigationView navigationView;
    public MealList favourites;
    public MealList ownRecipes;


    private MainActivity self=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setSelectedItemId(R.id.bottom_nav_home);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnNavigationItemSelectedListener(navListener);

        initRetrofit();

        //Favouriten und eigene Rezepte laden
        favourites=new Gson().fromJson(load(FILENAME_FAVOURITES),MealList.class);
        ownRecipes=new Gson().fromJson(load(FILENAME_OWN_RECIPES),MealList.class);

        if(favourites==null) favourites=new MealList();
        if(ownRecipes==null) ownRecipes=new MealList();


        getSupportActionBar().hide();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
         ft.replace(R.id.fragment_container, new HomeFragment()).commit();
         // inititate a search view

        /*MainActivity self=this;
        findViewById(R.id.btnCreateOwn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(self,CreateOwnRecipeActivity.class);
                startActivity(i);
            }
        });*/

       /* findViewById(R.id.toMeal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MealFragment frag = new MealFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, frag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();

                *//*
                android.app.Fragment selectedFragment = null;
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                ft.replace(R.id.fragment_container, new MealFragment()).commit();*//*

            }
        });*/

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

       saveFiles();

    }

    public void saveFiles(){
        save(new Gson().toJson(favourites),FILENAME_FAVOURITES);
        Log.d("test","Favourites saved");
        save(new Gson().toJson(ownRecipes),FILENAME_OWN_RECIPES);
        Log.d("test","Own Recipes saved");
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
                        case R.id.bottom_nav_api_test:
                            ft.replace(R.id.fragment_container, new ApiTestFragment(self)).commit();
                    }


                    return true;
                }
            };

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
     * Erstellt von Marcel Vidmar
     * zu testzwecken: fügt dem test-button einen sinn zu, derzeit:
     * API-Aufruf zum abfragen der kategorien, die dann wieder als json in die konsole geschrieben werden
     */
    public void startApiCall(View v){
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
                        List<Meal> list=response.body().getMeals();
                        list.get(0).fillArrays();

                        Log.d("TAG", new Gson().toJson(list));

                //Speichern und öffnen von response zu Testzwecken
                save(new Gson().toJson(response.body().getMeals()),"test.txt");
                load("test.txt");
            }

            @Override
            public void onFailure(Call<MealList> call, Throwable t) {

            }
        });
    }


    private void showText(String msg){
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setTitle("Delete entry")
                .setMessage(msg);
    }

    /**
     * Erstellt von Johannes Fahr
     * Wählt ein zufälliges Gericht aus und öffnet das Zubereitungsvideo
     * @param v
     */
    public void randomVid(View v)
    {
        Call<MealList> call = apiService.getRandomRecipe();
        call.enqueue(new Callback<MealList>() {
            @Override
            public void onResponse(@NonNull Call<MealList> call, @NonNull Response<MealList> response) {

                //Abfangen/Ausgeben Fehlercode Bsp. 404
                if (!response.isSuccessful()) {
                    Log.d("ERROR", "Code: " + response.code());
                    return;
                }
                List<Meal> list=response.body().getMeals();
                list.get(0).fillArrays();

                playVid(list.get(0).getStrYoutube());
                Log.d("TAG", new Gson().toJson(list));

            }

            @Override
            public void onFailure(Call<MealList> call, Throwable t) {

            }
        });
    }

    /**
     * Erstellt von Johannes Fahr
     * Ermöglicht den Aufruf von Links die innerhalb des JSON ausgelesen werden und als Parameter weitergegeben werden.
     * @param videourl Die Url die aufgerufen werden soll
     */
    public void playVid(String videourl)
    {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(videourl)));
        Log.i("Video", "Video Playing....");
    }


    private void showToast(){
        showToast("Fallback text");
    }

    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }




    //TODO umsetzen
    public void addToFavourites(View v){

        Log.d("test",v.toString()+" tried to add a favourite");
    }

    private void ShowText(String msg){
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setTitle("Delete entry")
                .setMessage(msg);
    }

    /**
     * Erstellt von Johannes Fahr
     * @param jsonString Jsonstring aus Abfrage welcher gespeichert werden soll
     * @param fileName Dateiname der benutzt werden soll zum Speichern
     */

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
    /**
     * Erstellt von Johannes Fahr
     * @param fileName Dateiname der Datei zum richtigen Aufrufen
     * @return Gibt den Inhalt der Datei als String zurück
     */
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



}