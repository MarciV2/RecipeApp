package com.dhbw.informatik.recipeapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhbw.informatik.recipeapp.R;
import com.dhbw.informatik.recipeapp.model.Meal;
import com.dhbw.informatik.recipeapp.model.lists.MealList;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/*
Erstellt von Marcel Vidmar
Dynamische Anzeige und sonstige Aktionen in der Detailansicht eines Rezepts
 */
public class MealDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_meal);

        Meal meal= (Meal) getIntent().getSerializableExtra("meal");

        //Prüfen, dass auch ein Meal zum Anzeigen mitgegeben wurde
        if(meal==null) {
            setResult(RESULT_CANCELED);
            Log.e("dev","ERROR: Meal-Detail-View wurde ohne Meal geöffnet!");
            finish();
        }
        ImageView ivThumb=findViewById(R.id.ivThumb);
        TextView tvTitle=findViewById(R.id.title_label);
        TextView tvIngredients=findViewById(R.id.ingredients_label);
        TextView tvCategory=findViewById(R.id.categoryLabel);
        TextView tvArea=findViewById(R.id.areaLabel);
        TextView tvInstructions=findViewById(R.id.instructions);
        Button btnWatchOnYT=findViewById(R.id.btnWatchOnYT);


        Picasso.get().load(meal.getStrMealThumb()).into(ivThumb);


        tvTitle.setText(meal.getStrMeal());

        //Ingredients&Measures
        //Aus 2 listen 1 string zum anzeigen machen
        String ingredientsStr="Ingredients:";
        meal.fillArrays();
        String[] ingredientList=meal.getIngredients();
        String[] measuresList=meal.getMeasures();
        for(int i=0;i<ingredientList.length;i++){
            String ingredient=ingredientList[i];
            String measure=measuresList[i];
            if(ingredient!=null&&measure!=null) {
                if(!ingredient.isEmpty()&&!measure.isEmpty()) {
                    ingredientsStr += "\n";
                    ingredientsStr += ingredientList[i];
                    ingredientsStr += "  -  ";
                    ingredientsStr += measuresList[i];
                }
            }
        }
        tvIngredients.setText(ingredientsStr);

        tvCategory.setText(meal.getStrCategory());
        tvArea.setText(meal.getStrArea());
        tvInstructions.setText(meal.getStrInstructions());



        //YT-Button
        if(meal.getStrYoutube()==null)
            btnWatchOnYT.setEnabled(false);
        else
            if(meal.getStrYoutube().isEmpty())
                btnWatchOnYT.setEnabled(false);
            else
                btnWatchOnYT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(meal.getStrYoutube())));
                    }
                });




        //Fav-Button-click-handler
        findViewById(R.id.btnAddToFavourites).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO besser machen... ->Risiko der asynchronität
               MealList favourites = new Gson().fromJson(load(MainActivity.FILENAME_FAVOURITES), MealList.class);
               favourites.getMeals().add(meal);
               save(new Gson().toJson(favourites),MainActivity.FILENAME_FAVOURITES);
            }
        });
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