package com.dhbw.informatik.recipeapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhbw.informatik.recipeapp.FileHandler;
import com.dhbw.informatik.recipeapp.R;
import com.dhbw.informatik.recipeapp.model.Meal;
import com.dhbw.informatik.recipeapp.model.lists.MealList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/*
Erstellt von Marcel Vidmar
Dynamische Anzeige und sonstige Aktionen in der Detailansicht eines Rezepts
 */
public class MealDetailActivity extends AppCompatActivity {

    private FileHandler fileHandler;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_meal);
        fileHandler=FileHandler.getInstance();

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

        //Bild laden mit Picasso addon
        String mealThumb=meal.getStrMealThumb();
        if(URLUtil.isValidUrl(mealThumb)){
            Picasso.get().load(meal.getStrMealThumb()).into(ivThumb);


        }else  {
            if(mealThumb!=null) {
                Bitmap bmp = fileHandler.loadImg(mealThumb);
                ivThumb.setImageBitmap(bmp);
            }
        }


        tvTitle.setText(meal.getStrMeal());
        getSupportActionBar().setTitle(meal.getStrMeal());

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
            btnWatchOnYT.setVisibility(View.GONE);
        else
            if(meal.getStrYoutube().isEmpty())
                btnWatchOnYT.setEnabled(false);
            else
                btnWatchOnYT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Intent mit youtube Aufruf ist deutlich einfacher umsetzbar wie embeded Player
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(meal.getStrYoutube())));
                    }
                });


        //Fav-button icon
        //Icon für Fav setzen
        FloatingActionButton btn_addToFavs= findViewById(R.id.btnAddToFavourites);
        if (fileHandler.isMealFav(meal))
            btn_addToFavs.setImageResource(R.drawable.ic_favoritesfull);
        else
            btn_addToFavs.setImageResource(R.drawable.ic_favouriteempty);



        //Fav-Button-click-handler
        btn_addToFavs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!fileHandler.isMealFav(meal)) {
                    btn_addToFavs.setImageResource(R.drawable.ic_favoritesfull);
                    fileHandler.addToFavourites(meal);
                }
                else {
                    btn_addToFavs.setImageResource(R.drawable.ic_favouriteempty);
                    List<Meal> mealsToRemove=new ArrayList<>();

                    fileHandler.removeFromFavourites(meal);
                }

            }
        });
    }





}