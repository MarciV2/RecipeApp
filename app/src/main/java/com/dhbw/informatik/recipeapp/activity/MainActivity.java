package com.dhbw.informatik.recipeapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dhbw.informatik.recipeapp.R;
import com.dhbw.informatik.recipeapp.RecipeAPIService;
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
        setContentView(R.layout.activity_main); // test here
        initRetrofit();
        addClickHandlerToTestBtn();


    }

    /**
     * Created by Marcel Vidmar
     * zu testzwecken: fügt dem test-button einen sinn zu, derzeit:
     * API-Aufruf zum abfragen der kategorien, die dann wieder als json in die konsole geschrieben werden
     */
    private void addClickHandlerToTestBtn(){
        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<MealCategoriesList> call=apiService.getAllCategoriesDetailed();
                call.enqueue(new Callback<MealCategoriesList>() {
                    @Override
                    public void onResponse(Call<MealCategoriesList> call, Response<MealCategoriesList> response) {
                        //TODO etwas mit den daten anfangen, hier nur beispielsweise in die konsole gehauen...
                        //Abfangen/Ausgeben Fehlercode Bsp. 404
                        if(!response.isSuccessful()){
                            Log.d("ERROR", "Code: "+  response.code());
                            return;
                        }
                        Log.d("TAG", new Gson().toJson(response.body().categories));
                    }

                    @Override
                    public void onFailure(Call<MealCategoriesList> call, Throwable t) {
                        Log.d("TAG", "error: "+t.toString());
                    }
                });



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