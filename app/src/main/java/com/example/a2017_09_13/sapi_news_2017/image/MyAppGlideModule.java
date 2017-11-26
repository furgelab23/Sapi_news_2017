package com.example.a2017_09_13.sapi_news_2017.image;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by 2017-09-13 on 11/11/2017.
 */
//Needed for Glide to operate correctly
@GlideModule
public final class MyAppGlideModule extends AppGlideModule {
    public MyAppGlideModule() {
        super();
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return super.isManifestParsingEnabled();
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        super.registerComponents(context, glide, registry);
    }
}