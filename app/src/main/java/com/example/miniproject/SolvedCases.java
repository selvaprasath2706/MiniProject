package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SolvedCases extends AppCompatActivity {
    String Dept,Area;
    RecyclerView r;
    FirebaseRecyclerAdapter<FetchData,myViewHolder> adapter;
    DatabaseReference databaseReference;
    FirebaseRecyclerOptions<FetchData> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solved_cases);
        r=findViewById(R.id.recycler);
        r.setHasFixedSize(true);
        r.setLayoutManager(new LinearLayoutManager(this));
        r.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//        Area=getIntent().getStringExtra("Area");
        //      Dept=getIntent().getStringExtra("Department");
        Dept=getIntent().getStringExtra("Department");
        Area=getIntent().getStringExtra("Area");
        databaseReference= FirebaseDatabase.getInstance().getReference("App").child("Complaint").child(Dept).child(Area).child("Solved");
        LoadData();

    }
    private void LoadData(){
        options=new FirebaseRecyclerOptions.Builder<FetchData>().setQuery(databaseReference,FetchData.class).build();
        adapter=new FirebaseRecyclerAdapter<FetchData, myViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull FetchData model) {
                holder.t1.setText(model.getAreacode());
                holder.t2.setText(model.getDate());//model.getComplaint()
                holder.t3.setText("Lat"+String.valueOf(model.getLatitude())+"Long"+String.valueOf(model.getLongitude()));
                holder.t3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri mapUri = Uri.parse("geo:0,0?q="+model.getLatitude()+","+model.getLongitude()+"");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                });
            }

            @NonNull
            @Override
            public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.data,parent,false);

                return new myViewHolder(v);
            }
        };
        adapter.startListening();
        r.setAdapter(adapter);
    }

}