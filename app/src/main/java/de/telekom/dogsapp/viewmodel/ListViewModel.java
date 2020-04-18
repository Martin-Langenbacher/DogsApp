package de.telekom.dogsapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import de.telekom.dogsapp.model.DogBreed;

public class ListViewModel extends AndroidViewModel {

    // Mutable: We can change the LiveDate when we want it...
    // !!! --> is needed: <List<DogBreed>>
    public MutableLiveData<List<DogBreed>> dogs = new MutableLiveData<List<DogBreed>>();
    public MutableLiveData<Boolean> dogLoadError = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<Boolean>();
    // one view should match to one view model

    public ListViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh(){
        DogBreed dog1 = new DogBreed("1", "corgi", "15 years", "", "", "", "");
        DogBreed dog2 = new DogBreed("2", "rotwailler", "10 years", "", "", "", "");
        DogBreed dog3 = new DogBreed("1", "labrador", "13 years", "", "", "", "");
        DogBreed dog4 = new DogBreed("1", "corgi", "15 years", "", "", "", "");
        DogBreed dog5 = new DogBreed("2", "rotwailler", "10 years", "", "", "", "");
        DogBreed dog6 = new DogBreed("1", "labrador", "13 years", "", "", "", "");
        DogBreed dog7 = new DogBreed("1", "corgi", "15 years", "", "", "", "");
        DogBreed dog8 = new DogBreed("2", "rotwailler", "10 years", "", "", "", "");
        DogBreed dog9 = new DogBreed("1", "labrador", "13 years", "", "", "", "");
        ArrayList<DogBreed> dogsList = new ArrayList<>();
        dogsList.add(dog1);
        dogsList.add(dog2);
        dogsList.add(dog3);
        dogsList.add(dog4);
        dogsList.add(dog5);
        dogsList.add(dog6);
        dogsList.add(dog7);
        dogsList.add(dog8);
        dogsList.add(dog9);

        dogs.setValue(dogsList);
        dogLoadError.setValue(false);
        loading.setValue(false);
    }
}
