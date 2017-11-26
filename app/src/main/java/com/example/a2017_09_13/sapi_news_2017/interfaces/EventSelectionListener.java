package com.example.a2017_09_13.sapi_news_2017.interfaces;

import android.net.Uri;
import android.view.View;

import com.example.a2017_09_13.sapi_news_2017.model.Event;

/**
 * Created by 2017-09-13 on 11/16/2017.
 */

public interface EventSelectionListener {
    void onEventClicked(Event event, Uri uri,View view);
}
