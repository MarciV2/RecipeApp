package com.dhbw.informatik.recipeapp;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.dhbw.informatik.recipeapp.model.Meal;
import com.dhbw.informatik.recipeapp.model.lists.MealList;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    public static final String FILENAME_OWN_RECIPES="ownRecipes.json";
    public static final String FILENAME_FAVOURITES="favourites.json";
    public static final String FILENAME_LAST_CLICKED ="lastClicked.json";

    public  MealList favourites;
    public MealList ownRecipes;
    public MealList lastClicked;

    private static FileHandler instance;
    private Context context;

    private FileHandler(){
       favourites = new MealList();
       ownRecipes = new MealList();
       lastClicked = new MealList();
    }

    public static FileHandler getInstance(){
        if(instance==null)
            instance=new FileHandler();

        return instance;
    }

    public void setContext(Context context){this.context=context;}


    public void saveFiles(){
        saveFavourites();
        save(new Gson().toJson(ownRecipes),FILENAME_OWN_RECIPES);
        Log.d("test","Own Recipes saved");
        save(new Gson().toJson(lastClicked),FILENAME_LAST_CLICKED);
        Log.d("test","Last Clicked saved");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void readFiles(){
        //Favouriten und eigene Rezepte laden
        favourites = new Gson().fromJson(load(FILENAME_FAVOURITES), MealList.class);
        favourites.getMeals().forEach(Meal::fillArrays);
        ownRecipes = new Gson().fromJson(load(FILENAME_OWN_RECIPES), MealList.class);
        ownRecipes.getMeals().forEach(Meal::fillArrays);
        lastClicked = new Gson().fromJson(load(FILENAME_LAST_CLICKED), MealList.class);
        lastClicked.getMeals().forEach(Meal::fillArrays);
    }


    /**
     * Erstellt von Johannes Fahr
     * @param jsonString Jsonstring aus Abfrage welcher gespeichert werden soll
     * @param fileName Dateiname der benutzt werden soll zum Speichern
     */

    public void save(String jsonString, String fileName) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, context.MODE_PRIVATE);
            fos.write(jsonString.getBytes());
            Log.d("TAG", "Saved: "+ jsonString + "to " + context.getFilesDir() + "/" + fileName);

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
            fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br= new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while((text = br.readLine())!=null){
                sb.append(text);
            }
            Log.d("TAG", "Read:"+sb.toString() +" from " + context.getFilesDir() + "/" + fileName);

            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            //Datei als leer erstellen
            Log.d("dev","Datei wurde nicht gefunden, leere neue erstellen");
            MealList newMealList=new MealList();
            save(new Gson().toJson(newMealList),fileName);
            return new Gson().toJson(newMealList);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Erstellt von Marcel Vidmar
     * Prüft, ob Rezept bereits in Favouriten-Liste ist, wenn nicht, wird die ses hinzugefügt
     * @param meal Rezept, dass zu den Favouriten hinzugefügt werden soll
     */
    public void addToFavourites(Meal meal){
        //prüfen, dass meal noch nicht in favs ist
        for(Meal m:favourites.getMeals()) if(m.getIdMeal()==meal.getIdMeal())  return;

        favourites.getMeals().add(meal);
        save(new Gson().toJson(favourites),FILENAME_FAVOURITES);
        Log.d("test",meal.getStrMeal()+" zu favouriten hinzugefügt");
    }


    /**
     * Erstellt von Marcel Vidmar
     * Prüft, ob angegebenes Rezept in der Favouriten-Liste ist
     * @return ob angegebenes Rezept in der Favouriten-Liste ist
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean isMealFav(Meal m) {
        readFiles();
        for(Meal m2:favourites.getMeals()){
            if(m.getIdMeal()==m2.getIdMeal()) return true;
        }
        return false;
    }

    /**
     * Erstellt von Marcel Vidmar
     * Entfernt das angegebene Rezept aus den Favouriten
     * @param meal Rezept, das aus der Favouriten-Liste entfernt werden soll
     */
    public void removeFromFavourites(Meal meal){
        favourites = new Gson().fromJson(load(FILENAME_FAVOURITES), MealList.class);
        //prüfen, dass meal noch nicht in favs ist

        List<Meal> mealsToRemove=new ArrayList<>();

        for(Meal m:favourites.getMeals()) if(m.getIdMeal()==meal.getIdMeal())  mealsToRemove.add(m);

        favourites.getMeals().removeAll(mealsToRemove);
        save(new Gson().toJson(favourites),FILENAME_FAVOURITES);
        Log.d("test",meal.getStrMeal()+" von favouriten entfernt");
    }
    public void removeFromLastClicked(Meal meal){
        lastClicked = new Gson().fromJson(load(FILENAME_LAST_CLICKED), MealList.class);

        List<Meal> mealsToRemove=new ArrayList<>();

        for(Meal m:lastClicked.getMeals()) if(m.getIdMeal()==meal.getIdMeal())  mealsToRemove.add(m);

        lastClicked.getMeals().removeAll(mealsToRemove);
        save(new Gson().toJson(lastClicked),FILENAME_LAST_CLICKED);
        Log.d("test",meal.getStrMeal()+" von favouriten entfernt");
    }

    public void saveFavourites(){
        save(new Gson().toJson(favourites),FILENAME_FAVOURITES);
        Log.d("test","Favourites saved");
    }

    public void lastClicked(Meal meal)
    {
        MealList temp;
        temp=new MealList();
        for(Meal m:lastClicked.getMeals()) if(m.getIdMeal()==meal.getIdMeal()) {
            removeFromLastClicked(m);

            temp.getMeals().add(m);
            for(int i=0;i<lastClicked.getMeals().size();i++)
            {
                temp.getMeals().add(lastClicked.getMeals().get(i));
            }
            lastClicked=temp;
            Log.d("test",meal.getStrMeal()+" zu last clicked hinzugefügt");
            return;
        }

        temp.getMeals().add(meal);
        for(int i=0;i<lastClicked.getMeals().size();i++)
        {
            temp.getMeals().add(lastClicked.getMeals().get(i));
        }
        lastClicked=temp;
        Log.d("test",meal.getStrMeal()+" zu last clicked hinzugefügt");
    }

}
