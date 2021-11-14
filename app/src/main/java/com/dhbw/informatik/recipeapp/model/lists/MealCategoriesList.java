package com.dhbw.informatik.recipeapp.model.lists;

import com.dhbw.informatik.recipeapp.model.MealCategory;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MealCategoriesList {

    @SerializedName("categories")
    private List<MealCategory> categoryList;

    public List<MealCategory> getCategories() {
        return categoryList;
    }

    public void setCategories(List<MealCategory> categories) {
        this.categoryList = categories;
    }
}
