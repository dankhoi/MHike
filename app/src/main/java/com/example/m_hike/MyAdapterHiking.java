package com.example.m_hike;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapterHiking extends RecyclerView.Adapter<MyViewHolderHiking> {

    private Context context;
    private List<HikeData> dataList;
    DbHelper dbHelper;
    MainActivity mainActivity;

    public MyAdapterHiking(Context context, List<HikeData> dataList, DbHelper dbHelper, MainActivity mainActivity) {
        this.context = context;
        this.dataList = dataList;
        this.dbHelper = dbHelper;
        this.mainActivity = mainActivity;
    }

    public void resetDataList(List<HikeData> newDataList) {
        dataList = newDataList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolderHiking onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolderHiking(view, dataList, dbHelper, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderHiking holder, int position) {
        holder.textViewName.setText("Name: "+ dataList.get(position).getName());
        holder.textViewLocation.setText("Location: "+ dataList.get(position).getLocation());
        holder.textViewDate.setText("Date: "+ dataList.get(position).getDate());
        holder.textViewParkingAvailable.setText("Parking Available: "+ dataList.get(position).getParkingAvailable());
        holder.textViewLengthOfHike.setText("Length Of Hike: "+ dataList.get(position).getLengthOfHike());
        holder.textViewDifficultLevel.setText("Difficult Level: "+ dataList.get(position).getDifficultLevel());
        holder.textViewDescription.setText("Description: "+ dataList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<HikeData> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}

class MyViewHolderHiking extends RecyclerView.ViewHolder{

    TextView textViewName,textViewLocation, textViewDate, textViewParkingAvailable, textViewLengthOfHike, textViewDifficultLevel, textViewDescription;
    CardView recCard;
    Button buttonDelete, buttonUpdate, buttonViewDetails;
    List<HikeData> dataList;
    DbHelper dbHelper;
    MyAdapterHiking adapter;

    public MyViewHolderHiking(@NonNull View itemView, List<HikeData> dataList, DbHelper dbHelper, MyAdapterHiking adapter) {
        super(itemView);
        this.dataList = dataList;
        this.dbHelper = dbHelper;
        this.adapter = adapter;

        recCard = itemView.findViewById(R.id.recCard);
        textViewName = itemView.findViewById(R.id.tView_Name);
        textViewLocation = itemView.findViewById(R.id.tView_Location);
        textViewDate = itemView.findViewById(R.id.tView_date);
        textViewParkingAvailable = itemView.findViewById(R.id.tView_Parking);
        textViewLengthOfHike = itemView.findViewById(R.id.tView_lengthOfHike);
        textViewDifficultLevel = itemView.findViewById(R.id.tView_difficultLevel);
        textViewDescription = itemView.findViewById(R.id.tView_description);

        // Initialize the buttons
        buttonDelete = itemView.findViewById(R.id.btnDelete);
        buttonUpdate = itemView.findViewById(R.id.btnUpdate);
        buttonViewDetails = itemView.findViewById(R.id.btnViewDetails);

        // Set click listeners for the buttons
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the Delete button click
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    HikeData hikingData = dataList.get(position);
                    int id = hikingData.getId();

                    // Show a confirmation dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setTitle("Confirm Deletion");
                    builder.setMessage("Are you sure you want to delete this item?");
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // User confirmed deletion
                            dbHelper.deleteHikingRecord(id);

                            // Remove the deleted item from the data list
                            dataList.remove(position);

                            // Notify the adapter that the data has changed
                            adapter.resetDataList(dataList);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // User canceled deletion, do nothing
                        }
                    });
                    builder.show();
                }
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the Update button click
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Get the data of the clicked item
                    HikeData hikingData = dataList.get(position);
                    int id = hikingData.getId();

                    // Create Intent to switch to UpdateHiking
                    Intent intent = new Intent(itemView.getContext(), UpdateHiking.class);
                    intent.putExtra("id", String.valueOf(id));

                    // Launch UpdateHiking
                    itemView.getContext().startActivity(intent);
                }
            }
        });


        buttonViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    HikeData hikingData = dataList.get(position);
                    int selectedHikingId = hikingData.getId(); // Get hiking from Hiking Data

                    // Pass hikingId through DetailActivity
                    Intent intent = new Intent(itemView.getContext(), DetailHiking.class);
                    intent.putExtra("hikingId", selectedHikingId);
                    itemView.getContext().startActivity(intent);
                }
            }
        });

    }
}