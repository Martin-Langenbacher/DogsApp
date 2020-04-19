package de.telekom.dogsapp.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.telekom.dogsapp.model.DogBreed;
import de.telekom.dogsapp.model.DogDatabase;

public class DetailViewModel extends AndroidViewModel {

    public MutableLiveData<DogBreed> dogLiveData = new MutableLiveData<DogBreed>();
    private RetrieveDogTask task;// because we work with Async-Tasks

    public DetailViewModel(@NonNull Application application) {
        super(application);
    }

    /* //-->remove this one detail dog (for learning the previous lectures only)...
    public void fetch(){
        DogBreed dog1 = new DogBreed("1", "corgi", "15 years", "", "companionship", "calm and friendly", "");
        dogLiveData.setValue(dog1);
    } */
    public void fetch(int uuid) {
        task = new RetrieveDogTask();
        task.execute(uuid);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        if (task != null) {
            task.cancel(true);
            task = null;
        }
    }


    private class RetrieveDogTask extends AsyncTask<Integer, Void, DogBreed> {

        @Override
        protected DogBreed doInBackground(Integer... integers) {
            int uuid = integers[0];
            return DogDatabase.getInstance(getApplication()).dogDao().getDog(uuid);
        }

        // update our live-Data:
        @Override
        protected void onPostExecute(DogBreed dogBreed) {
            dogLiveData.setValue(dogBreed);

        }
    }


/*
    ==> CTRL ALT L ---> to format it nicely !!!!
    */


}
