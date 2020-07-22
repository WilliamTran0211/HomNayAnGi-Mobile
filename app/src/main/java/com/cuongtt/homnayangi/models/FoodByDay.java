package com.cuongtt.homnayangi.models;

import java.util.ArrayList;
import java.util.Date;

public class FoodByDay {
    private Date date;
    private ArrayList<Food> foodList;

    public FoodByDay() {
    }

    public FoodByDay(Date date, ArrayList<Food> foodList) {
        this.date = date;
        this.foodList = foodList;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<Food> getFoodList() {
        return foodList;
    }

    public void setFoodList(ArrayList<Food> foodList) {
        this.foodList = foodList;
    }

    public int FoodListSize(){
        return  this.foodList.size();
    }

    public Boolean FoodListIsEmpty(){
        return this.foodList.isEmpty();
    }
}
