package com.depauw.restaurantrater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import com.depauw.restaurantrater.databinding.ActivityViewReviewsBinding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ViewReviewsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    // Static Variables
    public static final int FROM_ADD_REVIEW_ACTIVITY = 1;
    public static final String FILE_NAME = "reviews.csv";

    // Member Variables
    private ActivityViewReviewsBinding binding;
    private ArrayList<Review> myList;
    CustomAdapter adapter;


    // Add specific lines to list
    public void addToList(@NonNull String line, @NonNull ArrayList<Review> list) {
        String[] reviewData = line.split(",");
        Review addObject = new Review(reviewData[0], reviewData[1], reviewData[2], reviewData[3], Integer.valueOf(reviewData[4]), Integer.valueOf(reviewData[5]));
        list.add(addObject);
    }

    // Scan the entire list
    public void readFile() {
        File myFile = new File(getFilesDir(), FILE_NAME);
        try (Scanner myScanner = new Scanner(myFile)) {
            while (myScanner.hasNextLine()) {
                String line = myScanner.nextLine();
                addToList(line, myList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        adapter = new CustomAdapter(this, myList);
        binding.listviewReviews.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewReviewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myList = new ArrayList<>();
        readFile();

        binding.listviewReviews.setOnItemClickListener(this);
        binding.buttonAddReview.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        Review thisRestaurant = myList.get(position);

        String date = thisRestaurant.getDate();
        String time = thisRestaurant.getTime();

        AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
        myBuilder.setTitle(R.string.alertTitle)
                .setMessage("This review was created on " + date + " at " + time);
        AlertDialog myDialog = myBuilder.create();
        myDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.button_add_review): {
                Intent myIntent = new Intent(this, AddReviewActivity.class);
                startActivityForResult(myIntent, FROM_ADD_REVIEW_ACTIVITY);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        if(requestCode == FROM_ADD_REVIEW_ACTIVITY && responseCode == Activity.RESULT_OK){
            String addReview = data.getStringExtra(AddReviewActivity.EXTRA_ADD_OBJECT);
            addToList(addReview, myList);
            adapter.notifyDataSetChanged();
        }
    }
}