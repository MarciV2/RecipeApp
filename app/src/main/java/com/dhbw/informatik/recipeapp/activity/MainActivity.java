package com.dhbw.informatik.recipeapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.dhbw.informatik.recipeapp.fragment.ApiTestFragment;
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
        filter= getIntent().getStringExtra("filter");
        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setSelectedItemId(R.id.bottom_nav_home);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnNavigationItemSelectedListener(navListener);

        initRetrofit();

        fileHandler=FileHandler.getInstance();
        fileHandler.setContext(getApplicationContext());

        fileHandler.readFiles();



        getSupportActionBar().hide();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new HomeFragment(self)).commit();

    }

    @Override
    protected void onPostResume() {
        Log.d("test","Resume");

        super.onPostResume();
    }



    @SuppressLint("ResourceType")
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        mealPreviewRecyclerView = findViewById(R.id.recyclerViewOfMeals);
        mealPreviewRecyclerView.setLayoutManager(new LinearLayoutManager(self, RecyclerView.VERTICAL, false));
        mealPreviewAdapter = new MealPreviewAdapter(mealList, self);

        if(filter!=null)areaFilter();
        pullDownRefresh();
        swipeFunctionality();
        queryFunctionality();
        super.onPostCreate(savedInstanceState);
    }
    void recipeById(String id)
    {

        Call<MealList> call = apiService.getRecipeById(id);
        call.enqueue(new Callback<MealList>() {
            @Override
            public void onResponse(@NonNull Call<MealList> call, @NonNull Response<MealList> response) {
                //TODO etwas mit den daten anfangen, hier nur beispielsweise in die konsole gehauen...
                //Abfangen/Ausgeben Fehlercode Bsp. 404
                if (!response.isSuccessful()) {
                    Log.d("ERROR", "Code: " + response.code());
                    return;
                }
                List<Meal> tmp2MealList = response.body().getMeals();
                Meal m=tmp2MealList.get(0);
                m.fillArrays();

                mealPreviewAdapter.update(m);
                mealPreviewRecyclerView.setAdapter(mealPreviewAdapter);

            }

            @Override
            public void onFailure(Call<MealList> call, Throwable t) {

            }
        });

    }
    void areaFilter()
    {
        Log.d("filer: ",filter);
        Call<MealList> call = apiService.filterByArea(filter);
        call.enqueue(new Callback<MealList>() {
            @Override
            public void onResponse(Call<MealList> call, Response<MealList> response) {
                //TODO etwas mit den daten anfangen, hier nur beispielsweise in die konsole gehauen...
                //Abfangen/Ausgeben Fehlercode Bsp. 404
                if (!response.isSuccessful()) {
                    Log.d("ERROR", "Code: " + response.code());
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

                    Log.d("Arraygröße", String.valueOf(list.size()));

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
                Log.d("TAG", "error: " + t.toString());
            }
        });

    }
    void swipeFunctionality()
    {
        findViewById(R.id.body_container).setOnTouchListener(new OnSwipeTouchListener(self) {
            public void onSwipeTop() {
                Log.d("TAG", "Top");
            }
            public void onSwipeLeft() {
                Log.d("TAG", "Right");
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
                Log.d("TAG", "Left");
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
                Log.d("TAG", "Bottom");
            }

        });
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
                    Fragment selectedFragment = null;
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                    switch(item.getItemId()){
                        case R.id.bottom_nav_home:
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
                Log.d("TEst","submit");
                search();


                return false;
            }

        });
    }
    public void pullDownRefresh()
    {
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        //Custom weg zum ziehen, um zu refreshe, (standard zu sensibel)
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        int mDistanceToTriggerSync = (int) (120 * metrics.density);
        Log.d("dev","height:"+((View) swipeContainer.getParent().getParent()).getHeight()*0.6);
        Log.d("dev","density: "+30*metrics.density);

        swipeContainer.setDistanceToTriggerSync(mDistanceToTriggerSync);


        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                navigationView = findViewById(R.id.bottom_navigation);
                navigationView.setSelectedItemId(R.id.bottom_nav_categories);
                navigationView.setSelectedItemId(R.id.bottom_nav_home);
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
                fileHandler.save(new Gson().toJson(response.body().getMeals()),"test.txt");
                fileHandler.load("test.txt");
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

        if(query.isEmpty()){}
        else{
            Log.d("TAG","Eingegeben:"+ query);
            Call<MealList> call = apiService.searchRecipeByName(query);
            call.enqueue(new Callback<MealList>() {
                @Override
                public void onResponse(Call<MealList> call, Response<MealList> response) {
                    //TODO etwas mit den daten anfangen, hier nur beispielsweise in die konsole gehauen...
                    //Abfangen/Ausgeben Fehlercode Bsp. 404
                    if (!response.isSuccessful()) {
                        Log.d("ERROR", "Code: " + response.code());
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

                        Log.d("Arraygröße", String.valueOf(list.size()));

                        for(int i=0;i<list.size();i++)
                        {
                            list.get(i).fillArrays();
                        }

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




    private void ShowText(String msg){
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setTitle("Delete entry")
                .setMessage(msg);
    }


}