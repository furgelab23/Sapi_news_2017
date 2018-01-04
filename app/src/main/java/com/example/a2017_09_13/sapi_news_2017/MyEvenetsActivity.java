package com.example.a2017_09_13.sapi_news_2017;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.a2017_09_13.sapi_news_2017.adapter.EventAdapter;
import com.example.a2017_09_13.sapi_news_2017.interfaces.EventSelectionListener;
import com.example.a2017_09_13.sapi_news_2017.model.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyEvenetsActivity extends AppCompatActivity implements EventSelectionListener{

    //Making RecyclerView
    RecyclerView recyclerView;
    EventAdapter adapter;
    private DatabaseReference databaseReference;//ramutatunk ezzel egy cimre
    ArrayList<Event> myevents = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;

    /**
     *
     * @param savedInstanceState which is a Bundle object containing the activity's previously saved state.
     *                           If theactivity has never existed before, the valu of the Bundle object is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_evenets);

        fillWithData(myevents);
        initRecyclerView();
    }


    /**
     *
     * @param myevents
     */
    private void fillWithData(final ArrayList<Event> myevents) {
        databaseReference = FirebaseDatabase.getInstance().getReference();//ezzel ferunk hozza
        databaseReference.child("events").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //ez csinal egy masolatot es itt toltom le az adatokat
                for (DataSnapshot iter : dataSnapshot.getChildren()) {
                    Event event = iter.getValue(Event.class);
                    Log.e("User",""+iter.child("uId").getValue());
                    Log.e("Useras",""+FirebaseAuth.getInstance().getCurrentUser().getUid());
                    if(iter.child("uId").getValue(String.class).matches(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        myevents.add(event);
                    }
                }
                adapter.notifyDataSetChanged();//megnezi , hogy mi valtozott s beteszi ha valtozas tortent
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //ha nem sikerul az adat lekerdezes
                throw databaseError.toException();
            }
        });
    }

    /**
     * Initialization the recycleView
     */
    private void initRecyclerView() {
        //Recycler List
        recyclerView = findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);


        adapter = new EventAdapter(myevents, this);

        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onEventClicked(Event event, Uri uri, View view) {

    }
}
