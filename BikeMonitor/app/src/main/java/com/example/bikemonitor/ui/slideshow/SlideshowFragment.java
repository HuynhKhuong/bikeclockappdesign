package com.example.bikemonitor.ui.slideshow;

import android.bluetooth.BluetoothClass;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.bikemonitor.R;
import com.example.bikemonitor.bluetoothbackgroundsetup.BluetoothConnectionSetup;
import com.example.bikemonitor.combackground.ComComponent;
import com.example.bikemonitor.databinding.FragmentSlideshowBinding;
import com.example.bikemonitor.statemachine.DeviceConnectionStateManager;
import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton;

import java.util.ArrayList;
import java.util.Arrays;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private ArrayList<String> m_wheelsOptions = new ArrayList<String>(Arrays.asList(new String[]{
            "20x1.25 inch",
            "24x1.5 inch",
            "26x1.75 inch",
            "27.5x2.0 inch",
            "29x2.2 inch",
            "700x23c",
            "700x25c",
            "700x27c",
            "700x32c",
            "700x35c"
            }));
    private ArrayAdapter<String> m_wheelsOptionsArrayAdapter;

    private final AdapterView.OnItemSelectedListener mOptionClickListener = new AdapterView.OnItemSelectedListener() {

        public void onItemSelected(AdapterView<?> av, View v, int arg2, long arg3) {
                ComComponent.getComComponent().setTargetSettingsUpdate(arg2);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private class customOnBackPressed extends OnBackPressedCallback {
        customOnBackPressed(){
            super(true);
        }

        @Override
        public void handleOnBackPressed(){
            //by default do nothing
            //no Navigator.popStack() is called
        }
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(requireActivity()).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Spinner wheelSizeOptionList = binding.wheelsizeOptions;

        m_wheelsOptionsArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.option_name, m_wheelsOptions);
        wheelSizeOptionList.setAdapter(m_wheelsOptionsArrayAdapter);
        wheelSizeOptionList.setOnItemSelectedListener(mOptionClickListener);

        final TextView textView = binding.textSlideshow;
        CircularProgressButton setupButton = binding.cirSetupButton;
        setupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupButton.startAnimation();
                ////Check current Connection State
                if(DeviceConnectionStateManager.getDeviceConnectionStateManager().getCurrentState() ==
                        DeviceConnectionStateManager.DEVICE_ACCEPTED){
                    DeviceConnectionStateManager.getDeviceConnectionStateManager().updateState(
                            DeviceConnectionStateManager.DEVICE_ACCEPTED_UPDATESETTINGS
                    );
                }

            }
        });

        slideshowViewModel.getbuttonAnimationStart().observe(getViewLifecycleOwner(),
                new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        setupButton.revertAnimation();
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