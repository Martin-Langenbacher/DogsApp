package de.telekom.dogsapp.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.telekom.dogsapp.R;


public class ListFragment extends Fragment {

    @BindView(R.id.floatingActionButton)
    FloatingActionButton fab;

    public ListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab.setOnClickListener(v -> { onGoToDetails();
        });
    }

    void onGoToDetails(){
        //NavDirections action = ListFragmentDirections.actionDetail();
        ListFragmentDirections.ActionDetail action = ListFragmentDirections.actionDetail();
        // actionDetail: we have defined it in dogs_navigation: "what to do when clicked...
        action.setDogUuid(5);
        Navigation.findNavController(fab).navigate(action);

    }




}

