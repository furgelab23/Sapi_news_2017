package com.example.a2017_09_13.sapi_news_2017.adapter;

import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.a2017_09_13.sapi_news_2017.R;
import com.example.a2017_09_13.sapi_news_2017.image.GlideApp;
import com.example.a2017_09_13.sapi_news_2017.interfaces.EventSelectionListener;
import com.example.a2017_09_13.sapi_news_2017.model.Event;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 2017-09-13 on 11/10/2017.
 */

public class  EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>{

    private List<Event> events = new ArrayList<>();

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private EventSelectionListener eventSelectionListener;
    private Uri mUri;

    public EventAdapter(List<Event> events,EventSelectionListener eventSelectionListener) {
        this.events = events;
        this.eventSelectionListener = eventSelectionListener;
    }

    public void addData(List<Event> event){
        this.events = event;
        notifyDataSetChanged();
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final EventViewHolder holder, int position) {
       final Event event = events.get(position);
       holder.viewtitle.setText(event.getTitle());
       holder.viewdescription.setText(event.getDescription());
       //Sikerult Glidenak a teljes funkcionalitasat el erjem igy tudsz placeholdereket tenni es transzformaciokat vegezni
        //placeholder egy placeholder kepet tolt be
        storageReference.child("teso.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                mUri = uri;
                GlideApp.with(holder.itemView.getContext()) //kontextus
                       .load(uri.toString())//mit
                       .into(holder.viewImage);//hova
            }
        });

        holder.cardViewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventSelectionListener.onEventClicked(event,mUri,view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();//hogy tudja , hogy hany viewt kezeljen generaljon ki
    }

    public void updateData(ArrayList<Event> eventsList) {
        this.events = eventsList;
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        TextView viewtitle;
        TextView viewdescription;
        TextView viewdate;
        ImageView viewImage;
        CardView cardViewContainer;

        EventViewHolder(View itemView) {
            super(itemView);
            viewtitle = (TextView) itemView.findViewById(R.id.titletext);
            viewdescription = (TextView) itemView.findViewById(R.id.descriptiontext);
            viewdate = (TextView) itemView.findViewById(R.id.datetext);
            viewImage = (ImageView) itemView.findViewById(R.id.imagecard);
            cardViewContainer = (CardView)itemView.findViewById(R.id.cardview);
        }
    }
}
