package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ComplaintsRaisedByUser extends AppCompatActivity {
    RecyclerView r;
    FirebaseRecyclerAdapter<FetchData,myViewHolder> adapter;
    DatabaseReference databaseReference,databaseReference2;
    FirebaseRecyclerOptions<FetchData> options;
    String Date,PhotoString,AreaCode,Complaint,Solved,refNo,Remarks,uid;
    double Latitude,Longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints_raised_by_user);
        r=findViewById(R.id.recycler);
        r.setHasFixedSize(true);
        r.setLayoutManager(new LinearLayoutManager(this));
        r.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        uid=getIntent().getStringExtra("uid");
        databaseReference= FirebaseDatabase.getInstance().getReference("App").child("User").child("People").child(uid).child("ComplaintsRaised");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("App");
        LoadData();
    }
    private void LoadData(){
        options=new FirebaseRecyclerOptions.Builder<FetchData>().setQuery(databaseReference,FetchData.class).build();
        adapter=new FirebaseRecyclerAdapter<FetchData, myViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull FetchData model) {
                holder.t1.setText(model.getRefno());
                holder.t2.setText(model.getDate());
                holder.t1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //
                        refNo=model.getRefno();
                        databaseReference2.child("ComplaintSearch").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChild(refNo)){
                                    Date=snapshot.child(refNo).child("date").getValue().toString();
                                    Remarks=snapshot.child(refNo).child("remarks").getValue().toString();
                                    PhotoString=snapshot.child(refNo).child("photostring").getValue().toString();
                                    AreaCode=snapshot.child(refNo).child("areacode").getValue().toString();
                                    Complaint=snapshot.child(refNo).child("complaint").getValue().toString();
                                    Solved=snapshot.child(refNo).child("solved").getValue().toString();
                                    Latitude=Double.parseDouble(snapshot.child(refNo).child("latitude").getValue().toString());
                                    Longitude=Double.parseDouble(snapshot.child(refNo).child("longitude").getValue().toString());
                                    Intent intent=new Intent(ComplaintsRaisedByUser.this,DetailsOfRefNo.class);
                                    intent.putExtra("Date",Date);
                                    intent.putExtra("PhotoString",PhotoString);
                                    intent.putExtra("AreaCode", AreaCode);
                                    intent.putExtra("Complaint",Complaint);
                                    intent.putExtra("RefNo",refNo);
                                    intent.putExtra("Solved",Solved);
                                    intent.putExtra("Remarks",Remarks);
                                    Bundle b = new Bundle();
                                    b.putDouble("Latitude", Latitude);
                                    b.putDouble("Longitude", Longitude);
                                    intent.putExtras(b);
                                    startActivity(intent);
                                    //                      remarks=snapshot.child(refNo).child("remarks").getValue().toString();
                                }
                                else{
                                    Toast.makeText(ComplaintsRaisedByUser.this, "Invalid Ref No ", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }

            @NonNull
            @Override
            public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.raisedcomplaintsdata,parent,false);

                return new myViewHolder(v);
            }
        };
        adapter.startListening();
        r.setAdapter(adapter);
    }
}