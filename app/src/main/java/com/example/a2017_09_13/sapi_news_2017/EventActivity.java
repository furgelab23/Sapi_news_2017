package com.example.a2017_09_13.sapi_news_2017;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.a2017_09_13.sapi_news_2017.adapter.EventAdapter;
import com.example.a2017_09_13.sapi_news_2017.model.Event;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.a2017_09_13.sapi_news_2017.interfaces.EventSelectionListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;

public class EventActivity extends AppCompatActivity implements EventSelectionListener {

    //Making RecyclerView
    RecyclerView recyclerView;
    //Linear Layout Manager
    LinearLayoutManager linearLayoutManager;
    //Setting up Adapter
    EventAdapter adapter;
    //Data Sets
    ArrayList<Event> events = new ArrayList<>();

    private DatabaseReference databaseReference;//ramutatunk ezzel egy cimre


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);//activity_event,card_view_list//
        initRecyclerView();
    }

    //menu sor hozza adasa
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_events:
                Intent jelzes = new Intent(getApplicationContext(), Add_EventActivity.class);
                startActivity(jelzes);
                return true;
            case R.id.item_profile:
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void initRecyclerView() {
        //Recycler List
        recyclerView = (RecyclerView) findViewById(R.id.busses);
        linearLayoutManager = new LinearLayoutManager(this);

        fillWithData(events);//feltolti adatokkal

        adapter = new EventAdapter(events, this);

        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


    }

    private void fillWithData(final ArrayList<Event> events) {
        databaseReference = FirebaseDatabase.getInstance().getReference();//ezzel ferunk hozza
        databaseReference.child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //ez csinal egy masolatot es itt toltom le az adatokat
                for (DataSnapshot iter : dataSnapshot.getChildren()) {
                    Object eventHash = iter.getValue();
                    Event event = (Event) eventHash;
                        Toast.makeText(getApplicationContext(), event.getTitle(), Toast.LENGTH_LONG)
                                .show();
//                    Event event = iter.getValue(Event.class);
//                    events.add(event);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //ha nem sikerul az adat lekerdezes
                throw databaseError.toException();
            }
        });
    }


    @Override
    public void onEventClicked(Event event, Uri uri, View view) {
        Bundle bundle = new Bundle();
        bundle.putString("uri_key", uri.toString());
        bundle.putParcelable("event_key", event);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, view,
                        "transition_key");
        startActivity(new Intent(this, DetailsActivity.class).putExtras(bundle), options.toBundle());//hol s mit jelenitsen meg

    }
}
