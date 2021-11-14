package com.dhbw.informatik.recipeapp.model.lists;

import com.dhbw.informatik.recipeapp.model.Meal;
import com.dhbw.informatik.recipeapp.model.MealCategory;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MealList {
    @SerializedName("meals")
    private List<Meal> meals;

    public MealList(List<Meal> meals) {
        this.meals = meals;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
}
