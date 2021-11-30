package com.dhbw.informatik.recipeapp;

import com.dhbw.informatik.recipeapp.model.lists.MealAreaList;
import com.dhbw.informatik.recipeapp.model.lists.MealCategoriesList;
import com.dhbw.informatik.recipeapp.model.lists.MealIngredientList;
import com.dhbw.informatik.recipeapp.model.lists.MealList;

import retrofit2.*;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RecipeAPIService {

    @GET("search.php")
    Call<MealList> searchRecipeByName(@Query("s") String recipeName);

    @GET("lookup.php")
    Call<MealList> getRecipeById(@Query("i") String id);

    @GET("random.php")
    Call<MealList> getRandomRecipe();

    @GET("categories.php")
    Call<MealCategoriesList> getAllCategoriesDetailed();

    @GET("list.php?c=list")
    Call<MealCategoriesList> getAllCategoriesShort();

    @GET("list.php?a=list")
    Call<MealAreaList> getAllAreas();

    @GET("list.php?i=list")
    Call<MealIngredientList> getAllIngredients();

    @GET("filter.php")
    Call<MealList> filterByMainIngredient(@Query("i") String ingredient);

    @GET("filter.php")
    Call<MealList> filterByCategory(@Query("c") String category);

    @GET("filter.php")
    Call<MealList> filterByArea(@Query("a") String area);
}
