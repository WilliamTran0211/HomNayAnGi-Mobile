package com.cuongtt.homnayangi.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cuongtt.homnayangi.R;
import com.cuongtt.homnayangi.models.Food;
import com.cuongtt.homnayangi.models.RecipesIngredients;
import com.cuongtt.homnayangi.network.APIService;
import com.cuongtt.homnayangi.network.ApiUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodItemViewHolder>  {

    private List<Food> foodList;
    private Context context;

    public FoodAdapter(List<Food> ingredientsList, Context context) {
        this.foodList = ingredientsList;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodAdapter.FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodAdapter.FoodItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodAdapter.FoodItemViewHolder holder, int position) {

        final Food item = foodList.get(holder.getAdapterPosition());

//        holder.checkBox.setVisibility(View.INVISIBLE);

        String ingredient_name = item.getFood().getName().toString();
        String amount = item.getAmount().toString();
        String unit = item.getUnit();

        holder.tv_ingredientName.setText(ingredient_name);
        holder.tv_amount.setText(amount);
        holder.tv_unit.setText(unit);

        holder.tv_ingredientName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                PopupMenu mnu = new PopupMenu(view.getContext(),view);
                mnu.inflate(R.menu.menu_popup);

                mnu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()){
                            case R.id.mnu_popup_over:
                                deleteFood(item.getId(), view);
                                break;
                            case R.id.mnu_popup_update:
                                updateFood();
                                break;
                        }
                        return false;
                    }
                });

                mnu.show();

                return false;
            }
        });
    }


    APIService mAPIService = ApiUtils.getAPIService();

    public void deleteFood(String foodID, final View view){
        Log.d("DEBUGGGGG", "popup menu click delete");
        mAPIService.deleteFood(foodID).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    Snackbar.make(view, "Thực phẩm không còn trong kho.", Snackbar.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }else{
                    Snackbar.make(view, "Lỗi, thử lại sau", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {

            }
        });
    }

    public void updateFood(){
        Log.d("DEBUGGGGG", "popup menu click update");

    }


    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodItemViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_ingredientName;
        public TextView tv_amount;
        public TextView tv_unit;
        public CheckBox checkBox;
        public LinearLayout rowFood;


        public FoodItemViewHolder(View itemView) {
            super(itemView);

            tv_ingredientName = itemView.findViewById(R.id.txt_food_name);
            tv_amount = itemView.findViewById(R.id.txt_food_amount);
            tv_unit = itemView.findViewById(R.id.txt_food_unit);
            checkBox = itemView.findViewById(R.id.checkbox_isChecked);
            rowFood = itemView.findViewById(R.id.rowFood);
        }
    }
}
