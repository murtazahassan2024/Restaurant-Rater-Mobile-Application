package com.depauw.restaurantrater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private List<Review> restaurants;

    public CustomAdapter(Context context, List<Review> restaurant) {
        this.context = context;
        this.restaurants = restaurant;
    }

    @Override
    public int getCount() {
        return restaurants.size();
    }

    @Override
    public Review getItem(int position) {
        return restaurants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_restaurant_rating_row, parent, false);
        }
        Review thisRestaurant = restaurants.get(position);

        TextView restaurantName = convertView.findViewById(R.id.textView_restaurant_name);
        RadioGroup restaurantMeal = convertView.findViewById(R.id.RadioGroup_restaurant_meals);
        RatingBar userFavorite = convertView.findViewById(R.id.ratingBar_user_favorite);
        ProgressBar userRating = convertView.findViewById(R.id.progressBar_user_rating);

        userFavorite.setRating(thisRestaurant.isFavorite());
        restaurantName.setText(thisRestaurant.getRestaurantName());

        if(thisRestaurant.getMeal().equals("Breakfast")){
           restaurantMeal.check(R.id.radioButton_meal_breakfast);
        }
        else if(thisRestaurant.getMeal().equals("Lunch")){
            restaurantMeal.check(R.id.radioButton_meal_lunch);
        }
        else if(thisRestaurant.getMeal().equals("Dinner")){
            restaurantMeal.check(R.id.radioButton_meal_dinner);
        }
        
        userRating.setProgress(thisRestaurant.getRating());

    return convertView;
    }

}
