package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Camera;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SearchComplaint extends AppCompatActivity {
    EditText E1;
    String refNo;
    String Date,PhotoString,AreaCode,Complaint,Solved,Remarks;
    double Latitude,Longitude;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_complaint);
        E1=findViewById(R.id.refNo);
        databaseReference = database.getReference("App");
    }

    public void CheckComplaintExistence(View view) {
        refNo=E1.getText().toString();
        if(!TextUtils.isEmpty(refNo)){
            databaseReference.child("ComplaintSearch").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(refNo)){
                         Date=snapshot.child(refNo).child("date").getValue().toString();

                         PhotoString=snapshot.child(refNo).child("photostring").getValue().toString();
                         AreaCode=snapshot.child(refNo).child("areacode").getValue().toString();
                         Complaint=snapshot.child(refNo).child("complaint").getValue().toString();
                         Solved=snapshot.child(refNo).child("solved").getValue().toString();
                         Remarks=snapshot.child(refNo).child("remarks").getValue().toString();
                         Latitude=Double.parseDouble(snapshot.child(refNo).child("latitude").getValue().toString());
                         Longitude=Double.parseDouble(snapshot.child(refNo).child("longitude").getValue().toString());
                        Intent intent=new Intent(SearchComplaint.this,DetailsOfRefNo.class);
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
                        Toast.makeText(SearchComplaint.this, "Invalid Ref No ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            Toast.makeText(this, "Cannot Be Null", Toast.LENGTH_SHORT).show();
        }
    }
}