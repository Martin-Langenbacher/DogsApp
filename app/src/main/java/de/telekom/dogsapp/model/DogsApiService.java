package de.telekom.dogsapp.model;

import com.google.android.gms.tasks.CancellationTokenSource;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DogsApiService {

    private static final String BASE_URL = "https://raw.githubusercontent.com";

    private DogsApi api;

    public DogsApiService () {
        api = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(DogsApi.class);
    }

    public Single<List<DogBreed>> getDogs() {
        return api.getDogs();
    }

    // with the additional cat example from DogsApi
    //public Single<List<Cats>> getCats() {
    //    return api.getCats();
    //}
    // --> multiple End-Points, but the same BASE-URL...!


    //... now with dynamic URL:
    //public Single<List<Cats>> getCats(String url){
    //    return api.getCats(url);
    //}


}
