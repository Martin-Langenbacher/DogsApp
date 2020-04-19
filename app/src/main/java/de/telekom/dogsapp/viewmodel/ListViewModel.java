package de.telekom.dogsapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import de.telekom.dogsapp.model.DogBreed;
import de.telekom.dogsapp.model.DogsApiService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ListViewModel extends AndroidViewModel {

    // Mutable: We can change the LiveDate when we want it...
    // !!! --> is needed: <List<DogBreed>>
    public MutableLiveData<List<DogBreed>> dogs = new MutableLiveData<List<DogBreed>>();
    public MutableLiveData<Boolean> dogLoadError = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<Boolean>();
    // one view should match to one view model

    // this was created and is available in "DogsApiService.java" -->
    private DogsApiService dogsService = new DogsApiService();
    private CompositeDisposable disposable = new CompositeDisposable();

    public ListViewModel(@NonNull Application application) {
        super(application);
    }

    /* --> Old version - not needed with access of Retrofit !!!
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
    } */

    // NEW with RETROFIT
    public void refresh() {
        fetchFromRemote();
    }


    /*
    ==> CTRL ALT L ---> to format it nicely !!!!
    */


    private void fetchFromRemote() {
        loading.setValue(true);
        disposable.add(
                // explanation in the video @ 3:40, chapter 34, Abschnitt 7: Retrofit...
                dogsService.getDogs()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<DogBreed>>() {
                            @Override
                            public void onSuccess(List<DogBreed> dogBreeds) {
                                dogs.setValue(dogBreeds);
                                dogLoadError.setValue(false);
                                loading.setValue(false);
                            }

                            @Override
                            public void onError(Throwable e) {
                                dogLoadError.setValue(true);
                                loading.setValue(false);
                                e.printStackTrace();
                            }
                        })
        );
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
