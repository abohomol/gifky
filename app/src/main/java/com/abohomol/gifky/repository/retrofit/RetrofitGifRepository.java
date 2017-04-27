package com.abohomol.gifky.repository.retrofit;

import com.abohomol.gifky.repository.GifItem;
import com.abohomol.gifky.repository.GifRepository;
import com.abohomol.gifky.repository.retrofit.GifResponse.GifResponseItem;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class RetrofitGifRepository implements GifRepository {

    private final GifRetrofitService service;
    private final String apiKey;

    public RetrofitGifRepository(GifRetrofitService service, String apiKey) {
        this.service = service;
        this.apiKey = apiKey;
    }

    @Override public Observable<List<GifItem>> search(String query, int offset) {
        Timber.d("Searching for gifs with %s key and %d offset", query, offset);
        return service
                .search(apiKey, query, offset)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<GifResponse, Observable<GifResponseItem>>() {
                    @Override public Observable<GifResponseItem> call(GifResponse gifResponse) {
                        return Observable.from(gifResponse.getData());
                    }
                })
                .map(new Func1<GifResponseItem, GifItem>() {
                    @Override public GifItem call(GifResponseItem responseItem) {
                        return new GifItem(responseItem.getSource(), responseItem.getUrl());
                    }
                })
                .toList();
    }

}
