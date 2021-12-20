package com.dhbw.informatik.recipeapp.model.lists;

import com.dhbw.informatik.recipeapp.model.Meal;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MealList {
    @SerializedName("meals")
    private List<Meal> meals;

    public MealList(List<Meal> meals) {
        this.meals = meals;
    }

    public MealList() {
        meals = new ArrayList<>();
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
}
