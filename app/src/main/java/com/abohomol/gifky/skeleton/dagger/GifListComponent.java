package com.abohomol.gifky.skeleton.dagger;

import com.abohomol.gifky.GifListActivity;
import com.abohomol.gifky.GifListView;
import com.abohomol.gifky.skeleton.Presenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = GifListModule.class)
public interface GifListComponent {

    void inject(GifListActivity activity);

    Presenter<GifListView> presenter();
}
