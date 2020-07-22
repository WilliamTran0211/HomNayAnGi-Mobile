package com.cuongtt.homnayangi.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cuongtt.homnayangi.R;
import com.cuongtt.homnayangi.activities.AddFoodDialog;
import com.cuongtt.homnayangi.adapters.FoodAdapter;
import com.cuongtt.homnayangi.models.Food;
import com.cuongtt.homnayangi.models.FoodByDay;
import com.cuongtt.homnayangi.models.Recipes;
import com.cuongtt.homnayangi.network.APIService;
import com.cuongtt.homnayangi.network.ApiUtils;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class ContainerFragment extends Fragment {

    APIService mAPIService = ApiUtils.getAPIService();
    ArrayList<Food> myContainer;

    ExtendedFloatingActionButton fab;
    RecyclerView foodRecycler;

    FoodAdapter adapter;

    SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");

    public ContainerFragment() {
        // Required empty public constructor
    }


    public static ContainerFragment newInstance(String param1, String param2) {
        ContainerFragment fragment = new ContainerFragment();
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
        View view = inflater.inflate(R.layout.fragment_user_container, container, false);

        SharedPreferences prefs = getContext().getSharedPreferences("MyUserInfo", MODE_PRIVATE);
        final String UserID = prefs.getString("UserId", "No id defined");
        final String email = prefs.getString("UserEmail", "No email defined");
        final String fullName = prefs.getString("UserFullName", "No name defined");


        foodRecycler = view.findViewById(R.id.foodRecycler);
        myContainer = new ArrayList<Food>();
        adapter = new FoodAdapter(myContainer, getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);


        foodRecycler.setItemAnimator(new DefaultItemAnimator());
        foodRecycler.setLayoutManager(linearLayoutManager);
        foodRecycler.setHasFixedSize(true);
        foodRecycler.setAdapter(adapter);

        fab = getActivity().findViewById(R.id.floating_button);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("DEBUGGGGGGGG", "ONCLICK - AddFood - FAB");
                AddFoodDialog dialog = new AddFoodDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "Food  dialog");

                dialog.setDialogResult(new AddFoodDialog.OnMyDialogResult() {
                    @Override
                    public void finish(String result) {
                        if(result.equals("success")){
                            fletchContainer(UserID);
                        }
                    }
                });

            }
        });

        fletchContainer(UserID);
        adapter.notifyDataSetChanged();

        return view;
    }


    public void fletchContainer(String userID){
        mAPIService.getUserFood(userID).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Gson gson = new Gson();
                if(response.isSuccessful()){
                    Log.d("DEBUGGGGGGGGGGGGG", "onResponse: Get all food successfully");

                    JsonArray myRes = response.body();

                    Type foodListType  = new TypeToken<ArrayList<Food>>(){}.getType();

                    ArrayList<Food> items  = gson.fromJson(response.body().toString(), foodListType);

                    ArrayList<FoodByDay> foodByDays = new  ArrayList<FoodByDay>();
                    FoodByDay itemFood = new FoodByDay();;

                    try {
                        Date itemDate = sdformat.parse(sdformat.format(items.get(0).getUpdated_at()));
                        itemFood.setDate(itemDate);

                        for(Food item : items){


                        }


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }




                    myContainer.clear();
                    myContainer.addAll(items);
                    System.out.println(myContainer.size());

                    adapter.notifyDataSetChanged();

                }else{
                    Log.d("DEBUGGGGGGGGGGGGG", "onResponse: Get all food fail - "+response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable throwable) {
                Log.d("DEBUGGGGGGGGGGGGG", "onResponse: Get all food call api fail");
            }
        });
    }
}