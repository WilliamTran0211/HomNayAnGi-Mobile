package com.cuongtt.homnayangi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cuongtt.homnayangi.R;
import com.cuongtt.homnayangi.models.Recipes;
import com.cuongtt.homnayangi.models.RecipesIngredients;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsItemViewHolder> {

    private List<RecipesIngredients> ingredientsList;
    private Context context;

    public IngredientsAdapter(List<RecipesIngredients> ingredientsList, Context context) {
        this.ingredientsList = ingredientsList;
        this.context = context;
    }

    @NonNull
    @Override
    public IngredientsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredients, parent, false);
        return new IngredientsAdapter.IngredientsItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsItemViewHolder holder, int position) {

        RecipesIngredients ingredient = ingredientsList.get(holder.getAdapterPosition());

        String ingredient_name = ingredient.getIngredient().getName();
        String amount = ingredient.getAmount() +" "+ ingredient.getUnit();

        holder.tv_ingredientName.setText(ingredient_name);
        holder.tv_amount.setText(amount);

    }

    @Override
    public int getItemCount() {
        return ingredientsList.size();
    }

    public static class IngredientsItemViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_ingredientName;
        public TextView tv_amount;

        public IngredientsItemViewHolder(View itemView) {
            super(itemView);

            tv_ingredientName = itemView.findViewById(R.id.tv_ingredientName);
            tv_amount = itemView.findViewById(R.id.tv_amount);

        }
    }
}
