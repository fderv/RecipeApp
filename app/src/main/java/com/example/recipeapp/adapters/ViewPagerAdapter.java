package com.example.recipeapp.adapters;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.recipeapp.R;
import com.example.recipeapp.listeners.MealListItemClickListener;
import com.example.recipeapp.models.Meal;

import java.io.Serializable;

public class ViewPagerAdapter extends PagerAdapter implements Serializable, Parcelable {

    private Context mContext;
    private MealListItemClickListener mListener;
    private LayoutInflater layoutInflater;
    private Meal[] mMeals;

    public ViewPagerAdapter(Context mContext, Meal[] meals, MealListItemClickListener listener) {
        this.mContext = mContext;
        mMeals = meals;
        mListener = listener;
    }

    protected ViewPagerAdapter(Parcel in) {
        mMeals = in.createTypedArray(Meal.CREATOR);
    }

    public static final Creator<ViewPagerAdapter> CREATOR = new Creator<ViewPagerAdapter>() {
        @Override
        public ViewPagerAdapter createFromParcel(Parcel in) {
            return new ViewPagerAdapter(in);
        }

        @Override
        public ViewPagerAdapter[] newArray(int size) {
            return new ViewPagerAdapter[size];
        }
    };

    @Override
    public int getCount() {
        if (mMeals == null) {
            return 0;
        }
        return mMeals.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.viewpager_item,null);

        ImageView imageView = view.findViewById(R.id.iv_viewpager_item);
        TextView textView = view.findViewById(R.id.tv_viewpager_item);

        final Meal m = mMeals[position];
        if (m != null) {
            Glide.with(mContext).load(m.getThumbnail()).into(imageView);
            textView.setText(m.getTitle());

        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onMealItemClick(m);
            }
        });

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }

    public void setMealData(Meal[] meals) {

        mMeals = meals;
        notifyDataSetChanged();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedArray(mMeals, i);
    }
}