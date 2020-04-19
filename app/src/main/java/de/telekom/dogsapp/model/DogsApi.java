package de.telekom.dogsapp.model;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface DogsApi {
    @GET("DevTides/DogsApi/master/dogs.json")
    Single<List<DogBreed>> getDogs();

    // if we would have more end-points - e.g. cats...
    //@GET("DevTides/DogsApi/master/cats.json")
    //Single<List<Cats>> getCats();

    //... with dynamic URL...
    //@GET()
    //Single<List<Cats>> getCats(@Url String url);


}
