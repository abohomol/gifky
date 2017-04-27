package com.abohomol.gifky;

import com.abohomol.gifky.repository.GifItem;
import com.abohomol.gifky.skeleton.View;

import java.util.List;

import rx.Observable;

public interface GifListView extends View {

    void showMoreGifItems(List<GifItem> items);
    void showSearchResults(List<GifItem> items);

    Observable<String> searchObservable();
    Observable<Integer> endOfListReachedObservable();
}
