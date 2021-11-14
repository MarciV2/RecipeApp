package com.dhbw.informatik.recipeapp.model;

import com.google.gson.annotations.SerializedName;


public class Meal {

    @SerializedName("idMeal")
    private int idMeal;

    @SerializedName("strMeal")
    private String strMeal;

    @SerializedName("strCategory")
    private String strCategory;

    @SerializedName("strArea")
    private String strArea;

    @SerializedName("strInstructions")
    private String strInstructions;

    @SerializedName("strMealThumb")
    private String strMealThumb;

    @SerializedName("strTags")
    private String strTags;

    @SerializedName("strYoutube")
    private String strYoutube;

    @SerializedName("strIngredient1")
    private String strIngredient1;

    @SerializedName("strIngredient2")
    private String strIngredient2;

    @SerializedName("strIngredient3")
    private String strIngredient3;

    @SerializedName("strIngredient4")
    private String strIngredient4;

    @SerializedName("strIngredient5")
    private String strIngredient5;

    @SerializedName("strIngredient6")
    private String strIngredient6;

    @SerializedName("strIngredient7")
    private String strIngredient7;

    @SerializedName("strIngredient8")
    private String strIngredient8;

    @SerializedName("strIngredient9")
    private String strIngredient9;

    @SerializedName("strIngredient10")
    private String strIngredient10;

    @SerializedName("strIngredient11")
    private String strIngredient11;

    @SerializedName("strIngredient12")
    private String strIngredient12;

    @SerializedName("strIngredient13")
    private String strIngredient13;

    @SerializedName("strIngredient14")
    private String strIngredient14;

    @SerializedName("strIngredient15")
    private String strIngredient15;

    @SerializedName("strIngredient16")
    private String strIngredient16;

    @SerializedName("strIngredient17")
    private String strIngredient17;

    @SerializedName("strIngredient18")
    private String strIngredient18;

    @SerializedName("strIngredient19")
    private String strIngredient19;

    @SerializedName("strIngredient20")
    private String strIngredient20;

    @SerializedName("strMeasure1")
    private String strMeasure1;

    @SerializedName("strMeasure2")
    private String strMeasure2;

    @SerializedName("strMeasure3")
    private String strMeasure3;

    @SerializedName("strMeasure4")
    private String strMeasure4;

    @SerializedName("strMeasure5")
    private String strMeasure5;

    @SerializedName("strMeasure6")
    private String strMeasure6;

    @SerializedName("strMeasure7")
    private String strMeasure7;

    @SerializedName("strMeasure8")
    private String strMeasure8;

    @SerializedName("strMeasure9")
    private String strMeasure9;

    @SerializedName("strMeasure10")
    private String strMeasure10;

    @SerializedName("strMeasure11")
    private String strMeasure11;

    @SerializedName("strMeasure12")
    private String strMeasure12;

    @SerializedName("strMeasure13")
    private String strMeasure13;

    @SerializedName("strMeasure14")
    private String strMeasure14;

    @SerializedName("strMeasure15")
    private String strMeasure15;

    @SerializedName("strMeasure16")
    private String strMeasure16;

    @SerializedName("strMeasure17")
    private String strMeasure17;

    @SerializedName("strMeasure18")
    private String strMeasure18;

    @SerializedName("strMeasure19")
    private String strMeasure19;

    @SerializedName("strMeasure20")
    private String strMeasure20;




    private String[] ingredients;
    private String[] measures;

    public Meal(int idMeal, String strMeal, String strCategory, String strArea, String strInstructions, String strMealThumb, String strTags, String strYoutube, String strIngredient1, String strIngredient2, String strIngredient3, String strIngredient4, String strIngredient5, String strIngredient6, String strIngredient7, String strIngredient8, String strIngredient9, String strIngredient10, String strIngredient11, String strIngredient12, String strIngredient13, String strIngredient14, String strIngredient15, String strIngredient16, String strIngredient17, String strIngredient18, String strIngredient19, String strIngredient20, String strMeasure1, String strMeasure2, String strMeasure3, String strMeasure4, String strMeasure5, String strMeasure6, String strMeasure7, String strMeasure8, String strMeasure9, String strMeasure10, String strMeasure11, String strMeasure12, String strMeasure13, String strMeasure14, String strMeasure15, String strMeasure16, String strMeasure17, String strMeasure18, String strMeasure19, String strMeasure20) {
        this.idMeal = idMeal;
        this.strMeal = strMeal;
        this.strCategory = strCategory;
        this.strArea = strArea;
        this.strInstructions = strInstructions;
        this.strMealThumb = strMealThumb;
        this.strTags = strTags;
        this.strYoutube = strYoutube;
        this.strIngredient1 = strIngredient1;
        this.strIngredient2 = strIngredient2;
        this.strIngredient3 = strIngredient3;
        this.strIngredient4 = strIngredient4;
        this.strIngredient5 = strIngredient5;
        this.strIngredient6 = strIngredient6;
        this.strIngredient7 = strIngredient7;
        this.strIngredient8 = strIngredient8;
        this.strIngredient9 = strIngredient9;
        this.strIngredient10 = strIngredient10;
        this.strIngredient11 = strIngredient11;
        this.strIngredient12 = strIngredient12;
        this.strIngredient13 = strIngredient13;
        this.strIngredient14 = strIngredient14;
        this.strIngredient15 = strIngredient15;
        this.strIngredient16 = strIngredient16;
        this.strIngredient17 = strIngredient17;
        this.strIngredient18 = strIngredient18;
        this.strIngredient19 = strIngredient19;
        this.strIngredient20 = strIngredient20;
        this.strMeasure1 = strMeasure1;
        this.strMeasure2 = strMeasure2;
        this.strMeasure3 = strMeasure3;
        this.strMeasure4 = strMeasure4;
        this.strMeasure5 = strMeasure5;
        this.strMeasure6 = strMeasure6;
        this.strMeasure7 = strMeasure7;
        this.strMeasure8 = strMeasure8;
        this.strMeasure9 = strMeasure9;
        this.strMeasure10 = strMeasure10;
        this.strMeasure11 = strMeasure11;
        this.strMeasure12 = strMeasure12;
        this.strMeasure13 = strMeasure13;
        this.strMeasure14 = strMeasure14;
        this.strMeasure15 = strMeasure15;
        this.strMeasure16 = strMeasure16;
        this.strMeasure17 = strMeasure17;
        this.strMeasure18 = strMeasure18;
        this.strMeasure19 = strMeasure19;
        this.strMeasure20 = strMeasure20;


        //Ingredients als Liste formatieren, um besseren Zugriff darauf zu erhalten
        this.ingredients = new String[20];
        ingredients[0]=strIngredient1;
        ingredients[1]=strIngredient2;
        ingredients[2]=strIngredient3;
        ingredients[3]=strIngredient4;
        ingredients[4]=strIngredient5;
        ingredients[5]=strIngredient6;
        ingredients[6]=strIngredient7;
        ingredients[7]=strIngredient8;
        ingredients[8]=strIngredient9;
        ingredients[9]=strIngredient10;
        ingredients[10]=strIngredient11;
        ingredients[11]=strIngredient12;
        ingredients[12]=strIngredient13;
        ingredients[13]=strIngredient14;
        ingredients[14]=strIngredient15;
        ingredients[15]=strIngredient16;
        ingredients[16]=strIngredient17;
        ingredients[17]=strIngredient18;
        ingredients[18]=strIngredient19;
        ingredients[19]=strIngredient20;


        //Measures als Liste formatieren, um besseren Zugriff darauf zu erhalten
        this.measures = new String[20];
        measures[0]=strMeasure1;
        measures[1]=strMeasure2;
        measures[2]=strMeasure3;
        measures[3]=strMeasure4;
        measures[4]=strMeasure5;
        measures[5]=strMeasure6;
        measures[6]=strMeasure7;
        measures[7]=strMeasure8;
        measures[8]=strMeasure9;
        measures[9]=strMeasure10;
        measures[10]=strMeasure11;
        measures[11]=strMeasure12;
        measures[12]=strMeasure13;
        measures[13]=strMeasure14;
        measures[14]=strMeasure15;
        measures[15]=strMeasure16;
        measures[16]=strMeasure17;
        measures[17]=strMeasure18;
        measures[18]=strMeasure19;
        measures[19]=strMeasure20;
    }

    public int getIdMeal() {
        return idMeal;
    }

    public void setIdMeal(int idMeal) {
        this.idMeal = idMeal;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public void setStrMeal(String strMeal) {
        this.strMeal = strMeal;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }

    public String getStrArea() {
        return strArea;
    }

    public void setStrArea(String strArea) {
        this.strArea = strArea;
    }

    public String getStrInstructions() {
        return strInstructions;
    }

    public void setStrInstructions(String strInstructions) {
        this.strInstructions = strInstructions;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

    public void setStrMealThumb(String strMealThumb) {
        this.strMealThumb = strMealThumb;
    }

    public String getStrTags() {
        return strTags;
    }

    public void setStrTags(String strTags) {
        this.strTags = strTags;
    }

    public String getStrYoutube() {
        return strYoutube;
    }

    public void setStrYoutube(String strYoutube) {
        this.strYoutube = strYoutube;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public String[] getMeasures() {
        return measures;
    }

    public void setMeasures(String[] measures) {
        this.measures = measures;
    }
}
