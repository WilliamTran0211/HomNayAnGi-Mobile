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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeItemViewHolder>  {

    private List<Recipes> recipesList;
    private Context context;

    public RecipesAdapter(List<Recipes> recipesList, Context context) {
        this.recipesList = recipesList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecipeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipes, parent, false);
        return new RecipeItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecipeItemViewHolder holder, int position) {

        final Recipes recipes = recipesList.get(holder.getAdapterPosition() );
        holder.tv_recipes_title.setText(recipes.getTitle());
        holder.tv_recipes_summary.setText(recipes.getSummary());


//        Picasso.get().load(recipes.getImages().get(0)).into(holder.img_recipes);

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

//    @Override
//    public Filter getFilter() {
//        return myFilter;
//    }
//
//    private Filter myFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence charSequence) {
//            ArrayList<Recipes> filterList = new ArrayList<>();
//            if(charSequence == null || charSequence.length() == 0){
//                filterList.addAll(recipesList);
//            } else{
//                String filterPattern = charSequence.toString().toLowerCase().trim();
//
//                for(Recipes item : recipesList){
//                    if(item.getTitle().toLowerCase().contains(filterPattern)){
//                        filterList.add(item);
//                    }
//                }
//            }
//            FilterResults results = new FilterResults();
//            results.values = filterList;
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//            recipesList.clear();
//            recipesList.addAll((ArrayList) filterResults.values);
//            notifyDataSetChanged();
//        }
//    };

    public static class RecipeItemViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        public ImageView img_recipes;
        public TextView tv_recipes_title;
        public TextView tv_recipes_summary;
        public RatingBar rating;

        public RecipeItemViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            img_recipes = itemView.findViewById(R.id.img_recipe);
            tv_recipes_title = itemView.findViewById(R.id.tv_title);
            tv_recipes_summary = itemView.findViewById(R.id.tv_summary);
//            rating = itemView.findViewById(R.id.rating);

        }
    }
}

