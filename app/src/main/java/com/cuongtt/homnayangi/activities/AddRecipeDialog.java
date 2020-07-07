package com.cuongtt.homnayangi.activities;

import android.graphics.Canvas;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.cuongtt.homnayangi.R;
import com.cuongtt.homnayangi.adapters.IngredientsAdapter;
import com.cuongtt.homnayangi.adapters.InstructionsAdapter;
import com.cuongtt.homnayangi.models.Ingredients;
import com.cuongtt.homnayangi.models.Recipes;
import com.cuongtt.homnayangi.models.RecipesIngredients;
import com.cuongtt.homnayangi.models.RecipesIngredientsSimplify;
import com.cuongtt.homnayangi.network.APIService;
import com.cuongtt.homnayangi.network.ApiUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRecipeDialog extends DialogFragment {

    TextInputEditText edtTitle, edtSummary, edtServing, edtReadyIn;
    RecyclerView recyclerIngredients, recyclerInstruction;
    Button btnAddIngredient, btnAddInstruction, btnAddImage;
    CheckBox chDishType_morning, chDishType_noon, chDishType_night;

    IngredientsAdapter ingredientsAdapter;
    InstructionsAdapter instructionsAdapter;

    Ingredients ingredientItem;
    RecipesIngredients recipesIngredientsItem;
    ArrayList<RecipesIngredients> ListIngredientsOfRecipe = new ArrayList<RecipesIngredients>();
    ArrayList<String> ListInstructions = new ArrayList<String>();

    Toolbar toolbar;

    String dishType;


    public static AddRecipeDialog newInstance() {
        return new AddRecipeDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_layout_add_recipe, container, false);

        addControls(view);

        btnAddInstruction.setOnClickListener(addIntsructionListener);
        btnAddIngredient.setOnClickListener(addIngredientListener);


        ingredientsAdapter = new IngredientsAdapter(ListIngredientsOfRecipe, view.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerIngredients.setItemAnimator(new DefaultItemAnimator());
        recyclerIngredients.setLayoutManager(linearLayoutManager);
        recyclerIngredients.setHasFixedSize(true);
        recyclerIngredients.setAdapter(ingredientsAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerIngredients);


        instructionsAdapter = new InstructionsAdapter(ListInstructions, view.getContext());
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(view.getContext());
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerInstruction.setItemAnimator(new DefaultItemAnimator());
        recyclerInstruction.setLayoutManager(linearLayoutManager1);
        recyclerInstruction.setHasFixedSize(true);
        recyclerInstruction.setAdapter(instructionsAdapter);

        ItemTouchHelper itemTouchHelper1 = new ItemTouchHelper(simpleCallback1);
        itemTouchHelper1.attachToRecyclerView(recyclerInstruction);


        toolbar.setTitle("Thêm công thức");

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }
        setHasOptionsMenu(true);


        return view;
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_dialog, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mnu_dialog_save) {
            System.out.println("SAVE CLICK");
            CreateRecipe();
            return true;
        } else if (id == android.R.id.home) {
            AddRecipeDialog.this.dismiss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction)  {
                case ItemTouchHelper.LEFT:
                case ItemTouchHelper.RIGHT:
                    ListIngredientsOfRecipe.remove(position);
                    ingredientsAdapter.notifyItemRemoved(position);
                    ingredientsAdapter.notifyDataSetChanged();
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(getActivity(), R.color.md_red_500))
                    .addActionIcon(R.drawable.ic_trash_2_20px)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        }
    };

    ItemTouchHelper.SimpleCallback simpleCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction)  {
                case ItemTouchHelper.LEFT:
                case ItemTouchHelper.RIGHT:
                    ListInstructions.remove(position);
                    instructionsAdapter.notifyItemRemoved(position);
                    instructionsAdapter.notifyDataSetChanged();
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(getActivity(), R.color.md_red_500))
                    .addActionIcon(R.drawable.ic_trash_2_20px)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        }
    };


    View.OnClickListener addIntsructionListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AddInstructionDialog dialog = new AddInstructionDialog();
            dialog.show(getFragmentManager(), "instruction dialog");
            dialog.setDialogResult(new AddInstructionDialog.OnMyDialogResult() {
                @Override
                public void finish(String result) {
                    String Instruction = result;
                    if(!Instruction.isEmpty()){
                        ListInstructions.add(Instruction);
                        instructionsAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    };

    View.OnClickListener addIngredientListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            recipesIngredientsItem = new RecipesIngredients();
            AddIngredientDialog dialog = new AddIngredientDialog();
            dialog.show(getFragmentManager(), "Ingredient dialog");
            dialog.setDialogResult(new AddIngredientDialog.OnMyDialogResult() {
                @Override
                public void finish(String result) {
                    System.out.println(result);
                    Gson gson = new Gson();
                    recipesIngredientsItem = gson.fromJson(result, RecipesIngredients.class);

                    if(checkItem(ListIngredientsOfRecipe, recipesIngredientsItem)){
                        System.out.println("New ingredient has in array");
                        int index = ListIngredientsOfRecipe.indexOf(recyclerIngredients);
                        ListIngredientsOfRecipe.set(index, recipesIngredientsItem);
                    }
                    System.out.println("New ingredient NOT HAVE IN array");
                    ListIngredientsOfRecipe.add(recipesIngredientsItem);

                    ingredientsAdapter.notifyDataSetChanged();

                }
            });
        }
    };

    public boolean checkItem(ArrayList<RecipesIngredients> list, RecipesIngredients checkValue){

        System.out.println(checkValue.toString());
        for(RecipesIngredients item : list){
            System.out.println(item.toString());
            if(item.getIngredient().getId() == checkValue.getIngredient().getId()){
                return true;
            }
        }
        return false;
    }

    private void addControls(View view) {
        edtTitle = view.findViewById(R.id.edt_recipe_title);
        edtSummary = view.findViewById(R.id.edt_recipe_summary);
        edtServing = view.findViewById(R.id.edt_recipe_servings);
        edtReadyIn = view.findViewById(R.id.edt_recipe_readyIn);

        chDishType_morning = view.findViewById(R.id.cbDishType_Morning);
        chDishType_noon = view.findViewById(R.id.cbDishType_Noon);
        chDishType_night = view.findViewById(R.id.cbDishType_night);
        
        btnAddIngredient = view.findViewById(R.id.btn_recipe_AddIngredients);
        btnAddInstruction = view.findViewById(R.id.btn_recipe_AddInstructions);

        recyclerIngredients = view.findViewById(R.id.rcyIngredients);
        recyclerInstruction = view.findViewById(R.id.rcyInstructions);

        edtSummary.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        toolbar = view.findViewById(R.id.dialog_recipe_toolbar);

    }

    APIService mAPIService = ApiUtils.getAPIService();


    public void CreateRecipe(){

        System.out.println("Chạy nè");

        String title, summary, dishType = null;
        Integer serving, readyInMinutes;

        if(chDishType_morning.isChecked()){
            if(dishType.isEmpty()) dishType = "Sáng";
        }
        if(chDishType_noon.isChecked()){
            dishType += "/Trưa";
        }
        if(chDishType_night.isChecked()){
            dishType += "Tối";
        }

        title = edtTitle.getText().toString();
        summary = edtSummary.getText().toString();

        serving = Integer.parseInt(edtServing.getText().toString());
        readyInMinutes = Integer.parseInt(edtReadyIn.getText().toString());


        JsonArray instructionArray = new Gson().toJsonTree(ListInstructions).getAsJsonArray();


        JsonObject jsonDataNewRecipe = new JsonObject();
        jsonDataNewRecipe.addProperty("title", title);
        jsonDataNewRecipe.addProperty("servings", serving);
        jsonDataNewRecipe.addProperty("readyInMinutes", readyInMinutes);
        jsonDataNewRecipe.addProperty("summary", summary);
        jsonDataNewRecipe.addProperty("dish_types", dishType);
        jsonDataNewRecipe.add("inductions", instructionArray);


        ArrayList<RecipesIngredientsSimplify> ingredients = new ArrayList<RecipesIngredientsSimplify>();
        RecipesIngredientsSimplify ingredient;
        for(RecipesIngredients item : ListIngredientsOfRecipe){
            ingredient = new RecipesIngredientsSimplify(item.getAmount(), item.getUnit(), item.getIngredient().getId());
            ingredients.add(ingredient);
        }
        final JsonArray ingredientsArray = new Gson().toJsonTree(ingredients).getAsJsonArray();

        if(title.isEmpty()){
            edtTitle.setError("Tên món không được trống");
        }else if(summary.isEmpty()){
            edtSummary.setError("Mô tả món ăn không được trống");
        }

        mAPIService.createRecipes(jsonDataNewRecipe).enqueue(new Callback<Recipes>() {
            @Override
            public void onResponse(Call<Recipes> call, Response<Recipes> response) {
                    if(response.isSuccessful()){
                        Recipes  newRecipe = new Recipes();
                        newRecipe = response.body();

                        System.out.println("Mã recipe mới: " + newRecipe.getId());

                        JsonObject jsonUpdateIngredientOfRecipe = new JsonObject();
                        jsonUpdateIngredientOfRecipe.add("recipe_ingredients", ingredientsArray);


                    }
            }

            @Override
            public void onFailure(Call<Recipes> call, Throwable throwable) {

            }
        });
    }
}
