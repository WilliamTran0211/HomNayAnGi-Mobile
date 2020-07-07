package com.cuongtt.homnayangi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Ingredients {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("possible_unit")
    @Expose
    private ArrayList<String> possible_unit;

    public Ingredients() {
    }

    public Ingredients(String id, String name, ArrayList<String> possible_unit) {
        this.id = id;
        this.name = name;
        this.possible_unit = possible_unit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getPossible_unit() {
        return possible_unit;
    }

    public void setPossible_unit(ArrayList<String> possible_unit) {
        this.possible_unit = possible_unit;
    }

    @Override
    public String toString() {
        return name;
    }
}
