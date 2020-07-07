package com.cuongtt.homnayangi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Recipes {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("dish_types")
    @Expose
    private String dish_types;
    @SerializedName("servings")
    @Expose
    private Integer servings;
    @SerializedName("readyInMinutes")
    @Expose
    private Integer readyInMinutes;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("images")
    @Expose
    private ArrayList<String> images;
    @SerializedName("inductions")
    @Expose
    private ArrayList<String> inductions;

    public Recipes() {
    }

    public Recipes(String id, String title, String dish_types, Integer servings, Integer readyInMinutes, String summary, ArrayList<String> images, ArrayList<String> inductions) {
        this.id = id;
        this.title = title;
        this.dish_types = dish_types;
        this.servings = servings;
        this.readyInMinutes = readyInMinutes;
        this.summary = summary;
        this.images = images;
        this.inductions = inductions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDish_types() {
        return dish_types;
    }

    public void setDish_types(String dish_types) {
        this.dish_types = dish_types;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public Integer getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(Integer readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public void setImages(String value, int position){
        this.images.add(position, value);
    }

    public ArrayList<String> getInductions() {
        return inductions;
    }

    public void setInductions(ArrayList<String> inductions) {
        this.inductions = inductions;
    }


    @Override
    public String toString() {
        return "Recipes{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", dish_types='" + dish_types + '\'' +
                ", servings=" + servings +
                ", readyInMinutes=" + readyInMinutes +
                ", summary='" + summary + '\'' +
                ", images=[" + images +
                "], inductions=[" + inductions +
                "]}";
    }
}
