package de.telekom.dogsapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.telekom.dogsapp.model.DogBreed;

public class DetailViewModel extends ViewModel {

    public MutableLiveData<DogBreed> dogLiveData = new MutableLiveData<DogBreed>();

    public void fetch(){
        DogBreed dog1 = new DogBreed("1", "corgi", "15 years", "", "companionship", "calm and friendly", "");
        dogLiveData.setValue(dog1);
    }




}
