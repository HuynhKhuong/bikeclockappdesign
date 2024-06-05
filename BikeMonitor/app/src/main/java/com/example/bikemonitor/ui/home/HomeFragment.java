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

        final TextView speedValue = binding.speedValue;
        homeViewModel.getText().observe(getViewLifecycleOwner(), speedValue::setText);

        final TextView speedUnit = binding.speedUnit;
        speedUnit.setText(" km/h");

        final TextView parameterLegend = binding.additionalInformationLegend;
        parameterLegend.setText("ODO: ");
        final TextView parameterValue = binding.additionalInformationValue;
        parameterValue.setText("0000");
        final TextView parameterUnit = binding.additionalInformationUnit;
        parameterUnit.setText("km/h");

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