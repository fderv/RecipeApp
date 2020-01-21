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
import com.example.recipeapp.listeners.CategoryListItemClickListener;
import com.example.recipeapp.R;
import com.example.recipeapp.models.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Cursor mCursor;

    private int mItemCount;
    private ArrayList<Category> mCategoryData;
    private CategoryListItemClickListener mOnClickListener;
    private Context mContext;

    public CategoryAdapter(CategoryListItemClickListener listener, Context context) {
        mItemCount = 0;
        mOnClickListener = listener;
        mContext = context;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.category_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        CategoryViewHolder viewHolder = new CategoryViewHolder(view, mOnClickListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        mCursor.moveToPosition(position);
        holder.bind(new Category(mCursor.getString(0), mCursor.getString(1), mCursor.getString(2), mCursor.getString(3)));
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        mItemCount = mCursor.getCount();
        notifyDataSetChanged();
    }

    /**
     *
     */
    class CategoryViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        private  CategoryListItemClickListener mListener;
        TextView categoryNameTV;
        ImageView categoryThumbIV;

        public CategoryViewHolder(@NonNull View itemView, CategoryListItemClickListener listener) {
            super(itemView);

            mListener = listener;
            itemView.setOnClickListener(this);

            categoryNameTV = itemView.findViewById(R.id.tv_category_item);
            categoryThumbIV = itemView.findViewById(R.id.iv_category_item);
        }

        public void bind(final Category category) {

            categoryNameTV.setText(category.getTitle());
            String categoryImageUrl = category.getThumbnail();
            Glide.with(mContext).load(categoryImageUrl).into(categoryThumbIV);

        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mCursor.moveToPosition(clickedPosition);

            mListener.onCategoryItemClick(new Category(mCursor.getString(0), mCursor.getString(1), mCursor.getString(2), mCursor.getString(3)));

        }
    }
}
