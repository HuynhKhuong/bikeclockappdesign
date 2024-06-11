package com.example.bikemonitor.ui.home;

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
import com.example.bikemonitor.databinding.FragmentHomeBinding;

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
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.speedValue.setText(homeViewModel.getDisplayCurrentSpeedValue());
        binding.speedUnit.setText(homeViewModel.getDisplayCurrentSpeedUnit());


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



        Button additionalInformationMenu = binding.additionalInformationSelectbutton;
        additionalInformationMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeViewModel.updateCurrentUnit();
            }
        });

        Button lockIndicator = binding.lockindicator;
        lockIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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