package com.example.miniproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class RaiseAnIssue extends AppCompatActivity implements LocationListener {
    ImageView imageView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int CAMERA_REQUEST_CODE = 102;
    String imageEncoded,AreaName,AreaCode,AreaSub,Complaint,Randno,Refno,Dept;
    Context context;
    boolean GpsStatus ;
    TextView T1;
    EditText E1;
    Double latitude,longitude;
    LocationManager locationManager;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_an_issue);
        context = getApplicationContext();
        imageView=findViewById(R.id.imageView);
        Dept=getIntent().getStringExtra("dept");
        databaseReference = database.getReference("App");
        T1=findViewById(R.id.locdetails);
        E1=findViewById(R.id.userComplaintText);
        mAuth = FirebaseAuth.getInstance();

    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            encodeBitmapAndSaveToFirebase(imageBitmap);
        }

    }
    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

    }

    public void CheckGpsStatus(){
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(GpsStatus == true) {
        } else {
            Toast.makeText(context, "GPS Is Disabled", Toast.LENGTH_SHORT).show();
            buildAlertMessageNoGps();

        }
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }



    @Override
    public void onLocationChanged(Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        T1.setText("Current Location: " + location.getLatitude() + ", " + location.getLongitude());
        final Geocoder gcd = new Geocoder(context);
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 10);
            for (Address address : addresses) {
                if(address.getLocality()!=null && address.getPostalCode()!=null){
                    AreaCode=address.getPostalCode();

                    databaseReference.child("AreaCode").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(AreaCode)){
                                AreaName=snapshot.child(AreaCode).child("Area").getValue().toString();


                            }
                            else{
                                AreaName="Empty";
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    break;

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void turnOnCamera(View view) {
        dispatchTakePictureIntent();
    }

    public void getLocationDetails(View view) {
    CheckGpsStatus();
        try {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);

        }
        catch(SecurityException e) {
            e.printStackTrace();
        }


    }

    public void RaiseAnIssue(View view) {
        Complaint=E1.getText().toString();


        if(!TextUtils.isEmpty(imageEncoded)){
            if(!TextUtils.isEmpty(Complaint)){
                if(!((latitude==null)&&(longitude==null))){
                    AreaSub=AreaName.substring(0,3);
                    Randno=getRandomNumberString();
                    Refno=AreaSub+Randno;
                    Calendar c=Calendar.getInstance();
                    SimpleDateFormat s=new SimpleDateFormat("dd:MM:yyyy");
                    String date=s.format(c.getTime());
                    String uid = mAuth.getCurrentUser().getUid();
                    databaseReference.child("User").child("People").child(uid).child("ComplaintsRaised").child(Refno).child("refno").setValue(Refno);
                    databaseReference.child("User").child("People").child(uid).child("ComplaintsRaised").child(Refno).child("date").setValue(date);

                    databaseReference.child("Complaint").child(Dept).child(AreaName).child("Unsolved").child(Refno).child("refno").setValue(Refno);
                    databaseReference.child("Complaint").child(Dept).child(AreaName).child("Unsolved").child(Refno).child("photostring").setValue(imageEncoded);
                    databaseReference.child("Complaint").child(Dept).child(AreaName).child("Unsolved").child(Refno).child("latitude").setValue(latitude);
                    databaseReference.child("Complaint").child(Dept).child(AreaName).child("Unsolved").child(Refno).child("longitude").setValue(longitude);
                    databaseReference.child("Complaint").child(Dept).child(AreaName).child("Unsolved").child(Refno).child("complaint").setValue(Complaint);
                    databaseReference.child("Complaint").child(Dept).child(AreaName).child("Unsolved").child(Refno).child("solved").setValue("false");
                    databaseReference.child("Complaint").child(Dept).child(AreaName).child("Unsolved").child(Refno).child("areacode").setValue(AreaCode);
                    databaseReference.child("Complaint").child(Dept).child(AreaName).child("Unsolved").child(Refno).child("date").setValue(date);

                    databaseReference.child("ComplaintSearch").child(Refno).child("refno").setValue(Refno);
                    databaseReference.child("ComplaintSearch").child(Refno).child("latitude").setValue(latitude);
                    databaseReference.child("ComplaintSearch").child(Refno).child("longitude").setValue(longitude);
                    databaseReference.child("ComplaintSearch").child(Refno).child("complaint").setValue(Complaint);
                    databaseReference.child("ComplaintSearch").child(Refno).child("solved").setValue("false");
                    databaseReference.child("ComplaintSearch").child(Refno).child("department").setValue(Dept);
                    databaseReference.child("ComplaintSearch").child(Refno).child("date").setValue(date);
                    databaseReference.child("ComplaintSearch").child(Refno).child("areacode").setValue(AreaCode);
                    databaseReference.child("ComplaintSearch").child(Refno).child("photostring").setValue(imageEncoded);
                    databaseReference.child("ComplaintSearch").child(Refno).child("remarks").setValue("");
                    E1.setText("");
                    T1.setText("");
                    imageView.setImageBitmap(null);
                    Intent intent=new Intent(RaiseAnIssue.this,AckForRaisingComplaint.class);
                    intent.putExtra("Refno",Refno);
                    intent.putExtra("Department",Dept);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(context, "Location Unavailable", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(context, "Enter anything about this complaint", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context, "Photo is not Available", Toast.LENGTH_SHORT).show();
        }
    }


    ///After submit btn    databaseReference.child("Complaint").child("Eb").child("Unsolved").child("").child("PhotoString").setValue(imageEncoded);
    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }
}