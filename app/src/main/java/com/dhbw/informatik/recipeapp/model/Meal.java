package com.dhbw.informatik.recipeapp.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Meal implements Serializable {

    @SerializedName("idMeal")
    private String idMeal;

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




    private transient String[] ingredients;
    private transient String[] measures;



    public void fillArrays(){
        //Ingredients als Liste formatieren, um besseren Zugriff darauf zu erhalten
        ingredients = new String[20];
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
        measures = new String[20];
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



    public String getIdMeal() {
        return idMeal;
    }

    public void setIdMeal(String idMeal) {
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

        if(ingredients.length>=1)
            strIngredient1=ingredients[0];
        if(ingredients.length>=2)
            strIngredient2=ingredients[1];
        if(ingredients.length>=3)
            strIngredient3=ingredients[2];
        if(ingredients.length>=4)
            strIngredient4=ingredients[3];
        if(ingredients.length>=5)
            strIngredient5=ingredients[4];
        if(ingredients.length>=6)
            strIngredient6=ingredients[5];
        if(ingredients.length>=7)
            strIngredient7=ingredients[6];
        if(ingredients.length>=8)
            strIngredient8=ingredients[7];
        if(ingredients.length>=9)
            strIngredient9=ingredients[8];
        if(ingredients.length>=10)
            strIngredient10=ingredients[9];
        if(ingredients.length>=11)
            strIngredient11=ingredients[10];
        if(ingredients.length>=12)
            strIngredient12=ingredients[11];
        if(ingredients.length>=13)
            strIngredient13=ingredients[12];
        if(ingredients.length>=14)
            strIngredient14=ingredients[13];
        if(ingredients.length>=15)
            strIngredient15=ingredients[14];
        if(ingredients.length>=16)
            strIngredient16=ingredients[15];
        if(ingredients.length>=17)
            strIngredient17=ingredients[16];
        if(ingredients.length>=18)
            strIngredient18=ingredients[17];
        if(ingredients.length>=19)
            strIngredient19=ingredients[18];
        if(ingredients.length>=20)
            strIngredient20=ingredients[19];
    }

    public String[] getMeasures() {
        return measures;
    }

    public void setMeasures(String[] measures) {
        this.measures = measures;
        if(measures.length>=1)
            strMeasure1=measures[0];
        if(measures.length>=2)
            strMeasure2=measures[1];
        if(measures.length>=3)
            strMeasure3=measures[2];
        if(measures.length>=4)
            strMeasure4=measures[3];
        if(measures.length>=5)
            strMeasure5=measures[4];
        if(measures.length>=6)
            strMeasure6=measures[5];
        if(measures.length>=7)
            strMeasure7=measures[6];
        if(measures.length>=8)
            strMeasure8=measures[7];
        if(measures.length>=9)
            strMeasure9=measures[8];
        if(measures.length>=10)
            strMeasure10=measures[9];
        if(measures.length>=11)
            strMeasure11=measures[10];
        if(measures.length>=12)
            strMeasure12=measures[11];
        if(measures.length>=13)
            strMeasure13=measures[12];
        if(measures.length>=14)
            strMeasure14=measures[13];
        if(measures.length>=15)
            strMeasure15=measures[14];
        if(measures.length>=16)
            strMeasure16=measures[15];
        if(measures.length>=17)
            strMeasure17=measures[16];
        if(measures.length>=18)
            strMeasure18=measures[17];
        if(measures.length>=19)
            strMeasure19=measures[18];
        if(measures.length>=20)
            strMeasure20=measures[19];


    }
}
