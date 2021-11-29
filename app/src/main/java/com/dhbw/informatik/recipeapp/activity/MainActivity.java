package com.dhbw.informatik.recipeapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MotionEventCompat;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.dhbw.informatik.recipeapp.ApiTestFragment;
import com.dhbw.informatik.recipeapp.OnSwipeTouchListener;
import com.dhbw.informatik.recipeapp.SelectFilterFragment;
import com.dhbw.informatik.recipeapp.FavoritesFragment;
import com.dhbw.informatik.recipeapp.HomeFragment;
import com.dhbw.informatik.recipeapp.R;
import com.dhbw.informatik.recipeapp.RecipeAPIService;
import com.dhbw.informatik.recipeapp.model.Meal;
import com.dhbw.informatik.recipeapp.model.lists.MealCategoriesList;
import com.dhbw.informatik.recipeapp.model.lists.MealList;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import com.google.android.material.snackbar.Snackbar;
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
    public int fragment=0;
    public String query=null;

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
        favourites = new Gson().fromJson(load(FILENAME_FAVOURITES), MealList.class);
        ownRecipes = new Gson().fromJson(load(FILENAME_OWN_RECIPES), MealList.class);

        if (favourites == null) favourites = new MealList();
        if (ownRecipes == null) ownRecipes = new MealList();


        getSupportActionBar().hide();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new HomeFragment(self)).commit();





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
    protected void onPostResume() {
        Log.d("test","Resume");
        super.onPostResume();
    }



    @SuppressLint("ResourceType")
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {

        queryFunctionality();
        findViewById(R.id.body_container).setOnTouchListener(new OnSwipeTouchListener(self) {
            public void onSwipeTop() {
                Log.d("TAG", "Top");
            }
            public void onSwipeRight() {
            Log.d("TAG", "Right");
                navigationView = findViewById(R.id.bottom_navigation);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            switch(fragment){
                case 0:
                    fragment=1;
                    ft.replace(R.id.fragment_container, new SelectFilterFragment()).commit();
                    navigationView.setSelectedItemId(R.id.bottom_nav_categories);
                    break;
                case 1:
                    fragment=2;
                    ft.replace(R.id.fragment_container, new FavoritesFragment()).commit();
                    navigationView.setSelectedItemId(R.id.bottom_nav_favorites);
                    break;
                case 2:
                    fragment=3;
                    ft.replace(R.id.fragment_container, new ApiTestFragment(self)).commit();
                    navigationView.setSelectedItemId(R.id.bottom_nav_api_test);
                    break;
                case 3:
                    break;
            }
            }
            public void onSwipeLeft() {
            Log.d("TAG", "Left");
                navigationView = findViewById(R.id.bottom_navigation);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            switch(fragment){
                case 0:
                    break;
                case 1:
                    fragment=0;
                    ft.replace(R.id.fragment_container, new HomeFragment(self)).commit();
                    navigationView.setSelectedItemId(R.id.bottom_nav_home);
                    break;
                case 2:
                    fragment=1;
                    ft.replace(R.id.fragment_container, new SelectFilterFragment()).commit();
                    navigationView.setSelectedItemId(R.id.bottom_nav_categories);
                    break;
                case 3:
                    fragment=2;
                    ft.replace(R.id.fragment_container, new FavoritesFragment()).commit();
                    navigationView.setSelectedItemId(R.id.bottom_nav_favorites);
                    break;
            }
        }

            public void onSwipeBottom() {
                Log.d("TAG", "Bottom");
            }

        });

        super.onPostCreate(savedInstanceState);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

                if(fragment==0) {
                    SearchView sV = findViewById(R.id.search_box);
                    sV.clearFocus(); }


        return super.onTouchEvent(event);
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
                            fragment=0;
                            ft.replace(R.id.fragment_container, new HomeFragment(self)).commit();
                            break;
                        case R.id.bottom_nav_categories:
                            fragment=1;
                            ft.replace(R.id.fragment_container, new SelectFilterFragment()).commit();
                            break;
                        case R.id.bottom_nav_favorites:
                            fragment=2;
                            ft.replace(R.id.fragment_container, new FavoritesFragment()).commit();
                            break;
                        case R.id.bottom_nav_api_test:
                            fragment=3;
                            ft.replace(R.id.fragment_container, new ApiTestFragment(self)).commit();

                            break;
                    }


                    return true;
                }
            };

    public void queryFunctionality()
    {

        SearchView searchView =  findViewById(R.id.search_box);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                search();


                return false;
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

    public void search()
    {
        SearchView sV = findViewById(R.id.search_box);
        query=sV.getQuery().toString();
        Log.d("TAG","Eingegeben:"+ query);
        if(query.isEmpty()){}
        else{
            Call<MealList> call = apiService.searchRecipeByName(query);
            call.enqueue(new Callback<MealList>() {
                @Override
                public void onResponse(Call<MealList> call, Response<MealList> response) {
                    //TODO etwas mit den daten anfangen, hier nur beispielsweise in die konsole gehauen...
                    //Abfangen/Ausgeben Fehlercode Bsp. 404
                    if (!response.isSuccessful()) {
                        Log.d("ERROR", "Code: " + response.code());

                        return;
                    }

                    try{
                        List<Meal> list=response.body().getMeals();
                        Log.d("Arraygröße", String.valueOf(list.size()));
                        for(int i=0;i<list.size();i++)
                        {
                            list.get(i).fillArrays();

                        }
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.body_container), String.valueOf(list.size())+" entrys found!", Snackbar.LENGTH_SHORT).setAction("X", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                    }
                                });
                        snackbar.show();
                        sV.clearFocus();
                        Log.d("TAG", new Gson().toJson(list));

                    }
                    catch(NullPointerException n1)
                    {
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.body_container), "No entrys found", Snackbar.LENGTH_LONG).setAction("X", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                    }
                                });

                        snackbar.show();
                        Log.d("TAG", "No entrys found");

                    }




                }

                @Override
                public void onFailure(Call<MealList> call, Throwable t) {
                    Log.d("TAG", "error: " + t.toString());
                }
            });}
    }
    public void clickSearch(View v)
    {
        Log.d("TAG", "Searchview clicked");
        search();
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