package com.example.recipeapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipeapp.R;
import com.example.recipeapp.listeners.MealListItemClickListener;
import com.example.recipeapp.models.Meal;

import java.io.Serializable;
import java.util.ArrayList;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> implements Serializable {

    private int mItemCount;
    private ArrayList<Meal> mMealData;
    private MealListItemClickListener mOnClickListener;
    private Context mContext;

    public MealAdapter(ArrayList<Meal> mealData, MealListItemClickListener onClickListener, Context context) {
        mItemCount = mealData.size();
        mMealData = mealData;
        mOnClickListener = onClickListener;
        mContext = context;
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.meal_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        MealViewHolder viewHolder = new MealViewHolder(view, mOnClickListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        holder.bind(mMealData.get(position));
    }

    @Override
    public int getItemCount() { return mItemCount; }

    public void setMealData(ArrayList<Meal> meals) {

        mMealData = meals;
        mItemCount = meals.size();
        notifyDataSetChanged();
    }

    /**
     *
     */
    class MealViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        private  MealListItemClickListener mListener;
        TextView mealTitleTV;
        ImageView mealThumbIV;

        public MealViewHolder(@NonNull View itemView, MealListItemClickListener listener) {
            super(itemView);

            mListener = listener;
            itemView.setOnClickListener(this);

            mealTitleTV = itemView.findViewById(R.id.tv_meal_item);
            mealThumbIV = itemView.findViewById(R.id.iv_meal_item);
        }

        public void bind(Meal meal) {
            mealTitleTV.setText(meal.getTitle());
            String mealImageUrl = meal.getThumbnail();
            Glide.with(mContext).load(mealImageUrl).into(mealThumbIV);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mListener.onMealItemClick(mMealData.get(clickedPosition));
        }
    }
}
