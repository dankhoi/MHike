
package com.example.m_hike;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class UpdateHiking extends AppCompatActivity {

    EditText eTName, eTLocation, eTDate, eTParkingAvailable, eTLengthOfHike, eTDifficultLevel, eTDescription;
    Button updateBtn, cancelBtn;
    int id; // ID of the record to update
    DbHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        eTName = findViewById(R.id.eTu_Name);
        eTLocation = findViewById(R.id.eTu_Location);
        eTDate = findViewById(R.id.eT_Date);
        eTParkingAvailable = findViewById(R.id.eTu_Parking);
        eTLengthOfHike = findViewById(R.id.eTu_LengthOfHike);
        eTDifficultLevel = findViewById(R.id.eTu_DifficultLevel);
        eTDescription = findViewById(R.id.eTu_Description);

        updateBtn = findViewById(R.id.updateButton);
        cancelBtn = findViewById(R.id.CancelButton);

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
                        UpdateHiking.this,
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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String idString = bundle.getString("id");

            // Check if ID exists or not
            if (idString != null) {
                id = Integer.parseInt(idString);

                // Use DatabaseHelper to get old data from the database
                HikeData hikingData = dbHelper.getHikingRecordById(id);
                if (hikingData != null) {
                    // Đặt dữ liệu cũ vào các EditText
                    eTName.setText(hikingData.getName());
                    eTLocation.setText(hikingData.getLocation());
                    eTDate.setText(hikingData.getDate());
                    eTParkingAvailable.setText(hikingData.getParkingAvailable());
                    eTLengthOfHike.setText(hikingData.getLengthOfHike());
                    eTDifficultLevel.setText(hikingData.getDifficultLevel());
                    eTDescription.setText(hikingData.getDescription());
                }
            } else {
                // ID does not exist, handle optionally (e.g. show notification)
            }
        }

        // Set up click listeners for your buttons
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Close the update operation and return to the previous screen
            }
        });
    }
    public void saveData(){
        TextInputLayout textInputLayoutName = findViewById(R.id.textInputLayoutName);
        TextInputLayout textInputLayoutLocation = findViewById(R.id.textInputLayoutLocation);
        TextInputLayout textInputLayoutParking = findViewById(R.id.textInputLayoutParking);
        TextInputLayout textInputLayoutLengthOfHike = findViewById(R.id.textInputLayoutLengthOfHike);
        TextInputLayout textInputLayoutDifficultLevel = findViewById(R.id.textInputLayoutDifficultLevel);
        TextInputLayout textInputLayoutDescription = findViewById(R.id.textInputLayoutDescription);

        // Hide error messages and clear data in EditText
        textInputLayoutName.setErrorEnabled(false); // Turn off error display
        textInputLayoutLocation.setErrorEnabled(false); // Turn off error display

        // Get data from edit fields
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
            textInputLayoutName.setError("Name is required");
            isValid = false;
        } else {
            textInputLayoutName.setError(null); // Clear error message if field is valid
        }

        if (location.isEmpty()) {
            textInputLayoutLocation.setError("Location is required");
            isValid = false;
        } else {
            textInputLayoutLocation.setError(null); // Clear error message if field is valid
        }

        if (parkingAvailable.isEmpty()) {
            textInputLayoutParking.setError("Parking is required");
            isValid = false;
        } else {
            textInputLayoutParking.setError(null); // Clear error message if field is valid
        }

        if (lengthOfHike.isEmpty()) {
            textInputLayoutLengthOfHike.setError("Length of hike is required");
            isValid = false;
        } else {
            textInputLayoutLengthOfHike.setError(null); // Clear error message if field is valid
        }

        if (difficultLevel.isEmpty()) {
            textInputLayoutDifficultLevel.setError("Difficult level is required");
            isValid = false;
        } else {
            textInputLayoutDifficultLevel.setError(null); // Clear error message if field is valid
        }

        if (description.isEmpty()) {
            textInputLayoutDescription.setError("Description is required");
            isValid = false;
        } else {
            textInputLayoutDescription.setError(null); // Clear error message if field is valid
        }

        if (!isValid) {
            // Display an error message and do not save data if there is an error
            Toast.makeText(UpdateHiking.this, "Please complete all information", Toast.LENGTH_SHORT).show();
            return; // Do not perform update if there is an error
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateHiking.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        int rowsUpdated = dbHelper.updateHikingRecord(id, name, location, date, parkingAvailable, lengthOfHike, difficultLevel, description);
        if (rowsUpdated > 0) {
            Toast.makeText(UpdateHiking.this, "Data has been updated", Toast.LENGTH_SHORT).show();

            // Call the refreshData() method in MainActivity to update the data list
            MainActivity mainActivity = (MainActivity) getParent();
            mainActivity.refreshData();

            setResult(RESULT_OK); // Set the result to success
            finish(); // End the update operation
        } else {
            Toast.makeText(UpdateHiking.this, "Error updating data", Toast.LENGTH_SHORT).show();
        }
    }
}