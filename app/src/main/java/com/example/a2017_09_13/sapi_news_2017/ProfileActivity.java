package com.example.a2017_09_13.sapi_news_2017;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.a2017_09_13.sapi_news_2017.adapter.EventAdapter;
import com.example.a2017_09_13.sapi_news_2017.model.Event;
import com.example.a2017_09_13.sapi_news_2017.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * This Activity shows the user data(firstName,lastName,emailAddress and phoneNumber)
 */
public class ProfileActivity extends AppCompatActivity {

    Button pbMyAdvertisments;
    TextView ptvfirstName,ptvlastName,ptvemaliAddress,ptvphonNumber;
    private DatabaseReference databaseReference;//ramutatunk ezzel egy cimre

    /**
     *
     * @param savedInstanceState which is a Bundle object containing the activity's previously saved state.
     *                           If the activity has never existed before, the value of the Bundle object is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        pbMyAdvertisments = findViewById(R.id.pbadvertisment) ;
        ptvfirstName =findViewById(R.id.ptvfNmae);
        ptvlastName = findViewById(R.id.ptvlName);
        ptvemaliAddress= findViewById(R.id.ptvemail_address);
        ptvphonNumber= findViewById(R.id.ptvphone_number);


        databaseReference = FirebaseDatabase.getInstance().getReference();//ezzel ferunk hozza
        databaseReference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    /**
                     * Here query of data create on dataSnaphot .
                     * @param dataSnapshot here are the data looking for
                     */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //ez csinal egy masolatot es itt toltom le az adatokat
                    User u = dataSnapshot.getValue(User.class);
                ptvfirstName.setText(u.getFirstName());
                ptvlastName.setText(u.getLastName());
                ptvemaliAddress.setText(u.getEmailAddress());
                ptvphonNumber.setText(u.getPhoneNumber());
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


        pbMyAdvertisments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fillWithData(myevents);//feltolti adatokkal
                Intent intent = new Intent(getApplicationContext(), MyEvenetsActivity.class);
                startActivity(intent);
            }
        });
    }

}