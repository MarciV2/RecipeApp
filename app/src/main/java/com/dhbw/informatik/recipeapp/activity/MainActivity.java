package com.dhbw.informatik.recipeapp.activity;

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
import com.dhbw.informatik.recipeapp.model.lists.MealCategoriesList;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    static public  RecipeAPIService apiService=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRetrofit();

    }

    public void StartApiCall(View v){
        Call<MealCategoriesList> call=apiService.getAllCategoriesDetailed();
        call.enqueue(new Callback<MealCategoriesList>() {
            @Override
            public void onResponse(Call<MealCategoriesList> call, Response<MealCategoriesList> response) {
                //TODO etwas mit den daten anfangen, hier nur beispielsweise in die konsole gehauen...
                Log.d("TAG", new Gson().toJson(response.body().categories));
            }

            @Override
            public void onFailure(Call<MealCategoriesList> call, Throwable t) {
                Log.d("TAG", "error: "+t.toString());
            }
        });
    }

    private void showToast(){
        showToast("Fallback text");
    }

    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Created by Marcel Vidmar
     * zu testzwecken: fügt dem test-button einen sinn zu, derzeit:
     * API-Aufruf zum abfragen der kategorien, die dann wieder als json in die konsole geschrieben werden
     */
    private void addClickHandlerToTestBtn(){
        findViewById(R.id.apiCall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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


    private void addClickHandlerToNavBtns(){
        findViewById(R.id.MainNavCatBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_main);
                ShowText("cool");
            }
        });

        findViewById(R.id.MainNavAreaBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_select_category);
            }
        });

        findViewById(R.id.MainNavIngredientsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_select_main_ingredient);
            }
        });

    }



    /**
     * Created by Marcel Vidmar
     * initialisiert den globalen API-Service, soll nur durch onCreate aufgerufen werden!
     */
    private void initRetrofit(){
        //API-Retrofit initialisieren
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService=retrofit.create(RecipeAPIService.class);
    }

}