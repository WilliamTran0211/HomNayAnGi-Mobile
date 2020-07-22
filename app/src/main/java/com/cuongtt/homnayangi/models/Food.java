package com.cuongtt.homnayangi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class Food {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user")
    @Expose
    private Users user;
    @SerializedName("food")
    @Expose
    private Ingredients food;
    @SerializedName("amount")
    @Expose
    private Float amount;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("created_at")
    @Expose
    private Date created_at;
    @SerializedName("updated_at")
    @Expose
    private Date updated_at;

    public Food() {
    }

    public Food(String id, Users user, Ingredients food, float amount, String unit, String note, Date created_at, Date updated_at) {
        this.id = id;
        this.user = user;
        this.food = food;
        this.amount = amount;
        this.unit = unit;
        this.note = note;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Ingredients getFood() {
        return food;
    }

    public void setFood(Ingredients food) {
        this.food = food;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
}
