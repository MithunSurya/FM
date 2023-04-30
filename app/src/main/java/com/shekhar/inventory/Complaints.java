package com.shekhar.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class Complaints extends AppCompatActivity {


    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);


        recyclerView = findViewById(R.id.complaints_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Complaints.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setItemViewCacheSize(25);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_orders_tool_bar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //firebase
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        Query query = rootRef.collection("complaints")
                .orderBy("timestamp", Query.Direction.DESCENDING);




        FirestoreRecyclerOptions<ComplaintModel> options = new FirestoreRecyclerOptions.Builder<ComplaintModel>()
                .setQuery(query, ComplaintModel.class)
                .build();




        FirestoreRecyclerAdapter<ComplaintModel, Complaints.ProductViewHolder> searchAdapter = new FirestoreRecyclerAdapter<ComplaintModel, Complaints.ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final Complaints.ProductViewHolder holder, int position, @NonNull final ComplaintModel model) {

                holder.complaintName.setText(model.getName());
                holder.complaintSection.setText(model.getSection());
                holder.complaintDescription.setText(model.getDescription());
                holder.complaintTimeStamp.setText(DateFormat.format("d MMM yyyy, h:mm a", model.getTimestamp().toDate()));




                //Load image to place holder
                Picasso.get().load(model.getImage()).into(holder.complaintImageView);





                //On single complaint click
                holder.itemHolder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });


            }

            @NonNull
            @Override
            public Complaints.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.complaint_item_layout, parent, false);

                Complaints.ProductViewHolder viewHolder = new Complaints.ProductViewHolder(view);
                return viewHolder;


            }
        };

        recyclerView.setAdapter(searchAdapter);
        searchAdapter.startListening();











    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout itemHolder;
        ImageView complaintImageView;

        TextView complaintName;
        TextView complaintDescription;
        TextView complaintSection;
        TextView complaintTimeStamp;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            itemHolder = itemView.findViewById(R.id.product_item_holder);
            complaintName = itemView.findViewById(R.id.complaint_name);
            complaintImageView = itemView.findViewById(R.id.complaint_image);
            complaintDescription = itemView.findViewById(R.id.complaint_description);
            complaintSection = itemView.findViewById(R.id.complaint_section);
            complaintTimeStamp = itemView.findViewById(R.id.complaint_timestamp);

        }
    }

}