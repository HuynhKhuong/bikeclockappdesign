package com.example.bikemonitor.ui.home;

import android.bluetooth.BluetoothClass;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.bikemonitor.R;
import com.example.bikemonitor.combackground.ComComponent;
import com.example.bikemonitor.databinding.FragmentHomeBinding;
import com.example.bikemonitor.statemachine.DeviceConnectionStateManager;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

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