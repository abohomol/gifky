package com.abohomol.gifky.repository.retrofit;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface GifRetrofitService {

    String HOST = "http://api.giphy.com";

    @GET("v1/gifs/search")
    Observable<GifResponse> search(@Query("api_key") String apiKey,
                                   @Query("q") String query,
                                   @Query("offset") int offset);
}
