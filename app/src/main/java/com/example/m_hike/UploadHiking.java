package com.example.m_hike;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UploadHiking extends AppCompatActivity {

    Button saveButton, cancelBtn;
    EditText eTName, eTLocation, eTDate, eTParkingAvailable, eTLengthOfHike, eTDifficultLevel, eTDescription;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        eTName = findViewById(R.id.eT_Name);
        eTLocation = findViewById(R.id.eT_Location);
        eTDate = findViewById(R.id.eT_Date);
        eTParkingAvailable = findViewById(R.id.eT_Parking);
        eTLengthOfHike = findViewById(R.id.eT_LengthOfHike);
        eTDifficultLevel = findViewById(R.id.eT_DifficultLevel);
        eTDescription = findViewById(R.id.eT_Description);
        saveButton = findViewById(R.id.saveButton);
        Button cancelBtn = findViewById(R.id.CancelButton);

        // Initialize dbHelper
        dbHelper = new DbHelper(this);

        // Set the current date and time to the "editTextDate" field
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm a", Locale.getDefault());
        String currentDateTime = dateTimeFormat.format(new Date());
        eTDate.setText(currentDateTime);

        // Initialize the DatePickerDialog with the current date
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, null, currentYear, currentMonth, currentDay
        );

        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Set the date in a Calendar instance
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                // Initialize a TimePickerDialog for selecting the time
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        UploadHiking.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selectedDate.set(Calendar.MINUTE, minute);

                                // Format and set the selected date and time to the "editTextDate" field
                                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
                                String selectedDateTime = dateTimeFormat.format(selectedDate.getTime());
                                eTDate.setText(selectedDateTime);
                            }
                        },
                        currentHour,
                        currentMinute,
                        false
                );
                timePickerDialog.show();
            }
        });

        eTDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the DatePicker when the EditText is clicked
                datePickerDialog.show();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // This will simulate the system's back button press
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    public void saveData() {
        TextInputLayout textInputLayoutName = findViewById(R.id.textInputLayoutName);
        TextInputLayout textInputLayoutLocation = findViewById(R.id.textInputLayoutLocation);
        TextInputLayout textInputLayoutParking = findViewById(R.id.textInputLayoutParking);
        TextInputLayout textInputLayoutLengthOfHike = findViewById(R.id.textInputLayoutLengthOfHike);
        TextInputLayout textInputLayoutDifficultLevel = findViewById(R.id.textInputLayoutDifficultLevel);
        TextInputLayout textInputLayoutDescription = findViewById(R.id.textInputLayoutDescription);
        // Hide error messages and clear data in EditText
        textInputLayoutName.setErrorEnabled(false); // Turn off error display
        textInputLayoutLocation.setErrorEnabled(false); // Turn off error display

        String name = eTName.getText().toString().trim();
        String location = eTLocation.getText().toString().trim();
        String date = eTDate.getText().toString().trim();
        String parkingAvailable = eTParkingAvailable.getText().toString().trim();
        String lengthOfHike = eTLengthOfHike.getText().toString().trim();
        String difficultLevel = eTDifficultLevel.getText().toString().trim();
        String description = eTDescription.getText().toString().trim();

        // Check and display an error message if required fields are invalid
        boolean isValid = true;

        if (name.isEmpty()) {
            textInputLayoutName.setError("Name is required"); // Show err message
            isValid = false;
        } else {
            textInputLayoutName.setError(null); // Clear error message if field is valid
        }

        if (location.isEmpty()) {
            textInputLayoutLocation.setError("Location is required");  // Show err message
            isValid = false;
        } else {
            textInputLayoutLocation.setError(null); // Clear error message if field is valid
        }

        if (parkingAvailable.isEmpty()) {
            textInputLayoutParking.setError("Parking is required");  // Show err message
            isValid = false;
        } else {
            textInputLayoutParking.setError(null); // Clear error message if field is valid
        }

        if (lengthOfHike.isEmpty()) {
            textInputLayoutLengthOfHike.setError("Length of hike is required");  // Show err message
            isValid = false;
        } else {
            textInputLayoutLengthOfHike.setError(null); // Clear error message if field is valid
        }

        if (difficultLevel.isEmpty()) {
            textInputLayoutDifficultLevel.setError("Difficult level is required");  // Show err message
            isValid = false;
        } else {
            textInputLayoutDifficultLevel.setError(null); // Clear error message if field is valid
        }

        if (description.isEmpty()) {
            textInputLayoutDescription.setError("Description is required");  // Show err message
            isValid = false;
        } else {
            textInputLayoutDescription.setError(null); // Clear error message if field is valid
        }

        if (!isValid) {
            // Display an error message and do not save data if there is an error
            Toast.makeText(UploadHiking.this, "Please complete all information", Toast.LENGTH_SHORT).show();
            return; // Do not perform update if there is an error
        }

        // Perform adding data to the database
        long newRowId = dbHelper.insertHikingRecord(name, location, date, parkingAvailable, lengthOfHike, difficultLevel, description);

        if (newRowId != -1) {
            // Data has been added successfully
            Toast.makeText(this, "Data was added successfully", Toast.LENGTH_SHORT).show();

            // Call the refreshData() method in MainActivity to update the data list
            MainActivity mainActivity = (MainActivity) getParent();
            mainActivity.refreshData();

            // Close Activity UploadActivity and return to MainActivity
            finish();
        } else {
            // Handle errors when adding data fails (e.g. display a message)
            Toast.makeText(this, "Error adding data", Toast.LENGTH_SHORT).show();
        }
    }
}