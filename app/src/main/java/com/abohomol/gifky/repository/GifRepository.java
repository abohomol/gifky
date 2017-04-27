package com.abohomol.gifky.repository;

import java.util.List;

import rx.Observable;

public interface GifRepository {

    Observable<List<GifItem>> search(String query, int offset);
}
