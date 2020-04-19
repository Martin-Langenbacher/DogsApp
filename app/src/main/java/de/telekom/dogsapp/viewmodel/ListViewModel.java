package de.telekom.dogsapp.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import de.telekom.dogsapp.model.DogBreed;
import de.telekom.dogsapp.model.DogDao;
import de.telekom.dogsapp.model.DogDatabase;
import de.telekom.dogsapp.model.DogsApiService;
import de.telekom.dogsapp.util.SharedPreferencesHelper;
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

    private AsyncTask<List<DogBreed>, Void, List<DogBreed>> insertTask;
    private AsyncTask<Void, Void, List<DogBreed>> retrieveTask;

    private SharedPreferencesHelper prefHelper = SharedPreferencesHelper.getInstance(getApplication());
    private long refreshTime = 5 * 60 * 1000 * 1000 * 1000L; // we need nano-Seconds (our example: 5Min.

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
    //public void refresh() { fetchFromRemote(); }
    //public void refresh() { fetchFromDatabase(); }
    public void refresh(){
        long updateTime = prefHelper.getUpdateTime(); // last time we updated the information
        long currentTime = System.nanoTime();
        if (updateTime != 0 && currentTime - updateTime < refreshTime){
            fetchFromDatabase();
        } else {
            fetchFromRemote();
        }
    }


    public void refreshBypassCache(){
        fetchFromRemote();
    }



    /*
    ==> CTRL ALT L ---> to format it nicely !!!!
    */




    private void fetchFromDatabase(){
        loading.setValue(true);
        retrieveTask = new RetrieveDogsTask();
        retrieveTask.execute();
    }


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
                                insertTask = new InsertDogsTask();
                                insertTask.execute(dogBreeds);
                                Toast.makeText(getApplication(), "Dogs retrieved from endpoint", Toast.LENGTH_SHORT).show();
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

    // This will only be called, whe the data is in the database
    private void dogsRetrieved(List<DogBreed> dogList){
        dogs.setValue(dogList);
        dogLoadError.setValue(false);
        loading.setValue(false);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();

        if (insertTask != null){
            insertTask.cancel(true);
            insertTask = null;
            // with this, we avoid memory crashes!
        }

        if (retrieveTask != null) {
            retrieveTask.cancel(true);
            retrieveTask = null;
        }
    }


    private class InsertDogsTask extends AsyncTask<List<DogBreed>, Void, List<DogBreed>>{

        // this is in a background thread
        @Override
        protected List<DogBreed> doInBackground(List<DogBreed>... lists) {
            List<DogBreed> list = lists[0];
            DogDao dao = DogDatabase.getInstance(getApplication()).dogDao();
            dao.deleteAllDogs();

            ArrayList<DogBreed> newList = new ArrayList<>(list);
            List<Long> result = dao.insertAll(newList.toArray(new DogBreed[0]));

            int i = 0;
            while (i < list.size()) {
                //list.get(i).uuid = result.get(i);  //  -->   this need to be changed... because of Long
                list.get(i).uuid = result.get(i).intValue();
                i++;
            }
            return list;
        }

        // now in a fore-ground thread:
        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds){
            dogsRetrieved(dogBreeds);
            prefHelper.saveUpdateTime(System.nanoTime());
        }
    }

    // the first two parameter we don't need, therefor: Void !!!
    private class RetrieveDogsTask extends AsyncTask<Void, Void, List<DogBreed>>{

        @Override
        protected List<DogBreed> doInBackground(Void... voids) {
            return DogDatabase.getInstance(getApplication()).dogDao().getAllDogs();
        }

        // now on front-ground:


        @Override
        protected void onPostExecute(List<DogBreed> dogBreeds) {
            dogsRetrieved(dogBreeds);
            Toast.makeText(getApplication(), "Dogs retrieved from database", Toast.LENGTH_SHORT).show();
        }


    }

}
