package com.dhbw.informatik.recipeapp.model;

import com.google.gson.annotations.SerializedName;

public class MealArea {
    @SerializedName("strArea")
    private String strArea;

    public MealArea(String strArea) {
        this.strArea = strArea;
    }

    public void setStrArea(String strArea) {
        this.strArea = strArea;
    }

    public String getStrArea() {
        return strArea;
    }
}
