package com.cuongtt.homnayangi.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.cuongtt.homnayangi.R;
import com.cuongtt.homnayangi.adapters.RecipesAdapter;
import com.cuongtt.homnayangi.adapters.SearchAdapter;
import com.cuongtt.homnayangi.models.FileResults;
import com.cuongtt.homnayangi.models.Recipes;
import com.cuongtt.homnayangi.network.APIService;
import com.cuongtt.homnayangi.network.ApiUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private SearchAdapter searchAdapter;
    private ArrayList<Recipes> recipes_list;
    private ArrayList<Recipes> recipes_full_list;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView =  view.findViewById(R.id.rcySearch);
        searchView = view.findViewById(R.id.searchBar);


        recipes_list = new ArrayList<Recipes>();
        recipes_full_list = new ArrayList<Recipes>();

        fletchRecipesItems();
        ArrayList<Recipes> test = new ArrayList<>(recipes_list);

        searchAdapter = new SearchAdapter(recipes_list, recipes_full_list,getActivity());


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(searchAdapter);


        searchAdapter.notifyDataSetChanged();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchAdapter.getFilter().filter(s);
                return false;
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
                    Log.d("DEBUGGGGGGGGGG", "Search Fragment - getAllRecipesList - API Call SUCCESS");

                    Gson gson = new Gson();

                    Type recipesListType  = new TypeToken<ArrayList<Recipes>>(){}.getType();

                    ArrayList<Recipes> items = gson.fromJson(response.body().toString(), recipesListType);



                    recipes_list.clear();
                    recipes_list.addAll(items);
                    recipes_full_list.clear();
                    recipes_full_list.addAll(items);


                    getRecipesImages();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable throwable) {
                Log.d("DEBUGGGGG", " Search Fragment - getAllRecipesList - API Call Failure - "+throwable.getMessage());
            }
        });
    }

    private void getRecipesImages(){
        System.out.println("run here getRecipesImages - " + recipes_list.toString());
        for(final Recipes item : recipes_list){
            String imgRecipeName = "";

            if(item.getImages() != null){
                imgRecipeName = item.getImages().get(0);
            }

            String id = item.getId().toString();
            System.out.println("run here getRecipesImages - " +id + " - "+ imgRecipeName);
            mAPIService.getRecipeImages(id).enqueue(new Callback<FileResults>() {
                @Override
                public void onResponse(Call<FileResults> call, Response<FileResults> response) {
                    if(response.isSuccessful()){
                        Log.d("DEBUGGGGGGGGGG", "SearchFragment - getRecipeImages - API Call SUCCESS");

                        Gson gson = new Gson();

                        recipes_list.get(recipes_list.indexOf(item)).setImages(response.body().getFileUrls());
                        recipes_full_list.get(recipes_full_list.indexOf(item)).setImages(response.body().getFileUrls());

                        searchAdapter.notifyDataSetChanged();

                    }
                }

                @Override
                public void onFailure(Call<FileResults> call, Throwable throwable) {
                    Log.d("DEBUGGGGG", "SearchFragment - getRecipeImages - API Call Failure - "+throwable.getMessage());
                }
            });
        }

    }
}