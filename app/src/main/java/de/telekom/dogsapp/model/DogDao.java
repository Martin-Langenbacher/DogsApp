package de.telekom.dogsapp.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DogDao {
    @Insert
    List<Long> insertAll(DogBreed... dogs);
    // "..." means we can provide as many dog variables as we like...
    // --> this is a list of all uuid

    @Query("SELECT * FROM dogbreed")
    List<DogBreed> getAllDogs();

    @Query(("SELECT * FROM dogbreed WHERE uuid = :dogId"))
    DogBreed getDog(int dogId);

    @Query("DELETE FROM dogbreed")
    void deleteAllDogs();


}
