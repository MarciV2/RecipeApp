package com.dhbw.informatik.recipeapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.dhbw.informatik.recipeapp.FileHandler;
import com.dhbw.informatik.recipeapp.adapter.MealPreviewAdapter;
import com.dhbw.informatik.recipeapp.fragment.PersonalFragment;
import com.dhbw.informatik.recipeapp.OnSwipeTouchListener;
import com.dhbw.informatik.recipeapp.fragment.SelectFilterFragment;
import com.dhbw.informatik.recipeapp.fragment.FavoritesFragment;
import com.dhbw.informatik.recipeapp.fragment.HomeFragment;
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
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public int fragment=0;
    public String query=null;
    static public RecipeAPIService apiService = null;
    BottomNavigationView navigationView;
    private MealPreviewAdapter mealPreviewAdapter;
    private RecyclerView mealPreviewRecyclerView;
    private MainActivity self=this;
    private SwipeRefreshLayout swipeContainer;
    private FileHandler fileHandler;
    public String filter=null;
    private List<Meal> mealList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Überprüfen ob Extra Filter vorhanden ist
        filter= getIntent().getStringExtra("filter");
        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setSelectedItemId(R.id.bottom_nav_home);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnNavigationItemSelectedListener(navListener);

        initRetrofit();

        fileHandler=FileHandler.getInstance();
        fileHandler.setContext(getApplicationContext());

        fileHandler.readFiles();

        Log.d("dev","files read...");

        getSupportActionBar().hide();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new HomeFragment(self)).commit();

    }


    @SuppressLint("ResourceType")
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        mealPreviewRecyclerView = findViewById(R.id.recyclerViewOfMeals);
        mealPreviewRecyclerView.setLayoutManager(new LinearLayoutManager(self, RecyclerView.VERTICAL, false));
        mealPreviewAdapter = new MealPreviewAdapter(mealList, self);
        //Prefix Abfrage um passende Funktion aufzurufen
        if(filter!=null&&filter.startsWith("area:"))areaFilter();
        if(filter!=null&&filter.startsWith("category:"))categoryFilter();
        if(filter!=null&&filter.startsWith("ingredient:"))ingredientFilter();
        //Initialfunktion müssen um Nullpointerexceptions zu vermeiden innerhalb von postcreate sein
        pullDownRefresh();
        swipeFunctionality();
        queryFunctionality();
        super.onPostCreate(savedInstanceState);
    }

    /**
     * Erstellt von Johannes Fahr
     * Fügt ein Rezept anhand seiner id zur Recyclerview hinzu
     * @param id id zum identifizieren des Rezepts
     */
    void recipeById(String id)
    {

        Call<MealList> call = apiService.getRecipeById(id);
        call.enqueue(new Callback<MealList>() {
            @Override
            public void onResponse(@NonNull Call<MealList> call, @NonNull Response<MealList> response) {
                //Abfangen/Ausgeben Fehlercode Bsp. 404
                if (!response.isSuccessful()) {
                    Log.e("ERROR", "Code: " + response.code());
                    return;
                }
                List<Meal> tmp2MealList = response.body().getMeals();
                Meal m=tmp2MealList.get(0);
                m.fillArrays();
                //Hinzufügen des gefundenen Gerichts zur Recyclerview
                mealPreviewAdapter.update(m);
                mealPreviewRecyclerView.setAdapter(mealPreviewAdapter);

            }

            @Override
            public void onFailure(Call<MealList> call, Throwable t) {

            }
        });

    }

    /**
     * Erstell von Johannes Fahr
     * Funktion holt mit dem über extra erhaltenen Kategoriefilter alle passenden Gerichte in eine Liste
     */
    void categoryFilter()
    {
        //Prefix entfernen
        filter=filter.substring(9);
        Log.d("filter: ",filter);
        Call<MealList> call = apiService.filterByCategory(filter);
        call.enqueue(new Callback<MealList>() {
            @Override
            public void onResponse(Call<MealList> call, Response<MealList> response) {
                //Abfangen/Ausgeben Fehlercode Bsp. 404
                if (!response.isSuccessful()) {
                    Log.e("ERROR", "Code: " + response.code());
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.body_container), "Errorcode: " + response.code(), Snackbar.LENGTH_SHORT).setAction("X", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });
                    snackbar.show();
                    return;
                }

                try {
                    List<Meal> list = response.body().getMeals();

                    Log.d("Arraygröße: ", String.valueOf(list.size()));

                    for (int i = 0; i < list.size(); i++) {
                        //Aufruf von recipebyid mit der passenden id
                        recipeById(String.valueOf(list.get(i).getIdMeal()));
                    }


                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.body_container), String.valueOf(list.size()) + " entrys found for the category:"+filter, Snackbar.LENGTH_SHORT).setAction("X", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });
                    snackbar.show();


                    Log.d("TAG", new Gson().toJson(list));

                } catch (NullPointerException n1) {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.body_container), "No Recipes for the category: "+filter, Snackbar.LENGTH_LONG).setAction("X", new View.OnClickListener() {
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
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.body_container), "Network error!", Snackbar.LENGTH_LONG).setAction("X", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        });

                snackbar.show();
                Log.e("Error",  t.toString());
            }
        });
    }
    /**
     * Erstellt von Johannes Fahr
     * Funktion holt mit dem über extra erhaltenen Zutatenfilter alle passenden Gerichte in eine Liste
     */
    void ingredientFilter()
    {
        //Prefix entfernen
        filter=filter.substring(11);
        Log.d("filter: ",filter);
        Call<MealList> call = apiService.filterByMainIngredient(filter);
        call.enqueue(new Callback<MealList>() {
            @Override
            public void onResponse(Call<MealList> call, Response<MealList> response) {

                //Abfangen/Ausgeben Fehlercode Bsp. 404
                if (!response.isSuccessful()) {
                    Log.e("ERROR", "Code: " + response.code());
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.body_container), "Errorcode: " + response.code(), Snackbar.LENGTH_SHORT).setAction("X", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });
                    snackbar.show();
                    return;
                }

                try {
                    List<Meal> list = response.body().getMeals();

                    Log.d("Arraygröße: ", String.valueOf(list.size()));

                    for (int i = 0; i < list.size(); i++) {
                        //Aufruf von recipebyid
                        recipeById(String.valueOf(list.get(i).getIdMeal()));
                    }


                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.body_container), String.valueOf(list.size()) + " entrys found for the ingredient:"+filter, Snackbar.LENGTH_SHORT).setAction("X", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });
                    snackbar.show();


                    Log.d("TAG", new Gson().toJson(list));

                } catch (NullPointerException n1) {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.body_container), "No Recipes for the ingredient: "+filter, Snackbar.LENGTH_LONG).setAction("X", new View.OnClickListener() {
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
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.body_container), "Network error!", Snackbar.LENGTH_LONG).setAction("X", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        });

                snackbar.show();
                Log.e("Error",  t.toString());
            }
        });
    }
    /**
     * Erstellt von Johannes Fahr
     * Funktion holt mit dem über extra erhaltenen Ortsfilter alle passenden Gerichte in eine Liste
     */
    void areaFilter()
    {
        filter=filter.substring(5);
        Log.d("filer: ",filter);
        Call<MealList> call = apiService.filterByArea(filter);
        call.enqueue(new Callback<MealList>() {
            @Override
            public void onResponse(Call<MealList> call, Response<MealList> response) {
                //Abfangen/Ausgeben Fehlercode Bsp. 404
                if (!response.isSuccessful()) {
                    Log.e("ERROR", "Code: " + response.code());
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.body_container), "Errorcode: " + response.code(), Snackbar.LENGTH_SHORT).setAction("X", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });
                    snackbar.show();
                    return;
                }

                try {
                    List<Meal> list = response.body().getMeals();

                    Log.d("Arraygröße: ", String.valueOf(list.size()));

                    for (int i = 0; i < list.size(); i++) {
                        //Aufruf von recipebyid
                        recipeById(String.valueOf(list.get(i).getIdMeal()));
                    }


                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.body_container), String.valueOf(list.size()) + " entrys found for the area:"+filter, Snackbar.LENGTH_SHORT).setAction("X", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });
                    snackbar.show();


                    Log.d("TAG", new Gson().toJson(list));

                } catch (NullPointerException n1) {
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.body_container), "No Recipes for the area: "+filter, Snackbar.LENGTH_LONG).setAction("X", new View.OnClickListener() {
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
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.body_container), "Network error!", Snackbar.LENGTH_LONG).setAction("X", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        });

                snackbar.show();
                Log.e("Error",  t.toString());
            }
        });

    }

    /**
     * Erstellt von Johannes Fahr
     * Funktion vergibt die Swipefunktionalität um mittels Swippen Fragments bzw. die Navbar Items durchzuwechseln
     */
    void swipeFunctionality()
    {
        findViewById(R.id.body_container).setOnTouchListener(new OnSwipeTouchListener(self) {
            public void onSwipeTop() {
            }
            public void onSwipeLeft() {
                Log.d("Swiped: ", "Right");
                //In Navbar passendes Item festlegen und auch fragment Variable richtig belegen
                navigationView = findViewById(R.id.bottom_navigation);
                switch(fragment){
                    case 0:
                        fragment=1;
                        navigationView.setSelectedItemId(R.id.bottom_nav_categories);
                        break;
                    case 1:
                        fragment=2;
                        navigationView.setSelectedItemId(R.id.bottom_nav_favorites);
                        break;
                    case 2:
                        fragment=3;
                        navigationView.setSelectedItemId(R.id.bottom_nav_api_test);
                        break;
                    case 3:
                        break;
                }
            }
            public void onSwipeRight() {
                Log.d("Swiped: ", "Left");
                //In Navbar passendes Item festlegen und auch fragment Variable richtig belegen
                navigationView = findViewById(R.id.bottom_navigation);
                switch(fragment){
                    case 0:
                        break;
                    case 1:
                        fragment=0;
                        navigationView.setSelectedItemId(R.id.bottom_nav_home);
                        break;
                    case 2:
                        fragment=1;
                        navigationView.setSelectedItemId(R.id.bottom_nav_categories);
                        break;
                    case 3:
                        fragment=2;
                        navigationView.setSelectedItemId(R.id.bottom_nav_favorites);
                        break;
                }
            }

            public void onSwipeBottom() {
            }

        });
    }

    /**
     * Erstellt von Johannes Fahr
     * Funktion soll Focus der Searchview auf false stellen sobald außerhalb der Tastatur getippt wird
     * @param event
     * @return
     */
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

       fileHandler.saveFiles();

    }

    @Override
    protected void onPause() {
        super.onPause();
        fileHandler.saveFiles();
    }



    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                    switch(item.getItemId()){
                        case R.id.bottom_nav_home:
                            //Wenn bereits Fragment 0 ausgewählt ist und auf 0 gewechselt wird kommt es zu Problemen weshalb dieser Fall verboten wurde
                            if(fragment!=0){
                            fragment=0;
                            ft.replace(R.id.fragment_container, new HomeFragment(self)).commit();}
                            break;
                        case R.id.bottom_nav_categories:
                            fragment=1;
                            ft.replace(R.id.fragment_container, new SelectFilterFragment()).commit();
                            break;
                        case R.id.bottom_nav_favorites:
                            fragment=2;
                            ft.replace(R.id.fragment_container, new FavoritesFragment(self)).commit();
                            break;
                        case R.id.bottom_nav_api_test:
                            fragment=3;
                            ft.replace(R.id.fragment_container, new PersonalFragment(self)).commit();

                            break;
                    }

                    return true;
                }
            };

    /**
     * Erstellt von Johannes Fahr
     * Funktion erstellt die Funktionalität des Klickens der Lupe der Tastatur der Searchview
     */
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
                Log.d("Query","submit");
                search();
                return false;
            }

        });
    }

    /**
     * Erstellt von Johannes Fahr
     * Pull down refresh um Rezepte in Recclerview durch neue zu ersetzen
     */
    public void pullDownRefresh()
    {
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        //Custom weg zum ziehen, um zu refreshe, (standard zu sensibel)
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        int mDistanceToTriggerSync = (int) (120 * metrics.density);

        swipeContainer.setDistanceToTriggerSync(mDistanceToTriggerSync);


        // Trifft ein wenn gezogen wurde
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //Einfachste Methode um Rezepte neu zu lasen ist einfach das homefragment einmal kurz durchzuwechseln
                navigationView = findViewById(R.id.bottom_navigation);
                navigationView.setSelectedItemId(R.id.bottom_nav_categories);
                navigationView.setSelectedItemId(R.id.bottom_nav_home);
                //Wichtig am Ende um Funktionalität wieder zurückzusetzen
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
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
     * Erstellt von Johannes Fahr
     * Funktion die aufgerufen wird wenn auf Lupe geklickt wurde, sei sucht nach dem Eingegebenen Stichwort
     */
    public void search()
    {
        //Query aus Searchview holen
        SearchView sV = findViewById(R.id.search_box);
        query=sV.getQuery().toString();

        if(query.isEmpty()){}
        else{
            Log.d("TAG","Eingegeben:"+ query);
            Call<MealList> call = apiService.searchRecipeByName(query);
            call.enqueue(new Callback<MealList>() {
                @Override
                public void onResponse(Call<MealList> call, Response<MealList> response) {

                    //Abfangen/Ausgeben Fehlercode Bsp. 404
                    if (!response.isSuccessful()) {
                        Log.e("ERROR", "Code: " + response.code());
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.body_container), "Errorcode: " + response.code(), Snackbar.LENGTH_SHORT).setAction("X", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                    }
                                });
                        snackbar.show();
                        return;
                    }

                    try{
                        List<Meal> list=response.body().getMeals();

                        Log.d("Arraygröße: ", String.valueOf(list.size()));

                        for(int i=0;i<list.size();i++)
                        {
                            list.get(i).fillArrays();
                        }
                        //Recyclerview mit gefundenen Rezepten befüllen
                        mealPreviewRecyclerView=findViewById(R.id.recyclerViewOfMeals);
                        mealPreviewRecyclerView.setLayoutManager(new LinearLayoutManager(self,RecyclerView.VERTICAL,false));
                        mealPreviewAdapter=new MealPreviewAdapter(list,self);
                        mealPreviewAdapter.update(list);
                        mealPreviewRecyclerView.setAdapter(mealPreviewAdapter);
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.body_container), String.valueOf(list.size())+" entrys found!", Snackbar.LENGTH_SHORT).setAction("X", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                    }
                                });
                        snackbar.show();
                        sV.clearFocus();

                        Log.d("Gefundene Rezepte: ", new Gson().toJson(list));

                    }
                    //Keine Rezepte gefunden
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
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.body_container), "Network error!", Snackbar.LENGTH_LONG).setAction("X", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });

                    snackbar.show();
                    Log.d("TAG", "error: " + t.toString());
                }
            });}
    }

    /**
     * Erstellt von Johannes Fahr
     * Ermöglicht Suche auch durch klicken der Lupe oben links
     * @param v
     */
    public void clickSearch(View v)
    {
        Log.d("TAG", "Searchview clicked");
        search();
    }


}