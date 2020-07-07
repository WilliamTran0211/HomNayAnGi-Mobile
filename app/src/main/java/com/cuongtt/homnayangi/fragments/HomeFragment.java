package com.cuongtt.homnayangi.fragments;

import java.lang.reflect.Type;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cuongtt.homnayangi.R;
import com.cuongtt.homnayangi.activities.AddIngredientDialog;
import com.cuongtt.homnayangi.activities.AddRecipeDialog;
import com.cuongtt.homnayangi.activities.MainActivity;
import com.cuongtt.homnayangi.activities.RecipeFormActivity;
import com.cuongtt.homnayangi.adapters.RecipesAdapter;
import com.cuongtt.homnayangi.models.FileResults;
import com.cuongtt.homnayangi.models.Recipes;
import com.cuongtt.homnayangi.network.APIService;
import com.cuongtt.homnayangi.network.ApiUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecipesAdapter recipesAdapter;
    private ArrayList<Recipes> recipes_list;

    FloatingActionButton fab;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        recyclerView = view.findViewById(R.id.recyclerRecipe);
        recipes_list = new ArrayList<Recipes>();
        recipesAdapter = new RecipesAdapter(recipes_list, getActivity());


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recipesAdapter);

        fletchRecipesItems();
        System.out.println(recipes_list);

        fab = view.findViewById(R.id.floating_button);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newRecipeDialog = AddRecipeDialog.newInstance();
                newRecipeDialog.show(getFragmentManager(), "newRecipeDialog");

            }
        });


        return view;
    }

    APIService mAPIService = ApiUtils.getAPIService();

    private void fletchRecipesItems(){

        mAPIService.getAllRecipesList("").enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful()){
                    Log.d("DEBUGGGGGGGGGG", "Home Fragment - getAllRecipesList - API Call SUCCESS");

                    Gson gson = new Gson();

                    Type recipesListType  = new TypeToken<ArrayList<Recipes>>(){}.getType();

                    ArrayList<Recipes> items = gson.fromJson(response.body().toString(), recipesListType);

                    recipes_list.clear();
                    recipes_list.addAll(items);

                    getRecipesImages();


                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable throwable) {
                Log.d("DEBUGGGGG", " Home Fragment - getAllRecipesList - API Call Failure - "+throwable.getMessage());
            }
        });



    }

    private void getRecipesImages(){
        System.out.println("run here getRecipesImages - " + recipes_list.toString());
        for(final Recipes item : recipes_list){
            String imgRecipeName = item.getImages().get(0);
            String id = item.getId().toString();
            System.out.println("run here getRecipesImages - " +id + " - "+ imgRecipeName);
            mAPIService.getRecipeImages(id).enqueue(new Callback<FileResults>() {
                @Override
                public void onResponse(Call<FileResults> call, Response<FileResults> response) {
                    if(response.isSuccessful()){
                        Log.d("DEBUGGGGGGGGGG", "HomeFragment - getRecipeImages - API Call SUCCESS");

                        Gson gson = new Gson();

                        recipes_list.get(recipes_list.indexOf(item)).setImages(response.body().getFileUrls());

                        recipesAdapter.notifyDataSetChanged();

                    }
                }

                @Override
                public void onFailure(Call<FileResults> call, Throwable throwable) {
                    Log.d("DEBUGGGGG", "HomeFragment - getRecipeImages - API Call Failure - "+throwable.getMessage());
                }
            });
        }

    }
}