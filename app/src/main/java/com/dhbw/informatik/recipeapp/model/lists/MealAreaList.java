package com.dhbw.informatik.recipeapp.model.lists;

import com.dhbw.informatik.recipeapp.model.MealArea;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MealAreaList {
    @SerializedName("meals")
    private List<MealArea> areaList;

    public MealAreaList(List<MealArea> areaList) {
        this.areaList = areaList;
    }

    public List<MealArea> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<MealArea> areaList) {
        this.areaList = areaList;
    }
}
