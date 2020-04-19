package de.telekom.dogsapp.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.telekom.dogsapp.R;
import de.telekom.dogsapp.databinding.FragmentDetailBinding;
import de.telekom.dogsapp.model.DogBreed;
import de.telekom.dogsapp.util.Util;
import de.telekom.dogsapp.viewmodel.DetailViewModel;


public class DetailFragment extends Fragment {

    private int dogUuid;
    private DetailViewModel viewModel;
    private FragmentDetailBinding binding;


    /* ---> NOT needed anymore because of DataBinding in xml!    ===============================>>>
    @BindView(R.id.dogImage)
    ImageView dogImage;

    @BindView(R.id.dogName)
    TextView dogName;

    @BindView(R.id.dogPurpose)
    TextView dogPurpose;

    @BindView(R.id.dogTemperament)
    TextView dogTemperament;

    @BindView(R.id.dogLifespan)
    TextView dogLifespan;

     =============================================================================================*/



    public DetailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        this.binding = binding;

        //Example: --> binding.dogName.setText("Some text"); // like this we have access to our code here...

        // ---> Delete because of DataBinding ====================================================>>
        //View view = inflater.inflate(R.layout.fragment_detail, container, false);
        //ButterKnife.bind(this, view);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments() != null) {
            dogUuid = DetailFragmentArgs.fromBundle(getArguments()).getDogUuid();
        }

        viewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        viewModel.fetch(dogUuid);

        observeViewModel();

    }

    private void observeViewModel () {
        viewModel.dogLiveData.observe(this, dogBreed -> {
            if(dogBreed != null && dogBreed instanceof DogBreed && getContext() != null){

                binding.setDog(dogBreed);

                /* ---> Delete because of DataBinding ============================================>>
                dogName.setText(dogBreed.dogBreed);
                dogPurpose.setText(dogBreed.bredFor);
                dogTemperament.setText(dogBreed.temperament);
                dogLifespan.setText(dogBreed.lifeSpan);

                if(dogBreed.imageUrl != null){
                    Util.loadImage(dogImage, dogBreed.imageUrl, new CircularProgressDrawable(getContext()));
                } */
            }
        });
    }


}


