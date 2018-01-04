package com.example.a2017_09_13.sapi_news_2017;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a2017_09_13.sapi_news_2017.image.GlideApp;
import com.example.a2017_09_13.sapi_news_2017.model.Event;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * this activity help me about that the event details are displayed and the data query be so
 */
public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    ImageView dimage;
    TextView deventTitle;
    TextView ddescription;
    TextView duserName;
    TextView dphoneNumber;
    CircleImageView dcircleImageView;
    String phoneNumberUser,username;
    private GoogleMap mMap;
    double latitude = 0,longitude = 0;

    private FusedLocationProviderClient fusedLocationProviderClient;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    /**
     *
     * @param savedInstanceState which is a Bundle object containing the activity's previously saved state.
     *                           If theactivity has never existed before, the valu of the Bundle object is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        deventTitle = (TextView) findViewById(R.id.cevent_title);
        ddescription =(TextView) findViewById(R.id.cdescription);
        duserName = findViewById(R.id.cuserName);
        dphoneNumber =(TextView) findViewById(R.id.cphonenumber);
        dcircleImageView =(CircleImageView)findViewById(R.id.cprofile_image);
        dimage = findViewById(R.id.ivdetails);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Event event = bundle.getParcelable("event_key");
            deventTitle.setText(event.getTitle());
            ddescription.setText(event.getDescription());

            //fenykep csatolas az image viewre
            storageReference.child("teso.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    GlideApp.with(getApplicationContext())
                            .load(uri)
                            .into(dimage);

                    Log.e("Foto",""+uri);
                }
            });


            latitude = event.getCoordinate().getLatitude();
            longitude = event.getCoordinate().getLongitude();
           /*reference.child("events").child("coordinate").child("latitude").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    latitude = dataSnapshot.getValue(Double.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            reference.child("events").child("coordinate").child("longitude").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    longitude = dataSnapshot.getValue(Double.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });*/
            Log.e("Details","Coordinate:"+latitude+" = "+longitude);
            //Toast.makeText(DetailsActivity.this, "Coordinate:"+latitude+" = "+longitude,
            //        Toast.LENGTH_LONG).show();
            reference.child("users").child(event.getuId()).child("lastName").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    username = dataSnapshot.getValue(String.class);
                    duserName.setText(username);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    throw databaseError.toException();
                }
            });

            reference.child("users").child(event.getuId()).child("phoneNumber").addValueEventListener(new ValueEventListener() {
                /**
                 * Here query of data create on dataSnaphot .
                 * @param dataSnapshot here are the data looking for
                 */
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    phoneNumberUser =dataSnapshot.getValue(String.class);
                    dphoneNumber.setText(dataSnapshot.getValue(String.class));
                }

                /**
                 * when is the error this call
                 * @param databaseError
                 */
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
        //eventMap();
    }

    /**
     * Here give teh googleMap coordinate latitude and longitude
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentmap);
        mapFragment.getMapAsync((OnMapReadyCallback) getApplicationContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        final LatLng targuMures = new LatLng(46.55, 24.5667);
        final LatLng eventMarker = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(targuMures).title("Marker in "));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(targuMures, 12));
    }
}
