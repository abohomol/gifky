package com.abohomol.gifky.skeleton.dagger;

import android.content.Context;

import com.abohomol.gifky.GifListPresenter;
import com.abohomol.gifky.GifListView;
import com.abohomol.gifky.R;
import com.abohomol.gifky.repository.GifRepository;
import com.abohomol.gifky.repository.retrofit.GifRetrofitService;
import com.abohomol.gifky.repository.retrofit.RetrofitGifRepository;
import com.abohomol.gifky.skeleton.Presenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class GifListModule {

    private final Context context;

    public GifListModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    String provideApiKey() {
        return context.getString(R.string.giphy_public_beta_key);
    }

    @Provides
    @Singleton
    GifRetrofitService provideGifRetrofitService() {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(GifRetrofitService.HOST)
                .build();
        return retrofit.create(GifRetrofitService.class);
    }

    @Provides
    @Singleton
    GifRepository provideGifRepository(GifRetrofitService service, String apiKey) {
        return new RetrofitGifRepository(service, apiKey);
    }

    @Provides
    @Singleton
    Presenter<GifListView> providePresenter(GifRepository repository) {
        return new GifListPresenter(repository);
    }

}
