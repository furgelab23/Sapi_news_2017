package com.example.a2017_09_13.sapi_news_2017;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a2017_09_13.sapi_news_2017.model.Event;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsActivity extends AppCompatActivity {


    TextView deventTitle;
    TextView ddescription;
    TextView dphoneNumber;
    CircleImageView dcircleImageView;
    String phoneNumberUser;
    private GoogleMap mMap;
    double latitude = 0,longitude = 0;
    private FusedLocationProviderClient fusedLocationProviderClient;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        deventTitle = (TextView) findViewById(R.id.cevent_title);
        ddescription =(TextView) findViewById(R.id.cdescription);
        dphoneNumber =(TextView) findViewById(R.id.cphonenumber);
        dcircleImageView =(CircleImageView)findViewById(R.id.cprofile_image);

        eventMap();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Event event = bundle.getParcelable("event_key");
            deventTitle.setText(event.getTitle());
            ddescription.setText(event.getDescription());

            latitude = event.getCoordinate().getLatitude();
            longitude = event.getCoordinate().getLongitude();
            Log.e("Details","Coordinate:"+latitude+" = "+longitude);
            Toast.makeText(DetailsActivity.this, "Coordinate:"+latitude+" = "+longitude,
                    Toast.LENGTH_LONG).show();

            reference.child("users").child(event.getuId()).child("phoneNumber").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    phoneNumberUser =dataSnapshot.getValue(String.class);
                    dphoneNumber.setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //ha nem sikerul az adat lekerdezes
                    throw databaseError.toException();
                }
            });
        }
        dphoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumberUser, null));
                startActivity(intent);
            }
        });
    }
    public void eventMap(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentmap);
        //mapFragment.getMapAsync((OnMapReadyCallback) getApplicationContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        final LatLng eventMarker = new LatLng(latitude, longitude);
        //mMap.addMarker(new MarkerOptions().position(eventMarker).title("Marker in "));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventMarker, 12));
    }

}
