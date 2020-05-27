package de.telekom.dogsapp.view;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.palette.graphics.Palette;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.telekom.dogsapp.R;
import de.telekom.dogsapp.databinding.FragmentDetailBinding;
import de.telekom.dogsapp.databinding.SendSmsDialogBinding;
import de.telekom.dogsapp.databinding.SendSmsDialogBindingImpl;
import de.telekom.dogsapp.model.DogBreed;
import de.telekom.dogsapp.model.DogPalette;
import de.telekom.dogsapp.model.SmsInfo;
import de.telekom.dogsapp.util.Util;
import de.telekom.dogsapp.viewmodel.DetailViewModel;


public class DetailFragment extends Fragment {

    private int dogUuid;
    private DetailViewModel viewModel;
    private FragmentDetailBinding binding;

    private Boolean sendSmsStarted = false;

    private DogBreed currentDog;


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
        setHasOptionsMenu(true);   // for SMS (next line): --> We want to see a menu in this fragment...
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
                currentDog = dogBreed;
                binding.setDog(dogBreed);
                if (dogBreed.imageUrl != null){
                    setupBackgroundColor(dogBreed.imageUrl);
                }

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


    private void setupBackgroundColor(String url){
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Palette.from(resource)
                        .generate(palette -> {
                            int intColor = palette.getLightMutedSwatch().getRgb();
                            DogPalette myPalette = new DogPalette(intColor);
                            binding.setPalette(myPalette);
                        });
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send_sms: {
                if (!sendSmsStarted){
                    sendSmsStarted = true;
                    ((MainActivity) getActivity()).checkSmsPermission();
                }
                //Toast.makeText(getContext(), "Action send sms", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.action_share: {
                // We share text information, e.g. twitter, facebook, ... can handle that kind of information...
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this dog breed");
                intent.putExtra(Intent.EXTRA_TEXT, currentDog.dogBreed + " bred for " + currentDog.bredFor);
                intent.putExtra(Intent.EXTRA_STREAM, currentDog.imageUrl);
                startActivity(Intent.createChooser(intent, "Share With"));
                //Toast.makeText(getContext(), "Action share", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }



    public void onPermissionResult(Boolean permissionGranted){
        if (isAdded() && sendSmsStarted && permissionGranted){
            SmsInfo smsInfo = new SmsInfo("", currentDog.dogBreed + " bred for " + currentDog.bredFor, currentDog.imageUrl  );

            SendSmsDialogBinding dialogBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(getContext()),
                    R.layout.send_sms_dialog,
                    null,
                    false
            );

            new AlertDialog.Builder(getContext())
                    .setView(dialogBinding.getRoot())
                    .setPositiveButton("Send SMS", (((dialog, which) -> {
                        if (!dialogBinding.smsDestination.getText().toString().isEmpty()){
                            smsInfo.to = dialogBinding.smsDestination.getText().toString();
                            sendSMS(smsInfo);
                        }
                    })))
                    .setNegativeButton("Cancel", ((dialog, which) -> {}))
                    .show();
            sendSmsStarted = false;

            // dialog binding:
            dialogBinding.setSmsInfo(smsInfo);
        }
    }


    // you need a real device with a SIM card to test it.
    private void sendSMS(SmsInfo smsInfo){
        Intent intent = new Intent(getContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getContext(), 0, intent, 0  );
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(smsInfo.to, null, smsInfo.text, pi, null);
    }


}






