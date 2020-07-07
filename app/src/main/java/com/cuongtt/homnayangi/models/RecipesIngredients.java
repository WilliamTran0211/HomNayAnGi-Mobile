package com.cuongtt.homnayangi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipesIngredients {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("amount")
    @Expose
    private Float amount;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("recipe")
    @Expose
    private Recipes recipe;
    @SerializedName("ingredient")
    @Expose
    private Ingredients ingredient;

    public RecipesIngredients() {
    }

    public RecipesIngredients(String id, Float amount, String unit, Recipes recipe, Ingredients ingredient) {
        this.id = id;
        this.amount = amount;
        this.unit = unit;
        this.recipe = recipe;
        this.ingredient = ingredient;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Recipes getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipes recipe) {
        this.recipe = recipe;
    }

    public Ingredients getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredients ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public String toString() {
        return "RecipesIngredients{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", unit='" + unit + '\'' +
                ", recipe=" + recipe +
                ", ingredient=" + ingredient +
                '}';
    }
}
