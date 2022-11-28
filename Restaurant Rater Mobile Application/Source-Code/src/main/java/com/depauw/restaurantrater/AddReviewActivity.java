package com.depauw.restaurantrater;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.depauw.restaurantrater.databinding.ActivityAddReviewBinding;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class AddReviewActivity extends AppCompatActivity implements View.OnClickListener {

    // Static Variables
    public static final String EXTRA_ADD_OBJECT = "com.depauw.restaurantrater.EXTRA_ADD_OBJECT";
    public static final String FILE_NAME = "reviews.csv";
    public static final int TWENTY_FOUR_COCK_TIME_ZONE_CHANGE = 12;
    public static final int TWENTY_FOUR_COCK_TIME_ZONE_CHANGE_INDICATOR = 13;
    public static final int TO_ADD_MINUTE_ZERO = 10;

    // Member Variables
    ActivityAddReviewBinding binding;
    private String name;
    private String date;
    private String time;
    private String meal;
    private int rating;
    private int isFavorite;
    private Calendar myCalendar;

    // Return what meal
    public String getMeal(int id){
        switch (id){
            case(R.id.radio_breakfast):{
                return "Breakfast";
            }
            case(R.id.radio_lunch):{
                return "Lunch";
            }
            case(R.id.radio_dinner):{
                return "Dinner";
            }
        }
        return null;
    }

    // Get inputted date
    private DatePickerDialog.OnDateSetListener datepicker_reviewdate_dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            date = "" + month  + "/" + dayOfMonth + "/" + year;
            binding.edittextReviewDate.setText(date);
        }
    };

    // Get inputted time
    private  TimePickerDialog.OnTimeSetListener timepicker_reviewtime_timeChangeListener = new  TimePickerDialog.OnTimeSetListener () {
        @Override
        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
            int hourToDisplay;
            String minuteToDisplay = (minute<TO_ADD_MINUTE_ZERO) ? "0" + minute : "" + minute;
            String dayZone;
            if(hourOfDay>=TWENTY_FOUR_COCK_TIME_ZONE_CHANGE_INDICATOR){
                 hourToDisplay = hourOfDay - TWENTY_FOUR_COCK_TIME_ZONE_CHANGE;
                 dayZone = "PM";
            }
            else{
                hourToDisplay = hourOfDay;
                dayZone = "AM";
            }
            time = "" + hourToDisplay + ":" + minuteToDisplay + " " + dayZone;
            binding.edittextReviewTime.setText(time);
        }
    };

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case(R.id.edittext_review_date):{
                DatePickerDialog myPicker = new DatePickerDialog(this,  datepicker_reviewdate_dateSetListener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH) );
                myPicker.show();
                break;
            }
            case(R.id.edittext_review_time):{
                TimePickerDialog myPicker = new TimePickerDialog(this,  timepicker_reviewtime_timeChangeListener, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true);
                myPicker.show();
                break;
            }
            case(R.id.button_add_review):{
                name = binding.edittextRestaurantName.getText().toString();
                meal = getMeal(binding.radiogroupMeals.getCheckedRadioButtonId());
                rating = binding.seekbarRating.getProgress();
                isFavorite = (binding.checkboxFavorite.isChecked()) ? 1 : 0;

                String addReview = name + "," + date + "," + time + "," + meal + "," + rating + "," + isFavorite;

                //Write to File
                File myFile = new File (getFilesDir(), FILE_NAME);
                try(FileWriter myWriter = new FileWriter(myFile, true))
                {
                    myWriter.write(addReview + System.lineSeparator());
                } catch (IOException e){
                    e.printStackTrace();
                }

                Intent data = new Intent();
                data.putExtra(EXTRA_ADD_OBJECT, addReview);
                setResult(Activity.RESULT_OK, data);

                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myCalendar = Calendar.getInstance();

        Intent intent = getIntent();

        binding.buttonAddReview.setOnClickListener(this);
        binding.edittextReviewTime.setOnClickListener(this);
        binding.edittextReviewDate.setOnClickListener(this);
    }
}