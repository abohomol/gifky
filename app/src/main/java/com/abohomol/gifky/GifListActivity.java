package com.abohomol.gifky;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.abohomol.gifky.repository.GifItem;
import com.abohomol.gifky.skeleton.Presenter;
import com.abohomol.gifky.skeleton.dagger.DaggerGifListComponent;
import com.abohomol.gifky.skeleton.dagger.GifListModule;
import com.abohomol.gifky.tools.EndlessScrollListener;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import timber.log.Timber;

import static android.support.v7.widget.StaggeredGridLayoutManager.*;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class GifListActivity extends Activity implements GifListView {

    private static final int SPAN_COUNT = 3;
    private final PublishSubject<Integer> loadMoreItems = PublishSubject.create();
    private EditText searchField;
    private GifListAdapter gifListAdapter;
    private EndlessScrollListener scrollListener;
    @Inject Presenter<GifListView> presenter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setLayoutParams(new LayoutParams(MATCH_PARENT, MATCH_PARENT));
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        initSearchField(rootLayout);
        initGifList(rootLayout);
        setContentView(rootLayout);

        initLoggingSystem();
        buildDependencyGraph();
        presenter.attachView(this);
    }

    private void initLoggingSystem() {
        Timber.plant(new Timber.DebugTree());
    }

    private void buildDependencyGraph() {
        DaggerGifListComponent
                .builder()
                .gifListModule(new GifListModule(this))
                .build().inject(this);
    }

    private void initSearchField(ViewGroup parent) {
        searchField = new EditText(this);
        searchField.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        searchField.setMinLines(1);
        parent.addView(searchField);
    }

    private void initGifList(ViewGroup parent) {
        RecyclerView gifList = new RecyclerView(this);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, VERTICAL);
        gifList.setLayoutManager(layoutManager);
        gifListAdapter = new GifListAdapter(this);
        gifList.setAdapter(gifListAdapter);
        gifList.setLayoutParams(new LayoutParams(MATCH_PARENT, MATCH_PARENT));
        scrollListener = new EndlessScrollListener(layoutManager) {
            @Override public void onLoadMore() {
                int itemCount = gifListAdapter.getItemCount();
                loadMoreItems.onNext(itemCount);
            }
        };
        gifList.addOnScrollListener(scrollListener);
        parent.addView(gifList);
    }

    @Override public void showMoreGifItems(List<GifItem> items) {
        gifListAdapter.addItems(items);
    }

    @Override public void showSearchResults(List<GifItem> items) {
        gifListAdapter.replaceItems(items);
    }

    @Override public Observable<String> searchObservable() {
        return RxTextView.afterTextChangeEvents(searchField)
                .map(new Func1<TextViewAfterTextChangeEvent, String>() {
                    @Override public String call(TextViewAfterTextChangeEvent event) {
                        scrollListener.reset();
                        /**
                         * Trim is needed to avoid passing strings that contain spaces only.
                         */
                        return event.editable().toString().trim();
                    }
                });
    }

    @Override public Observable<Integer> endOfListReachedObservable() {
        return loadMoreItems;
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
