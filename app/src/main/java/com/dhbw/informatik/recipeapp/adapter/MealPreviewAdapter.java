package com.dhbw.informatik.recipeapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhbw.informatik.recipeapp.R;
import com.dhbw.informatik.recipeapp.activity.MainActivity;
import com.dhbw.informatik.recipeapp.activity.MealDetailActivity;
import com.dhbw.informatik.recipeapp.model.Meal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
/*
Erstellt von Marcel Vidmar
Adapter für die Rezept-Previews
 */
public class MealPreviewAdapter extends RecyclerView.Adapter<MealPreviewAdapter.MealPreviewViewHolder> {

    private List<Meal> mealList;
    private MainActivity mainActivity;

    public MealPreviewAdapter(List<Meal> mealList, MainActivity mainActivity) {
        this.mealList = mealList;
        this.mainActivity=mainActivity;
    }


    @NonNull
    @Override
    public MealPreviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MealPreviewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_preview_element, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MealPreviewViewHolder holder, int position) {

        Meal meal=mealList.get(position);

        holder.tvTitle.setText(meal.getStrMeal());
        holder.tvArea.setText(meal.getStrArea());
        holder.tvCategory.setText(meal.getStrCategory());
        Picasso.get().load(meal.getStrMealThumb()).into(holder.ivThumb);



        //Aus liste 1 string zum anzeigen machen, trennzeichen:" , "
        String ingredientsStr="Ingredients:  ";
        String[] ingredientList=meal.getIngredients();
        if(ingredientList!=null){
            for(int i=0;i<ingredientList.length;i++){
                String ingredient=ingredientList[i];
                if(ingredient!=null) {
                    if(!ingredient.isEmpty()) {
                        ingredientsStr += ingredientList[i];
                        ingredientsStr += " , ";
                    }
                }
            }
            //letztes komma mit leerzeichen abtrennen
            ingredientsStr=ingredientsStr.substring(0,ingredientsStr.length()-3);
        }


        holder.tvIngredients.setText(ingredientsStr);


        //Click-Handler für Favourite-button
        holder.faBtnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.addToFavourites(meal);
            }
        });

        //Click-Handler für Aufruf der Detailseite des Rezepts
        View.OnClickListener clOpenMeal=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(view.getContext(),MealDetailActivity.class);
                i.putExtra("meal",meal);
                mainActivity.startActivity(i);
            }
        };
        //Angewanth auf titel und thumbnail
        holder.ivThumb.setOnClickListener(clOpenMeal);
        holder.tvTitle.setOnClickListener(clOpenMeal);

    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public void update(List<Meal> data) {
        notifyDataSetChanged();
    }



    public void update(Meal meal){
        if(mealList==null){
            mealList=new ArrayList<>();
            mealList.add(meal);
            return;
        }

        for(Meal m:mealList)
            if(m.getIdMeal()==meal.getIdMeal()) return;

        mealList.add(meal);
        notifyItemChanged(mealList.size());

    }

    public class MealPreviewViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumb;
        TextView tvTitle;
        TextView tvArea;
        TextView tvCategory;
        TextView tvIngredients;
        FloatingActionButton faBtnFavourite;

        public MealPreviewViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumb=itemView.findViewById(R.id.thumbImage);
            tvTitle=itemView.findViewById(R.id.titleLabel);
            tvArea=itemView.findViewById(R.id.areaLabel);
            tvCategory=itemView.findViewById(R.id.categoryLabel);
            tvIngredients=itemView.findViewById(R.id.ingredientsLabel);
            faBtnFavourite=itemView.findViewById(R.id.favoritesBtn);
        }
    }
}
