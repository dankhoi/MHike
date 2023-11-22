package com.example.m_hike;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DetailHiking extends AppCompatActivity {
    FloatingActionButton fabCreate;
    RecyclerView recyclerViewDetails;
    List<ObservationData> dataListObservation;
    MyAdapterObservation adapterObservation;
    DbHelper dbHelper;
    int hikingId;
    HikeData hikingData; // Add variable to store hike name

    @SuppressLint({"MissingInflatedId", "Range"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Get hikingId from Intent using the correct key
        Intent intent = getIntent();
        hikingId = intent.getIntExtra("hikingId", 0);

        // Initialize your dbHelper here
        dbHelper = new DbHelper(this);

        // Get the hike name from the database
        hikingData = dbHelper.getHikingRecordById(hikingId);

        // Display the hiking name on the TextView
        TextView textViewHikingName = findViewById(R.id.textViewHikingName);
        textViewHikingName.setText(hikingData.getName());

        recyclerViewDetails = findViewById(R.id.recyclerObservationView);
        fabCreate = findViewById(R.id.fabCreate);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(DetailHiking.this, 1);
        recyclerViewDetails.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(DetailHiking.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataListObservation = new ArrayList<>();
        @SuppressLint("Range") int id = 0;
        Cursor cursor = dbHelper.getAllObservationRecords(hikingId);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Extract data from the cursor for each record
                id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("time"));
                @SuppressLint("Range") String comment = cursor.getString(cursor.getColumnIndex("comment"));
                @SuppressLint("Range") int hikingId = cursor.getInt(cursor.getColumnIndex("hikingId"));

                // Create a UserData object for this record
                ObservationData observationData = new ObservationData(id, name, date, comment, hikingId);

                // Add the UserData object to the list
                dataListObservation.add(observationData);
            }
            cursor.close();
            dialog.hide();
        }

        adapterObservation = new MyAdapterObservation(DetailHiking.this, dataListObservation, dbHelper, this);
        recyclerViewDetails.setAdapter(adapterObservation);

        fabCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailHiking.this, UploadObservation.class);
                intent.putExtra("hikingId", hikingId); // Pass the hikingId to the UploadObservationActivity
                startActivity(intent);
            }
        });
    }

    public void onBackButtonClick(View view) {
        // Handle the event when the "Back" button is clicked
        finish(); // Close DetailActivity and return to the previous screen
    }


    //refreshData create
    @SuppressLint("Range")
    public void refreshData() {
        // Initialize a new list to store new data
        List<ObservationData> newDataList = new ArrayList<>();
        int id = 0;
        Cursor cursor = dbHelper.getAllObservationRecords(hikingId);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Extract data from the cursor for each record
                id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("time"));
                @SuppressLint("Range") String comment = cursor.getString(cursor.getColumnIndex("comment"));
                @SuppressLint("Range") int hikingId = cursor.getInt(cursor.getColumnIndex("hikingId"));

                // Create a UserData object for this record
                ObservationData observationData = new ObservationData(id, name, date, comment, hikingId);

                // Add the UserData object to the new list
                newDataList.add(observationData);
            }
            cursor.close();
        }

        // Delete old list
        dataListObservation.clear();

        // Add all new data to the old list
        dataListObservation.addAll(newDataList);

        // Update RecyclerView
        adapterObservation.notifyDataSetChanged();
    }

}