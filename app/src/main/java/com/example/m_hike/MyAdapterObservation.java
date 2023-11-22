package com.example.m_hike;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapterObservation extends RecyclerView.Adapter<MyViewHolderObservations>{

    private Context context;
    private List<ObservationData> dataListObservation;
    DbHelper dbHelper;
    DetailHiking detailActivity;


    public MyAdapterObservation(Context context, List<ObservationData> dataListObservation, DbHelper dbHelper, DetailHiking detailActivity) {
        this.context = context;
        this.dataListObservation = dataListObservation;
        this.dbHelper = dbHelper;
        this.detailActivity = detailActivity;
    }

    public void resetDataList(List<ObservationData> newDataList) {
        dataListObservation = newDataList;
    }

    @NonNull
    @Override
    public MyViewHolderObservations onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_detail_observation, parent, false);
        return new MyViewHolderObservations(view, dataListObservation, dbHelper, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderObservations holder, int position) {
        ObservationData observationData = dataListObservation.get(position);
        holder.textViewName.setText("Name: " + observationData.getName());
        holder.textViewDate.setText("Date: " + observationData.getTime());
        holder.textViewComment.setText("Comment: " + observationData.getComment());
    }

    @Override
    public int getItemCount() {
        return dataListObservation.size();
    }
}
class MyViewHolderObservations extends RecyclerView.ViewHolder{

    TextView textViewName, textViewDate,  textViewComment;
    CardView recCardViewDetail;
    Button buttonDelete, buttonUpdate;
    List<ObservationData> dataList;
    DbHelper dbHelper;
    MyAdapterObservation adapterObservation;

    public MyViewHolderObservations(@NonNull View itemView, List<ObservationData> dataList, DbHelper dbHelper, MyAdapterObservation adapterObservation) {
        super(itemView);
        this.dataList = dataList;
        this.dbHelper = dbHelper;
        this.adapterObservation = adapterObservation;

        recCardViewDetail = itemView.findViewById(R.id.recCardViewDetail);
        textViewName = itemView.findViewById(R.id.textView_Name);
        textViewDate = itemView.findViewById(R.id.textView_date);
        textViewComment = itemView.findViewById(R.id.textView_Comment);


        // Initialize the buttons
        buttonDelete = itemView.findViewById(R.id.btnDelete);
        buttonUpdate = itemView.findViewById(R.id.btnUpdate);

        // Set click listeners for the buttons
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ObservationData observationData = dataList.get(position);
                    int id = observationData.getId();

                    // Show a confirmation dialog
                    showDeleteConfirmationDialog(id, position);
                }
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Get the data of the clicked item
                    ObservationData observationData = dataList.get(position);
                    int id = observationData.getId();
                    // Get hikingId value from observationData
                    int hikingId = observationData.getHikingId();

                    // Create Intent to switch to UpdateObservationActivity
                    Intent intent = new Intent(itemView.getContext(), UpdateObservation.class);

                    // Attach the clicked item's data to the Intent to switch to UpdateObservationActivity
                    intent.putExtra("id", id); // Make sure the id is passed as the correct data type
                    // Đính kèm giá trị hikingId vào Intent
                    intent.putExtra("hikingId", hikingId);

                    // Put old data into Intent
                    intent.putExtra("name", observationData.getName());
                    intent.putExtra("date", observationData.getTime());
                    intent.putExtra("comment", observationData.getComment());

                    // Launch UpdateObservationActivity
                    itemView.getContext().startActivity(intent);
                }
            }
        });



    }

    private void showDeleteConfirmationDialog(int id, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this item?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User confirmed deletion
                dbHelper.deleteObservationRecord(id);

                // Remove the deleted item from the data list
                dataList.remove(position);

                // Notify the adapter that the data has changed
                adapterObservation.notifyItemRemoved(position);
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