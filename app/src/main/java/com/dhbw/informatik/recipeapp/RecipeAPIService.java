package com.dhbw.informatik.recipeapp;

import com.dhbw.informatik.recipeapp.model.lists.MealAreaList;
import com.dhbw.informatik.recipeapp.model.lists.MealCategoriesList;
import com.dhbw.informatik.recipeapp.model.lists.MealIngredientList;
import com.dhbw.informatik.recipeapp.model.lists.MealList;

import retrofit2.*;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RecipeAPIService {

    @GET("search.php?s={name}")
    Call<MealList> searchRecipeByName(@Path("name") String name);

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

    @GET("filter.php?i={ingredient}")
    Call<MealList> filterByMainIngredient(@Path("ingredient") String ingredient);

    @GET("filter.php?c={category}")
    Call<MealList> filterByCategory(@Path("category") String category);

    @GET("filter.php?c={area}")
    Call<MealList> filterByArea(@Path("area") String area);
}
