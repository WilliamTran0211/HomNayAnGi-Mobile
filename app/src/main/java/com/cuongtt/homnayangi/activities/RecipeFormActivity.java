package com.cuongtt.homnayangi.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cuongtt.homnayangi.R;
import com.cuongtt.homnayangi.models.Ingredients;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecipeFormActivity extends AppCompatActivity {

    private TextInputLayout txtTile, txtSummary;
    private EditText txtServings, txtMinute;
    private ListView list_ingredients, list_intruction;
    private TextView txtAddIngredients, txtAddIntructions;

    Ingredients ingredientItem;
    ArrayList<Ingredients> ListIngredientsOfRecipe = new ArrayList<Ingredients>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_form);

        addControl();
        addEvent();
    }

    private void addEvent() {
        txtAddIntructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        txtAddIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openIngredientsDialog();
            }


        });
    }

    private void openIngredientsDialog() {
        ingredientItem = new Ingredients();
        AddIngredientDialog dialog = new AddIngredientDialog();
        dialog.show(getSupportFragmentManager(), "OPEN dialog");
        dialog.setDialogResult(new AddIngredientDialog.OnMyDialogResult() {
            @Override
            public void finish(String result) {
                System.out.println(result);
                Gson gson = new Gson();
                ingredientItem = gson.fromJson(result, Ingredients.class);
                ListIngredientsOfRecipe.add(ingredientItem);
            }
        });
    }

    private void addControl() {
        txtTile = findViewById(R.id.edt_title);
        txtSummary = findViewById(R.id.edt_summary);
        txtServings = findViewById(R.id.edt_serving);
        txtMinute = findViewById(R.id.edt_minute);

        list_ingredients = findViewById(R.id.list_ingredients);
        list_intruction = findViewById(R.id.list_intruction);

        txtAddIngredients = findViewById(R.id.textview_add_ingredients);
        txtAddIntructions = findViewById(R.id.textview_add_intruction);
    }
}