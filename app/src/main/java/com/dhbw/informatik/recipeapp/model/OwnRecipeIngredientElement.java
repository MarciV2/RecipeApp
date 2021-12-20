package com.dhbw.informatik.recipeapp.model;

/**
 * Created by Marcel Vidmar
 * Datensatz, der Zutat und menge eines eigenen rezepts darstellt, musste aufgrund des Adapters der RecyclerView extra implementiert werden
 */
public class OwnRecipeIngredientElement {
    private String ingredient;
    private String measurement;


    public OwnRecipeIngredientElement() {
        ingredient = "";
        measurement = "";
    }

    public OwnRecipeIngredientElement(String ingredient, String measurement) {
        this.ingredient = ingredient;
        this.measurement = measurement;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }
}
