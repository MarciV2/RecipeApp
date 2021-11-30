package com.dhbw.informatik.recipeapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhbw.informatik.recipeapp.R;
import com.dhbw.informatik.recipeapp.model.Meal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MealPreviewAdapter extends RecyclerView.Adapter<MealPreviewAdapter.MealPreviewViewHolder> {

    private List<Meal> mealList;

    public MealPreviewAdapter(List<Meal> mealList) {
        this.mealList = mealList;
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


    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public void update(List<Meal> data) {
        mealList.clear();
        mealList.addAll(data);
        notifyDataSetChanged();
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
