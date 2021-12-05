package com.dhbw.informatik.recipeapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import com.dhbw.informatik.recipeapp.activity.MainActivity;
import com.dhbw.informatik.recipeapp.R;
import com.dhbw.informatik.recipeapp.adapter.MealPreviewAdapter;
import com.dhbw.informatik.recipeapp.model.Meal;
import com.dhbw.informatik.recipeapp.model.lists.MealList;
import com.google.android.material.snackbar.BaseTransientBottomBar;
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

public class LastClickedActivity extends AppCompatActivity {
    public MealList favourites;
    public MealList lastClicked;
    private List<Meal> mealList;
    private MealPreviewAdapter mealPreviewAdapter;
    private RecyclerView mealPreviewRecyclerView;
    private MainActivity mainActivity;
    LastClickedActivity lastClickedActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        setContentView(R.layout.activity_last_clicked);
        mainActivity = new MainActivity();
        updateMeals();
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {

        super.onPostCreate(savedInstanceState, persistentState);
    }
    /**
     * befüllt das recycler view
     */
    private void updateMeals() {

        lastClicked = new Gson().fromJson(load(mainActivity.FILENAME_LAST_CLICKED), MealList.class);
        if(lastClicked==null)return;
        mealList=lastClicked.getMeals();
        for(int i=0;i<mealList.size();i++)mealList.get(i).fillArrays();

        mealPreviewRecyclerView=findViewById(R.id.recyclerViewLastClicked);
        mealPreviewRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(),RecyclerView.VERTICAL,false));
        mealPreviewAdapter=new MealPreviewAdapter(mealList,this,true);
        mealPreviewAdapter.update(mealList);
        mealPreviewRecyclerView.setAdapter(mealPreviewAdapter);
        //API-Aufrufe starten
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

            fis.close();

            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public void addToFavourites(Meal meal){
        favourites = new Gson().fromJson(load(mainActivity.FILENAME_FAVOURITES), MealList.class);
        //prüfen, dass meal noch nicht in favs ist
        for(Meal m:favourites.getMeals()) if(m.getIdMeal()==meal.getIdMeal())  return;

        favourites.getMeals().add(meal);
        save(new Gson().toJson(favourites),mainActivity.FILENAME_FAVOURITES);
        Log.d("test",meal.getStrMeal()+" zu favouriten hinzugefügt");
    }

    public void removeFromFavourites(Meal meal){
        favourites = new Gson().fromJson(load(mainActivity.FILENAME_FAVOURITES), MealList.class);


        List<Meal> mealsToRemove=new ArrayList<>();

        for(Meal m:favourites.getMeals()) if(m.getIdMeal()==meal.getIdMeal())  mealsToRemove.add(m);

        favourites.getMeals().removeAll(mealsToRemove);

        save(new Gson().toJson(favourites),mainActivity.FILENAME_FAVOURITES);
        Log.d("test",meal.getStrMeal()+" von favouriten entfernt");
    }
    public boolean isMealFav(Meal m) {
        favourites = new Gson().fromJson(load(mainActivity.FILENAME_FAVOURITES), MealList.class);
        for(Meal m2:favourites.getMeals()){
            if(m.getIdMeal()==m2.getIdMeal()) return true;
        }
        return false;
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
}