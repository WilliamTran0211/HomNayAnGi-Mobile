package com.cuongtt.homnayangi.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cuongtt.homnayangi.R;
import com.cuongtt.homnayangi.Utils;
import com.cuongtt.homnayangi.activities.RecipeDetailActivity;
import com.cuongtt.homnayangi.models.Recipes;


import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.RecipeItemSearchViewHolder> implements Filterable {

    private List<Recipes> recipesList;
    private List<Recipes> recipesListFull;
    private Context context;

    public SearchAdapter(List<Recipes> recipesList, List<Recipes> recipesFullList,Context context) {
        this.recipesList = recipesList;
        this.context = context;
        this.recipesListFull = recipesFullList;
    }

    @NonNull
    @Override
    public RecipeItemSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_search, parent, false);
        return new RecipeItemSearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecipeItemSearchViewHolder holder, int position) {
        final Recipes recipes = recipesList.get(holder.getAdapterPosition() );

        holder.tv_recipes_title.setText(recipes.getTitle());
        holder.tv_recipes_summary.setText(recipes.getSummary());

        Glide.with(context)
                .load(recipes.getImages().get(0))
                .fitCenter()
                .into(holder.img_recipes);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_detail = new Intent(context, RecipeDetailActivity.class);

                Bundle bundle = new Bundle();

                Recipes r = recipesList.get(holder.getAdapterPosition() );

                bundle.putString("recipe_info_id", r.getId().toString());

                intent_detail.putExtras(bundle);

                String Recipe_Detail = Utils.getGsonParser().toJson(recipes);
                intent_detail.putExtra("Recipe_Detail", Recipe_Detail);

                context.startActivity(intent_detail);
            }
        });


    }

    @Override
    public int getItemCount() {
        return recipesList.size();
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }


    private Filter myFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Recipes> filterList = new ArrayList<>();
//            recipesListFull.clear();
//            recipesListFull.addAll(recipesListFull);
            System.out.println("SEARCH KEY = "+charSequence.toString());
            System.out.println("Size full "+ recipesListFull.size());
            System.out.println("Size filter "+recipesList.size());

            if(charSequence == null || charSequence.length() == 0){
                System.out.println("Not filter");
                filterList.addAll(recipesListFull);
            } else{
                System.out.println("has filter");
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(Recipes item : recipesListFull){
                    if(item.getTitle().toLowerCase().contains(filterPattern)){
                        filterList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            recipesList.clear();
            recipesList.addAll((ArrayList) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public static class RecipeItemSearchViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public ImageView img_recipes;
        public TextView tv_recipes_title;
        public TextView tv_recipes_summary;

        public RecipeItemSearchViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardViewSearch);
            img_recipes = itemView.findViewById(R.id.img_recipe_search);
            tv_recipes_title = itemView.findViewById(R.id.tv_title_search);
            tv_recipes_summary = itemView.findViewById(R.id.tv_summary_search);

        }
    }
}
