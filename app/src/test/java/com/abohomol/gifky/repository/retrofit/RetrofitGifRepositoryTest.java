package com.abohomol.gifky.repository.retrofit;

import org.junit.Before;
import org.junit.Test;

import rx.Observable;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RetrofitGifRepositoryTest {

    private GifRetrofitService service;

    @Before
    public void setUp() {
        service = mock(GifRetrofitService.class);
        when(service.search(anyString(), anyString(), anyInt()))
                .thenReturn(Observable.<GifResponse>empty());
    }

    @Test
    public void shouldCallServiceApiWithSpecifiedApiKey() {
        String apiKey = "test_api_key";
        RetrofitGifRepository repository = new RetrofitGifRepository(service, apiKey);
        repository.search("test_query", 0);
        verify(service).search(eq(apiKey), anyString(), anyInt());
    }

    @Test
    public void shouldCallServiceApiWithCorrectArguments() {
        RetrofitGifRepository repository = new RetrofitGifRepository(service, "");
        String query = "test_query";
        int offset = 177;
        repository.search(query, offset);
        verify(service).search(anyString(), eq(query), eq(offset));
    }
}
