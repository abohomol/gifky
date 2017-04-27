package com.abohomol.gifky;

import com.abohomol.gifky.repository.GifItem;
import com.abohomol.gifky.repository.GifRepository;
import com.abohomol.gifky.skeleton.BasePresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class GifListPresenter extends BasePresenter<GifListView> {

    private static final int SEARCH_PROCESSING_INTERVAL = 500;
    private static final String DEFAULT_SEARCH_QUERY = "random";

    private String lastSearchQuery = DEFAULT_SEARCH_QUERY;

    private final CompositeSubscription subscriptions = new CompositeSubscription();
    private final GifRepository repository;

    public GifListPresenter(GifRepository repository) {
        this.repository = repository;
    }

    @Override public void attachView(GifListView view) {
        super.attachView(view);
        subscribeForSearchRequests();
        subscribeForLoadMoreEvents();
        loadInitialTrendingItems();
    }

    private void subscribeForSearchRequests() {
        subscriptions.add(getView()
                .searchObservable()
                .debounce(SEARCH_PROCESSING_INTERVAL, TimeUnit.MILLISECONDS)
                .map(new Func1<String, String>() {
                    @Override public String call(String query) {
                        lastSearchQuery = query.isEmpty() ? DEFAULT_SEARCH_QUERY : query;
                        return lastSearchQuery;
                    }
                })
                .flatMap(new Func1<String, Observable<List<GifItem>>>() {
                    @Override public Observable<List<GifItem>> call(String query) {
                        return repository.search(query, 0);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<GifItem>>() {
                    @Override public void call(List<GifItem> items) {
                        getView().showSearchResults(items);
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable e) {
                        Timber.e(e, "Unable to process search request");
                    }
                }));
    }

    private void subscribeForLoadMoreEvents() {
        subscriptions.add(getView()
                .endOfListReachedObservable()
                .flatMap(new Func1<Integer, Observable<List<GifItem>>>() {
                    @Override public Observable<List<GifItem>> call(Integer offset) {
                        return repository.search(lastSearchQuery, offset);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<GifItem>>() {
                    @Override public void call(List<GifItem> items) {
                        getView().showMoreGifItems(items);
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable e) {
                        Timber.e(e, "Unable to acquire list of trending gifs");
                    }
                }));
    }

    private void loadInitialTrendingItems() {
        subscriptions.add(repository
                .search(lastSearchQuery, 0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<GifItem>>() {
                    @Override public void call(List<GifItem> items) {
                        getView().showMoreGifItems(items);
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable e) {
                        Timber.e(e, "Unable to acquire list of trending gifs");
                    }
                }));
    }

    @Override public void detachView() {
        subscriptions.clear();
        super.detachView();
    }

}
