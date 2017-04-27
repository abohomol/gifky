package com.abohomol.gifky;

import com.abohomol.gifky.repository.GifItem;
import com.abohomol.gifky.repository.GifRepository;
import com.abohomol.gifky.skeleton.Presenter;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GifListPresenterTest {

    private GifListView view;
    private GifRepository repository;
    private PublishSubject<String> textChangeDataSource = PublishSubject.create();
    private PublishSubject<Integer> loadMoreDataSource = PublishSubject.create();

    @Before
    public void setUp() {
        view = mock(GifListView.class);
        repository = mock(GifRepository.class);
        when(view.searchObservable()).thenReturn(textChangeDataSource);
        when(view.endOfListReachedObservable()).thenReturn(loadMoreDataSource);
    }

    @Test
    public void shouldQueryInitialGifListWithDefaultParamsOnViewAttach() {
        when(repository.search(anyString(), anyInt())).thenReturn(Observable.<List<GifItem>>empty());
        Presenter<GifListView> presenter = new GifListPresenter(repository);
        presenter.attachView(view);
        verify(repository).search("random", 0);
    }
}
