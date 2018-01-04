package com.example.a2017_09_13.sapi_news_2017;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.a2017_09_13.sapi_news_2017.adapter.EventAdapter;
import com.example.a2017_09_13.sapi_news_2017.model.Event;

import java.util.ArrayList;
import java.util.List;

import com.example.a2017_09_13.sapi_news_2017.interfaces.EventSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;


/**
 * this serves as the emergence of events. Here user the cardView and recycleView
 */
public class EventActivity extends AppCompatActivity implements EventSelectionListener,NavigationView.OnNavigationItemSelectedListener {//,SearchView.OnQueryTextListener{

    //Making RecyclerView
    RecyclerView recyclerView;
    //Linear Layout Manager
    LinearLayoutManager linearLayoutManager;
    //Setting up Adapter
    EventAdapter adapter;
    //Data Sets
    ArrayList<Event> events = new ArrayList<>();
    //Search View
    MaterialSearchView materialSearchView;
    //Is Pressed Boolean
    boolean isPressed = false;
    AlertDialog.Builder abousMeDialog;
    Toolbar mToolbar;
    NavigationView navigationView;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;



    private DatabaseReference databaseReference;//ramutatunk ezzel egy cimre

    /**
     *
     * @param savedInstanceState which is a Bundle object containing the activity's previously saved state.
     *                           If theactivity has never existed before, the valu of the Bundle object is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        fillWithData(events);//feltolti adatokkal
        initRecyclerView();
        prepareSearchView();
        mToolbar = findViewById(R.id.app_bar);


        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        recyclerView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(EventActivity.this, "Long Click", Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }


    //menu sor hozza adasa

    /**
     *
     * @param menu this contains the menu.xml data
     * @return (boolean) true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        materialSearchView.setMenuItem(item);
        return true;
    }


    /**
     * when click the logout button this is called and the user is break.
     */
    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getApplicationContext(), "Logged out of this session", Toast.LENGTH_SHORT)
                .show();
        finish();
    }

    /**
     * Initialization the recycleView
     */
    private void initRecyclerView() {
        //Recycler List
        recyclerView = findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);


        adapter = new EventAdapter(events,this);

        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        abousMeDialog = new AlertDialog.Builder(EventActivity.this);


    }

    /**
     * Here query of event data .
     * @param events this contains the data
     */
    private void fillWithData(final ArrayList<Event> events) {
        databaseReference = FirebaseDatabase.getInstance().getReference();//ezzel ferunk hozza
        databaseReference.child("events").addValueEventListener(new ValueEventListener() {
            /**
             * Here query of data create on dataSnaphot .
             * @param dataSnapshot here are the data looking for
             */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //ez csinal egy masolatot es itt toltom le az adatokat
                events.clear();
                for (DataSnapshot iter : dataSnapshot.getChildren()) {
                    Event event = iter.getValue(Event.class);
                    events.add(event);
                }
                adapter.notifyDataSetChanged();//megnezi , hogy mi valtozott s beteszi ha valtozas tortent
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

    /**
     * this is the responsiblility of the data wrapping
     * @param event
     * @param uri this the picture and this send DetailsActivity(makeSceneTransmititionAnimation)
     * @param view
     */
    @Override
    public void onEventClicked(Event event, Uri uri, View view) {
        Bundle bundle = new Bundle();
        bundle.putString("uri_key", uri.toString());
        bundle.putParcelable("event_key", event);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, view,
                        "transition_key");
        startActivity(new Intent(this,
                DetailsActivity.class).putExtras(bundle),
                options.toBundle());//hol s mit jelenitsen meg
    }

    /**
     * this responsibile wherefore that the user no break out one clik
     */
    @Override
    public void onBackPressed() {
        if (isPressed) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "Tap again to exit app", Toast.LENGTH_SHORT).show();
            isPressed = true;
            (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isPressed = false;
                    }
            }, 500);
        }
    }


    /**
     * this is the search
     * this help the date search
     */
    private void prepareSearchView() {
        materialSearchView = findViewById(R.id.material_search_view);
        materialSearchView.setCloseIcon(getResources().getDrawable(R.drawable.ic_clear_black_24dp, getTheme()));
        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            /**
             * search on event
             * @param newText search for the text you typed
             * @return one ArrayList this includes all events what he found
             */
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && !newText.isEmpty()) {
                    List<Event> lstFound = new ArrayList<>();
                    for (Event item : events) {
                        if (item.getTitle().contains(newText)
                                || item.getDescription().contains(newText)) {
                            lstFound.add(item);
                        }
                    }
                    adapter.updateData((ArrayList<Event>) lstFound);
                    adapter.notifyDataSetChanged();
                    return true;
                } else if (newText == null) {
                    adapter.updateData(events);
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        });

        materialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            /**
             *
             */
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            /**
             *
             */
            @Override
            public void onSearchViewClosed() {
                //Do some magic
                adapter.addData(events);
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * it is here that the navigationBar show choices and the user click one and starts the chain reaction
     * @param item all menu item he see the xml and looking choices
     * @return what you choose
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_profile:
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_add_events:
                Intent jelzes = new Intent(getApplicationContext(), Add_EventActivity.class);
                startActivity(jelzes);
                break;
            case R.id.nav_action_logout:
                logOut();
                break;
            case R.id.nav_contact:
                Intent contactIntent = new Intent(Intent.ACTION_SENDTO);
                contactIntent.setData(Uri.parse("mailto:"));
                contactIntent.putExtra(Intent.EXTRA_EMAIL, getResources().getStringArray(R.array.EXTRA_EMAIL_CONTACTS));
                contactIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.EXTRA_EMAIL_SUBJECT));
                if (contactIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(contactIntent);
                }
                break;
            case R.id.nav_about:
                abousMeDialog.setTitle(R.string.ABOUT_ME_TITLE)
                        .setMessage(R.string.ABOUT_ME_MSG)
                        .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

                break;

            default:
                break;
        }
        return true;
    }

    /**
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}

