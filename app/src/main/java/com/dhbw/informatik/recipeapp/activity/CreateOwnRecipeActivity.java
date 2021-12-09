package com.dhbw.informatik.recipeapp.activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import com.dhbw.informatik.recipeapp.FileHandler;
import com.dhbw.informatik.recipeapp.adapter.ORIEadapter;
import com.dhbw.informatik.recipeapp.R;
import com.dhbw.informatik.recipeapp.model.Meal;
import com.dhbw.informatik.recipeapp.model.MealArea;
import com.dhbw.informatik.recipeapp.model.MealCategory;
import com.dhbw.informatik.recipeapp.model.MealIngredient;
import com.dhbw.informatik.recipeapp.model.OwnRecipeIngredientElement;
import com.dhbw.informatik.recipeapp.model.lists.MealAreaList;
import com.dhbw.informatik.recipeapp.model.lists.MealCategoriesList;
import com.dhbw.informatik.recipeapp.model.lists.MealIngredientList;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Marcel Vidmar
 * Activity, um eigene Rezepte erstellen zu können. Verwendet Recycler view mit angehängtem Button, um bis zu 20 Zutaten dynamisch zufügen zu können.
 * Bei Eingabe werden als Auto-Vervollständigung bereits verwendete Zutaten (aus der API) vorgeschlagen
 */
public class CreateOwnRecipeActivity extends AppCompatActivity {

    private Meal meal;

    private RecyclerView recyclerView;
    private List<OwnRecipeIngredientElement> ingredients;
    private ORIEadapter adapter;
    private ActivityResultLauncher<String> getThumb;
    private Bitmap selectedBmp;
    private FileHandler fileHandler;

    private CreateOwnRecipeActivity self=this;

    public CreateOwnRecipeActivity() {
        fileHandler=FileHandler.getInstance();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_own_recipe);


        initThumbGetter();

        //Werte für AutoComplete-Felder holen
        updateCategories();
        updateAreas();

        recyclerView = findViewById(R.id.rvIngredients);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        ingredients = new ArrayList<OwnRecipeIngredientElement>();
        ingredients.add(new OwnRecipeIngredientElement("banane", "0,75"));
        ingredients.add(new OwnRecipeIngredientElement());
        adapter = new ORIEadapter(ingredients, this);
        updateAllIngredientsInAdapter();
        recyclerView.setAdapter(adapter);

        //ClickHandler für Thumbnail:
        //öffnet Intent, um bild auszuwählen, vorschaubild wird in imageView dann angezeigt
        findViewById(R.id.ivThumbnail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getThumb.launch("image/*");
            }
        });


        //ClickListener für erstellen button
        findViewById(R.id.btnCreate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etTitle=findViewById(R.id.etTitle);
                AutoCompleteTextView actvCategory=findViewById(R.id.actvCategory);
                AutoCompleteTextView actvArea=findViewById(R.id.actvArea);
                EditText etInstructions=findViewById(R.id.etInstructions);
                ImageView ivThumb=findViewById(R.id.ivThumbnail);

                String title=etTitle.getText().toString();
                String category= actvCategory.getText().toString();
                String area=actvArea.getText().toString();
                String instructions=etInstructions.getText().toString();
                String thumbnail = null;

                //Bild speichern
                String filename=title+"_"+instructions.hashCode();
                fileHandler.saveImg(selectedBmp,filename);


                if(ivThumb.getTag()!=null) thumbnail=filename;
                else thumbnail="https://www.pngkey.com/png/detail/258-2582338_food-symbol-restaurant-simbolo-comida-png.png";





                boolean ingredientsOK=trimIngredients();
                adapter.notifyDataSetChanged();

                //prüfen, dass notwendige felder befüllt wurden
                //notwendig: titel, kategorie, instructions und mind. 1 zutat
                //optional: area, thumbnail
                if(!title.isEmpty()
                    && ingredients.size()>0
                    && !instructions.isEmpty()
                    && !category.isEmpty()
                    && ingredientsOK
                ){
                    meal=new Meal();
                    meal.setStrMeal(title);
                    meal.setStrCategory(category);
                    meal.setStrInstructions(instructions);
                    if(!area.isEmpty()) meal.setStrArea(area);
                    meal.setStrMealThumb(thumbnail);
                    ArrayList<String> ingredientsList=new ArrayList<>();
                    ArrayList<String> measuresList=new ArrayList<>();

                    for(OwnRecipeIngredientElement orie : ingredients){
                        ingredientsList.add(orie.getIngredient());
                        measuresList.add(orie.getMeasurement());
                    }
                    meal.setIngredients(ingredientsList.toArray(new String[0]));
                    meal.setMeasures(measuresList.toArray(new String[0]));

                    Snackbar.make(findViewById(R.id.btnCreate),"Recipe '"+title+"' was sucessfully created!",BaseTransientBottomBar.LENGTH_LONG).show();
                    Log.d("dev","Recipe '"+title+"' was sucessfully created!");

                    Intent intent=new Intent();
                    intent.putExtra("meal",meal);
                    setResult(RESULT_OK,intent);
                    finish();//finishing activity
                }else {
                    //Meldung(en) anzeigen, wenn notwendige felder nicht ausgefüllt sind

                    if (title.isEmpty()) etTitle.setError("Please enter a title!");
                    if (ingredients.size() == 0)
                        Snackbar.make(findViewById(R.id.btnCreate), "Please enter some ingredients!", BaseTransientBottomBar.LENGTH_LONG).show();
                    if (category.isEmpty()) actvCategory.setError("Please enter a category!");
                    //Area ist optional
                    if (instructions.isEmpty())
                        etInstructions.setError("Please enter some instructions!");
                    if(!ingredientsOK) Snackbar.make(findViewById(R.id.btnCreate), "Please verify your ingredients and measures!", BaseTransientBottomBar.LENGTH_LONG).show();
                }
            }
        });

    }


    /**
     *Bereinigt die Liste der Zutaten und zugehörigen Mengen von leeren Zeilen und prüft auf volle zeilen
     * @return true, wenn alle entweder leer oder voll, false, wenn in mind. einer zeile nur 1 wert steht
     */
    private boolean trimIngredients(){
        ArrayList<OwnRecipeIngredientElement> emptyOries=new ArrayList<>();
        for(OwnRecipeIngredientElement orie:ingredients){
            //beide leer
            if(orie.getIngredient().isEmpty()&&orie.getMeasurement().isEmpty()) {
                emptyOries.add(orie);
            }
            //genau eines leer  -  Wenn eingabe unvollständig, false zurückgeben ^=XOR
            if(orie.getMeasurement().isEmpty()^orie.getIngredient().isEmpty())
                return false;

        }
        ingredients.removeAll(emptyOries);
        return true;
    }


    /**
     * Initialisiert den Handler, der die Auswahlprozedur des thumbnails übernimmt
     */
    private void initThumbGetter() {
        getThumb = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {

                        Log.d("tag", "I'm back from selecting an image!");
                        if (uri == null) {
                            Log.d("tag", "Nichts wurde ausgewählt");
                            Snackbar.make(findViewById(R.id.ivThumbnail), "No image has been selected!", BaseTransientBottomBar.LENGTH_LONG).show();
                            return;
                        }
                        Log.d("tag", "Bild wurde gefunden: " + uri);

                        ImageView ivThumb = findViewById(R.id.ivThumbnail);

                        ivThumb.setImageURI(uri);
                        ivThumb.setTag(uri);

                        selectedBmp=loadFromUri(uri);


                    }

                });
    }

    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            // check version of Android on device
            if(Build.VERSION.SDK_INT > 27){
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("tag", "touch");
        View view = self.getCurrentFocus();
        InputMethodManager manager = (InputMethodManager) self.getSystemService(this.INPUT_METHOD_SERVICE);
        assert manager != null && view != null;
        manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        return super.onTouchEvent(event);
    }

    /**
     * Fügt neues Eingabefeld für zutat (mit menge) hinzu, bis maximal 20 vorhanden sind (limitierung durch datenmodell in api)
     */
    public void addIngredient() {
        if (ingredients.size() < 20) {
            ingredients.add(new OwnRecipeIngredientElement());
            adapter.notifyItemChanged(ingredients.size() + 1);
        }
    }


    /**
     * Holt alle Zutaten aus der API und bereitet diese für die Verwendung zum Auto-Complete vor. der adapter wird hierdurch aufgerufen
     */
    private void updateAllIngredientsInAdapter() {

        List<String> ingredientList = new ArrayList<>();

        //API-Aufruf starten
        Call<MealIngredientList> call = MainActivity.apiService.getAllIngredients();
        call.enqueue(new Callback<MealIngredientList>() {
            @Override
            public void onResponse(@NonNull Call<MealIngredientList> call, @NonNull Response<MealIngredientList> response) {

                //Abfangen/Ausgeben Fehlercode Bsp. 404
                if (!response.isSuccessful()) {
                    Log.d("ERROR", "Code: " + response.code());
                    return;
                }
                List<MealIngredient> list = response.body().getIngredientList();

                //Liste in String-Liste umwandeln
                for (MealIngredient mi : list)
                    ingredientList.add(mi.getStrIngredient());

                adapter.setAvailableIngredients(ingredientList.toArray(new String[0]));
            }

            @Override
            public void onFailure(Call<MealIngredientList> call, Throwable t) {
                Snackbar.make(recyclerView, R.string.error_network_no_autoComplete, BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });

    }

    /**
     * Holt sich alle vorhandenen Kategories von der API und setzt diese als values für Auto-Complete im Kategorie-eingabe-feld
     */
    private void updateCategories(){
        List<String> categoriesList=new ArrayList<>();

        //API-Aufruf starten
        Call<MealCategoriesList> call= MainActivity.apiService.getAllCategoriesDetailed();
        call.enqueue(new Callback<MealCategoriesList>() {
            @Override
            public void onResponse(Call<MealCategoriesList> call, Response<MealCategoriesList> response) {
               //Abfangen/Ausgeben Fehlercode Bsp. 404
                if (!response.isSuccessful()) {
                    Log.d("ERROR", "Code: " + response.code());
                    return;
                }

                Log.d("dev","response: "+response.toString());
                List<MealCategory> list=response.body().getCategories();
                if(list==null) return;
                if(list.size()==0) return;
                    for (MealCategory c : list)
                        categoriesList.add((c.getStrCategory()));

                    AutoCompleteTextView actv = findViewById(R.id.actvCategory);
                    actv.setAdapter(new ArrayAdapter<String>(self,
                            android.R.layout.simple_selectable_list_item,
                             categoriesList));
                    actv.setThreshold(1);

            }

            @Override
            public void onFailure(Call<MealCategoriesList> call, Throwable t) {
                Snackbar.make(recyclerView, R.string.error_network_no_autoComplete, BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Holt sich alle vorhandenen Areas von der API und setzt diese als values für Auto-Complete im Area-eingabe-feld
     */
    private void updateAreas(){
        List<String> areasList=new ArrayList<>();

        //API-Aufruf starten
        Call<MealAreaList> call= MainActivity.apiService.getAllAreas();
        call.enqueue(new Callback<MealAreaList>() {
            @Override
            public void onResponse(Call<MealAreaList> call, Response<MealAreaList> response) {
                //Abfangen/Ausgeben Fehlercode Bsp. 404
                if (!response.isSuccessful()) {
                    Log.d("ERROR", "Code: " + response.code());
                    return;
                }

                Log.d("dev","response: "+response.toString());
                List<MealArea> list=response.body().getAreaList();
                if(list==null) return;
                if(list.size()==0) return;
                for (MealArea a : list)
                    areasList.add((a.getStrArea()));

                AutoCompleteTextView actv = findViewById(R.id.actvArea);
                actv.setAdapter(new ArrayAdapter<String>(self,
                        android.R.layout.simple_selectable_list_item,
                        areasList));
                actv.setThreshold(1);

            }

            @Override
            public void onFailure(Call<MealAreaList> call, Throwable t) {
                Snackbar.make(recyclerView, R.string.error_network_no_autoComplete, BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });
    }



}