package com.cuongtt.homnayangi.network;


import com.cuongtt.homnayangi.models.FileResults;
import com.cuongtt.homnayangi.models.Recipes;
import com.cuongtt.homnayangi.models.Users;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    //User APIs
    @Headers({"Content-type: application/json", "Accept: */*"})
    @POST("/api/users/login")
    Call<Users> login(@Body JsonObject body);

    @POST("api/users/create")
    Call<Users> signUp(@Body JsonObject body);

    @GET("/api/users/{id}")
    Call<Users> getUserInfo(@Path("id") String id);

    //Recipes APIs
    @Headers({"Content-type: application/json", "Accept: */*"})
    @GET("api/recipies/{id}")
    Call<Recipes> getRecipesInfo(@Path("id") String id);

    @Headers({"Content-type: application/json", "Accept: */*"})
    @GET("api/recipies/")
    Call<JsonArray> getAllRecipesList(@Query("search") String search);

    @Headers({"Content-type: application/json", "Accept: */*"})
    @POST("api/recipies/get-image")
    Call<FileResults> getImageUrl(@Body JsonObject body);

    @Headers({"Content-type: application/json", "Accept: */*"})
    @PUT("api/recipies/{id}/get-images")
    Call<FileResults> getRecipeImages(@Path("id") String id);

    @Headers({"Content-type: application/json", "Accept: */*"})
    @POST("/api/recipies/create")
    Call<Recipes> createRecipes(@Body JsonObject body);

    @Headers({"Content-type: application/json", "Accept: */*"})
    @POST("/api/recipies/{id}/upload-ingredients")
    Call<Recipes> recipesUploadIngredient(@Path("id") String id, @Body JsonArray body);


    //Ingredients APIs
    @Headers({"Content-type: application/json", "Accept: */*"})
    @GET("api/recipies/recipes-ingredients")
    Call<JsonArray> getIngredientsOfRecipe(@Query("recipe") String recipe);

    @Headers({"Content-type: application/json", "Accept: */*"})
    @GET("api/recipies/ingredients/")
    Call<JsonArray> getIngrdientsList();


}
