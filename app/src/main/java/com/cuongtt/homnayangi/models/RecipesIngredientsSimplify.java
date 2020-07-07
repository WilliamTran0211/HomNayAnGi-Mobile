package com.cuongtt.homnayangi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipesIngredientsSimplify {

    @SerializedName("amount")
    @Expose
    private Float amount;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("ingredient")
    @Expose
    private String ingredient;

    public RecipesIngredientsSimplify() {
    }

    public RecipesIngredientsSimplify( Float amount, String unit, String ingredient) {

        this.amount = amount;
        this.unit = unit;
        this.ingredient = ingredient;
    }
 

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
