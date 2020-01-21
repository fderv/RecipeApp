package com.example.recipeapp.adapters;

import android.content.Context;
import android.database.Cursor;
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
import com.example.recipeapp.models.Recipe;
import com.example.recipeapp.utilities.OtherUtils;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.RecipeViewHolder> {

    private Cursor mCursor;

    private int mItemCount;
    private MealListItemClickListener mOnClickListener;
    private Context mContext;

    public FavAdapter(MealListItemClickListener mOnClickListener, Context mContext) {
        mItemCount = 0;
        this.mOnClickListener = mOnClickListener;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.meal_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        RecipeViewHolder viewHolder = new RecipeViewHolder(view, mOnClickListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.bind(new Recipe(mCursor.getString(0), mCursor.getString(1), mCursor.getString(2),
                mCursor.getString(3), mCursor.getString(4), mCursor.getString(5),
                OtherUtils.convertStringToArray(mCursor.getString(6)),
                OtherUtils.convertStringToArray(mCursor.getString(7))));
    }


    @Override
    public int getItemCount() {
        return mItemCount;
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        if (newCursor != null)
            mItemCount = mCursor.getCount();
        notifyDataSetChanged();
    }


    class RecipeViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private MealListItemClickListener mListener;
        TextView mealTitleTV;
        ImageView mealThumbIV;

        public RecipeViewHolder(@NonNull View itemView, MealListItemClickListener listener) {
            super(itemView);

            mListener = listener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            mealTitleTV = itemView.findViewById(R.id.tv_meal_item);
            mealThumbIV = itemView.findViewById(R.id.iv_meal_item);
        }

        public void bind(Recipe recipe) {

            mealTitleTV.setText(recipe.getTitle());
            String mealImageUrl = recipe.getThumbnail();
            Glide.with(mContext).load(mealImageUrl).into(mealThumbIV);

        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mCursor.moveToPosition(clickedPosition);
            mListener.onMealItemClick(new Meal(mCursor.getString(0), mCursor.getString(1), mCursor.getString(2)));
        }

        @Override
        public boolean onLongClick(View view) {
            mCursor.moveToPosition(getAdapterPosition());
            mListener.onMealItemLongClick(mCursor.getString(0));
            return true;
        }
    }
}
