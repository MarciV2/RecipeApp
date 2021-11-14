package com.dhbw.informatik.recipeapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dhbw.informatik.recipeapp.R;
import com.dhbw.informatik.recipeapp.RecipeAPIService;
import com.dhbw.informatik.recipeapp.SelectArea;
import com.dhbw.informatik.recipeapp.SelectCategory;
import com.dhbw.informatik.recipeapp.SelectMainIngredient;
import com.dhbw.informatik.recipeapp.model.Meal;
import com.dhbw.informatik.recipeapp.model.lists.MealCategoriesList;
import com.dhbw.informatik.recipeapp.model.lists.MealList;
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

    static public RecipeAPIService apiService = null;
    static public MealList favourites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRetrofit();

        String strFavorites=load("favourites.json");
        if(strFavorites!=null) favourites=new Gson().fromJson(strFavorites,MealList.class);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        save(new Gson().toJson(favourites),"favourites.json");
        Log.d("test","Favourites saved");
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

    public void toCategories(View v){
        Intent i = new Intent(this, SelectCategory.class);
        startActivity(i);
    }

    public void toAreas(View v){
        Intent i = new Intent(this, SelectArea.class);
        startActivity(i);
    }

    public void toIngredients(View v){
        Intent i = new Intent(this, SelectMainIngredient.class);
        startActivity(i);
    }


    private void showToast(){
        showToast("Fallback text");
    }

    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }



    /**
     * Created by Marcel Vidmar
     * initialisiert den globalen API-Service, soll nur durch onCreate aufgerufen werden!
     */
    //TODO umsetzen
    public void addToFavourites(View v){

        Log.d("test",v.toString()+" tried to add a favourite");
    }

    private void ShowText(String msg){
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setTitle("Delete entry")
                .setMessage(msg);
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