package com.example.bikemonitor.ui.home;

import android.bluetooth.BluetoothClass;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;

import com.example.bikemonitor.bluetoothbackgroundsetup.DataContainer;
import com.example.bikemonitor.databinding.FragmentHomeBinding;
import com.example.bikemonitor.statemachine.DeviceConnectionStateManager;
import com.example.bikemonitor.ui.gallery.GalleryViewModel;

public class HomeFragment extends Fragment {

    private class customOnBackPressed extends OnBackPressedCallback{
        customOnBackPressed(){
            super(true);
        }

        @Override
        public void handleOnBackPressed(){
            //by default do nothing
            //no Navigator.popStack() is called
        }
    }
    private FragmentHomeBinding binding;
    private int startRecordTimeStamp;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        DataContainer cloudPort =
                new ViewModelProvider(requireActivity()).get(DataContainer.class);
        GalleryViewModel reportDisplayPort =
                new ViewModelProvider(requireActivity()).get(GalleryViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.speedValue.setText(homeViewModel.getDisplayCurrentSpeedValue());
        binding.speedUnit.setText(homeViewModel.getDisplayCurrentSpeedUnit());
        homeViewModel.getMainSpeedDisplay().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.speedValue.setText(homeViewModel.getDisplayCurrentSpeedValue());
                binding.speedUnit.setText(homeViewModel.getDisplayCurrentSpeedUnit());
            }
        });

        binding.additionalInformationLegend.setText(homeViewModel.getDisplayLegend());
        binding.additionalInformationValue.setText(homeViewModel.getDisplayValue());
        binding.additionalInformationUnit.setText(homeViewModel.getDisplayUnit());
        homeViewModel.getCurrentIndex().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.additionalInformationLegend.setText(homeViewModel.getDisplayLegend());
                binding.additionalInformationValue.setText(homeViewModel.getDisplayValue());
                binding.additionalInformationUnit.setText(homeViewModel.getDisplayUnit());
            }
        });

        homeViewModel.getAdditionalAvgSpdDisplay().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double Value) {
                binding.additionalInformationLegend.setText(homeViewModel.getDisplayLegend());
                binding.additionalInformationValue.setText(homeViewModel.getDisplayValue());
                binding.additionalInformationUnit.setText(homeViewModel.getDisplayUnit());
            }
        });
        homeViewModel.getAdditionalOdoDisplay().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer Value) {
                binding.additionalInformationLegend.setText(homeViewModel.getDisplayLegend());
                binding.additionalInformationValue.setText(homeViewModel.getDisplayValue());
                binding.additionalInformationUnit.setText(homeViewModel.getDisplayUnit());
            }
        });
        homeViewModel.getAdditionalTimeDisplay().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer Value) {
                binding.additionalInformationLegend.setText(homeViewModel.getDisplayLegend());
                binding.additionalInformationValue.setText(homeViewModel.getDisplayValue());
                binding.additionalInformationUnit.setText(homeViewModel.getDisplayUnit());
            }
        });
        homeViewModel.getAdditionalTripDisplay().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer Value) {
                binding.additionalInformationLegend.setText(homeViewModel.getDisplayLegend());
                binding.additionalInformationValue.setText(homeViewModel.getDisplayValue());
                binding.additionalInformationUnit.setText(homeViewModel.getDisplayUnit());
            }
        });

        Button additionalInformationMenu = binding.additionalInformationSelectbutton;
        additionalInformationMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeViewModel.updateCurrentUnit();
            }
        });

        binding.lockindicatorLock.setVisibility(View.VISIBLE);
        binding.lockindicatorUnlock.setVisibility(View.GONE);
        homeViewModel.getLockIndicator().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean status) {
                if(status){
                    binding.lockindicatorLock.setVisibility(View.VISIBLE);
                    binding.lockindicatorUnlock.setVisibility(View.GONE);
                }
                else{
                    binding.lockindicatorLock.setVisibility(View.GONE);
                    binding.lockindicatorUnlock.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.lockindicatorLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DeviceConnectionStateManager.getDeviceConnectionStateManager().getCurrentState() !=
                        DeviceConnectionStateManager.DEVICE_DISCONNECTED){
                DeviceConnectionStateManager.getDeviceConnectionStateManager().updateState(
                        DeviceConnectionStateManager.DEVICE_FORCEUNLOCK
                );
            }
        }});

        Button recordButtonPlay = binding.recorderStart;
        Button recordButtonPause = binding.recorderStop;

        recordButtonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //user pressed pause recoding
                Calendar timeStampOnClockPauseButton = Calendar.getInstance();

                int timeDuration = ((int)timeStampOnClockPauseButton.getTimeInMillis()/1000 - startRecordTimeStamp)/(60);

                Log.d("", "Time = " + Double.toString(timeDuration));
                cloudPort.getCloudData().setActivePeriod(timeDuration);

                cloudPort.getCloudData().setUserDistance(
                        cloudPort.getCloudData().getUserDistance() -
                        homeViewModel.getAdditionalOdoDisplay().getValue());

                cloudPort.notifyDataChange();
                reportDisplayPort.notifyDataChange();
                homeViewModel.setRecorderStartFlag(false);
            }
        });

        recordButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DeviceConnectionStateManager.getDeviceConnectionStateManager().getCurrentState() ==
                        DeviceConnectionStateManager.DEVICE_ACCEPTED){
                    //user pressed play recording
                    Calendar timeStampOnClickPlayButton = Calendar.getInstance();
                    startRecordTimeStamp = (int)(timeStampOnClickPlayButton.getTimeInMillis()/1000);
                    cloudPort.getCloudData().setDayRec(timeStampOnClickPlayButton.get(Calendar.DAY_OF_MONTH));
                    cloudPort.getCloudData().setMonRec(timeStampOnClickPlayButton.get(Calendar.MONTH) + 1);
                    cloudPort.getCloudData().setHourRec(timeStampOnClickPlayButton.get(Calendar.HOUR_OF_DAY));
                    cloudPort.getCloudData().setMinRec(timeStampOnClickPlayButton.get(Calendar.MINUTE));

                    cloudPort.getCloudData().setUserDistance(homeViewModel.getAdditionalOdoDisplay().getValue());
                    cloudPort.notifyDataChange();
                    reportDisplayPort.notifyDataChange();
                    homeViewModel.setRecorderStartFlag(true);
                }
            }
        });

        homeViewModel.getRecorderStartFlag().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    //recorder play
                    recordButtonPause.setVisibility(View.VISIBLE);
                    recordButtonPlay.setVisibility(View.GONE);
                }
                else{
                    //recorder pause
                    recordButtonPause.setVisibility(View.GONE);
                    recordButtonPlay.setVisibility(View.VISIBLE);
                }
            }
        });
        OnBackPressedCallback backGesture = new customOnBackPressed();
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), backGesture);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}