package com.cuongtt.homnayangi.activities;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.cuongtt.homnayangi.Utils;
import com.cuongtt.homnayangi.adapters.IngredientsAdapter;
import com.cuongtt.homnayangi.adapters.InstructionsAdapter;
import com.cuongtt.homnayangi.models.Recipes;
import com.cuongtt.homnayangi.models.RecipesIngredients;
import com.cuongtt.homnayangi.network.APIService;
import com.cuongtt.homnayangi.network.ApiUtils;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cuongtt.homnayangi.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailActivity extends AppCompatActivity {

    APIService mAPIService = ApiUtils.getAPIService();
    Recipes recipe_detail;
    ArrayList<RecipesIngredients> ingredientsList;

    Toolbar toolbar;
    CollapsingToolbarLayout toolBarLayout;
    TextView tvSummary, tvReadyInMinutes, tvServings, tvDishTypes;
    RecyclerView lvIngredients, lvInstructions;
    ImageView imageDetail;
    CarouselView carouselView;

    IngredientsAdapter ingredientsAdapter;
    InstructionsAdapter instructionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        recipe_detail = new Recipes();
        ingredientsList = new ArrayList<RecipesIngredients>();

        addControls();

        Intent intent = getIntent();
        String RecipeID = intent.getStringExtra("recipe_info_id");

        fetchIngredientsOfRecipe(RecipeID);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvIngredients = findViewById(R.id.lsv_ingredients);
        ingredientsAdapter = new IngredientsAdapter(ingredientsList, RecipeDetailActivity.this);
        lvIngredients.setItemAnimator(new DefaultItemAnimator());
        lvIngredients.setLayoutManager(linearLayoutManager);
        lvIngredients.setHasFixedSize(true);
        lvIngredients.setAdapter(ingredientsAdapter);


        String personJsonString = intent.getStringExtra("Recipe_Detail");
        recipe_detail= Utils.getGsonParser().fromJson(personJsonString, Recipes.class);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        lvInstructions = findViewById(R.id.lsv_instruction);
        lvInstructions.setItemAnimator(new DefaultItemAnimator());
        lvInstructions.setLayoutManager(linearLayoutManager2);
        lvInstructions.setHasFixedSize(true);
        instructionsAdapter = new InstructionsAdapter(recipe_detail.getInductions(), RecipeDetailActivity.this);
        lvInstructions.setAdapter(instructionsAdapter);
        instructionsAdapter.notifyDataSetChanged();


        addEvents();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toolBarLayout.setTitle(recipe_detail.getTitle());
        tvSummary.setText(recipe_detail.getSummary());
        Picasso.get().load(recipe_detail.getImages().get(0)).into(imageDetail);

        tvServings.setText(recipe_detail.getServings() + " người");
        tvReadyInMinutes.setText(recipe_detail.getReadyInMinutes() + " phút");
        tvDishTypes.setText(recipe_detail.getDish_types());

        carouselView = findViewById(R.id.carouselView);
        carouselView.setPageCount(recipe_detail.getImages().size());
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Glide.with(RecipeDetailActivity.this)
                        .load(recipe_detail.getImages().get(position))
                        .fitCenter()
                        .into(imageView);
            }
        });

    }

    private void addEvents() {
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_like);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    }

    private void addControls() {
        tvSummary = findViewById(R.id.tv_recipeDetail_summary);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        imageDetail = findViewById(R.id.img_recipe_detail);

        tvDishTypes = findViewById(R.id.tv_dish_types);
        tvReadyInMinutes = findViewById(R.id.tv_readyInMinutes);
        tvServings = findViewById(R.id.tv_servings);
    }


    private void fetchRecipeDetail(final String recipeID){
        mAPIService.getRecipesInfo(recipeID).enqueue(new Callback<Recipes>() {
            @Override
            public void onResponse(Call<Recipes> call, Response<Recipes> response) {
                if(response.isSuccessful()){
                    Log.d("DEBUGGGGGGGGGG", "Recipe Detail - fetchRecipeDetail - API Call SUCCESS");
                    System.out.println(response.body().toString());
                    recipe_detail = response.body();


                }
            }

            @Override
            public void onFailure(Call<Recipes> call, Throwable throwable) {
                Log.d("DEBUGGGGG", " Recipe Detail - fetchRecipeDetail - API Call Failure - "+throwable.getMessage());
            }
        });

    }

    private void fetchIngredientsOfRecipe(final String recipeID){
        mAPIService.getIngredientsOfRecipe(recipeID).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful()){
                    Log.d("DEBUGGGGGGGGGG", "Recipe Detail - fetchIngredientsOfRecipe - API Call SUCCESS");

                    Gson gson = new Gson();

                    Type recipesIngredientsListType  = new TypeToken<ArrayList<RecipesIngredients>>(){}.getType();

                    ArrayList<RecipesIngredients> items = gson.fromJson(response.body(), recipesIngredientsListType);

                    ingredientsList.clear();
                    ingredientsList.addAll(items);

                    ingredientsAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable throwable) {
                Log.d("DEBUGGGGG", " Recipe Detail - fetchIngredientsOfRecipe - API Call Failure - "+throwable.getMessage());
            }
        });
    }




}