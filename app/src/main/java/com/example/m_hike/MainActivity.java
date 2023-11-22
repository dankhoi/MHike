package com.example.m_hike;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    List<HikeData> dataList;
    MyAdapterHiking adapter;
    SearchView searchView;
    DbHelper dbHelper;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();

        // Initialize your dbHelper here
        dbHelper = new DbHelper(this);

        @SuppressLint("Range") int id = 0;
        Cursor cursor = dbHelper.getAllHikingRecords(id);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Extract data from the cursor for each record
                id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String location = cursor.getString(cursor.getColumnIndex("location"));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
                @SuppressLint("Range") String parkingAvailable = cursor.getString(cursor.getColumnIndex("parkingAvailable"));
                @SuppressLint("Range") String lengthOfHike = cursor.getString(cursor.getColumnIndex("lengthOfHike"));
                @SuppressLint("Range") String difficultLevel = cursor.getString(cursor.getColumnIndex("difficultLevel"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));

                // Create a UserData object for this record
                HikeData hiking = new HikeData(id, name, location, date, parkingAvailable, lengthOfHike, difficultLevel, description);

                // Add the UserData object to the list
                dataList.add(hiking);
            }
            cursor.close();
            dialog.hide();
        }

        adapter = new MyAdapterHiking(MainActivity.this, dataList, dbHelper, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged(); // Update RecyclerView

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UploadHiking.class);
                startActivity(intent);
            }
        });

    }

    //refreshData create
    @SuppressLint("Range")
    public void refreshData() {
        dataList.clear(); // Delete current data
        int id = 0;
        Cursor cursor = dbHelper.getAllHikingRecords(id);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Get data from cursor and add to list
                id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String location = cursor.getString(cursor.getColumnIndex("location"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String parkingAvailable = cursor.getString(cursor.getColumnIndex("parkingAvailable"));
                String lengthOfHike = cursor.getString(cursor.getColumnIndex("lengthOfHike"));
                String difficultLevel = cursor.getString(cursor.getColumnIndex("difficultLevel"));
                String description = cursor.getString(cursor.getColumnIndex("description"));

                HikeData hiking = new HikeData(id, name, location, date, parkingAvailable, lengthOfHike, difficultLevel, description);

                dataList.add(hiking);
            }
            cursor.close();
        }
        adapter.notifyDataSetChanged(); // Update RecyclerView
    }

    public void searchList(String text){
        ArrayList<HikeData> searchList = new ArrayList<>();
        for (HikeData dataClass: dataList){
            if (dataClass.getName().toLowerCase().contains(text.toLowerCase())){
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }
}