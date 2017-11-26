package com.example.a2017_09_13.sapi_news_2017;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.a2017_09_13.sapi_news_2017.model.Event;
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

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        deventTitle = (TextView) findViewById(R.id.cevent_title);
        ddescription =(TextView) findViewById(R.id.cdescription);
        dphoneNumber =(TextView) findViewById(R.id.cphonenumber);
        dcircleImageView =(CircleImageView)findViewById(R.id.cprofile_image);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Event event = bundle.getParcelable("event_key");
            deventTitle.setText(event.getTitle());
            ddescription.setText(event.getDescription());
            reference.child("users").child(event.getuId()).child("phoneNumber").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dphoneNumber.setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //ha nem sikerul az adat lekerdezes
                    throw databaseError.toException();
                }
            });
        }
    }


}
