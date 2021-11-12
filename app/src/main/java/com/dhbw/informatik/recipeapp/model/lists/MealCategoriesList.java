package com.dhbw.informatik.recipeapp.model.lists;

import com.dhbw.informatik.recipeapp.model.MealCategory;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MealCategoriesList {

    @SerializedName("categories")
    public List<MealCategory> categories;
}
