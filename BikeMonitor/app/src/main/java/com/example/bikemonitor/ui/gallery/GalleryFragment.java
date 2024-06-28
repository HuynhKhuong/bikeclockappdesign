package com.example.bikemonitor.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.bikemonitor.R;
import com.example.bikemonitor.bluetoothbackgroundsetup.DataContainer;
import com.example.bikemonitor.combackground.ComComponent;
import com.example.bikemonitor.databinding.FragmentGalleryBinding;
import com.example.bikemonitor.databinding.LogoutButtonBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    private ArrayList<String> m_reportsOptions = new ArrayList<String>(Arrays.asList(new String[]{}));
    private int m_chosenReportIndx = 0;
    private ArrayAdapter<String> m_reportsOptionsArrayAdapter;

    private final AdapterView.OnItemSelectedListener mOptionClickListener = new AdapterView.OnItemSelectedListener() {

        public void onItemSelected(AdapterView<?> av, View v, int arg2, long arg3) {
            m_chosenReportIndx = arg2;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(requireActivity()).get(GalleryViewModel.class);

        DataContainer dataPort =
                new ViewModelProvider(requireActivity()).get(DataContainer.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ///Initialize!!!!

        //Report list fill up
        Spinner reportOptionList = binding.reportlistOptions;

        final int Id = dataPort.getCurrentRequestedId();
        int currentIndx = 0;
        if(currentIndx == Id){
            m_reportsOptions.add("Record_0");
        }
        else{
            while(currentIndx <= Id){
                m_reportsOptions.add("Record_" + Integer.toString(currentIndx));
                currentIndx++;
            }
        }


        m_reportsOptionsArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.option_name, m_reportsOptions);
        reportOptionList.setAdapter(m_reportsOptionsArrayAdapter);
        reportOptionList.setOnItemSelectedListener(mOptionClickListener);

        //Display delta Odo
        int deltaOdo = dataPort.getCloudData().getUserDistance();
        String displayString = "";
        if(deltaOdo < 10){
            displayString = "0" + Integer.toString(deltaOdo);
        }
        else {
            displayString = Integer.toString(deltaOdo);
        }

        binding.deltaOdoValue.setText(displayString);

        //Display time in minutes
        double deltaTime = dataPort.getCloudData().getActivePeriod();
        int convertedDeltaTime = (int)deltaTime;
        if(convertedDeltaTime < 10){
            displayString = "0" + Integer.toString(convertedDeltaTime);
        }
        else {
            displayString = Integer.toString(convertedDeltaTime);
        }
        binding.deltaTimeValue.setText(displayString);

        int timeStampHour = dataPort.getCloudData().getHourRec();
        if(timeStampHour < 10){
            displayString = "0" + Integer.toString(timeStampHour);
        }
        else{
            displayString = Integer.toString(timeStampHour);
        }
        displayString += " : ";
        int timeStampMinute = dataPort.getCloudData().getMinRec();
        if(timeStampMinute < 10){
            displayString += "0" + Integer.toString(timeStampMinute);
        }
        else{
            displayString += Integer.toString(timeStampMinute);
        }

        binding.timeStampValue.setText(displayString);

        int timeStampDate = dataPort.getCloudData().getDayRec();
        if(timeStampDate < 10){
            displayString = "0" + Integer.toString(timeStampDate);
        }
        else{
            displayString = Integer.toString(timeStampDate);
        }
        displayString += " / ";
        int timeStampMonth = dataPort.getCloudData().getMonRec();
        if(timeStampMonth < 10){
            displayString += "0" + Integer.toString(timeStampMonth);
        }
        else{
            displayString += Integer.toString(timeStampMonth);
        }
        binding.timeStampValue1.setText(displayString);

        dataPort.getIndexNotifier().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer aInteger) {
                Log.d(" ", "Index updated");
                int currentIndx = 0;
                m_reportsOptions.clear();
                while(currentIndx <= aInteger){
                    m_reportsOptions.add("Record_" + Integer.toString(currentIndx));
                    currentIndx++;
                }

                m_reportsOptionsArrayAdapter.notifyDataSetChanged();
            }
        });

        galleryViewModel.getDataChangeNotifier().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                //Display delta Odo
                int deltaOdo = dataPort.getCloudData().getUserDistance();
                String displayString = "";
                if(deltaOdo < 10){
                    displayString = "0" + Integer.toString(deltaOdo);
                }
                else {
                    displayString = Integer.toString(deltaOdo);
                }

                binding.deltaOdoValue.setText(displayString);

                //Display time in minutes
                double deltaTime = dataPort.getCloudData().getActivePeriod();
                int convertedDeltaTime = (int)deltaTime;
                if(convertedDeltaTime < 10){
                    displayString = "0" + Integer.toString(convertedDeltaTime);
                }
                else {
                    displayString = Integer.toString(convertedDeltaTime);
                }
                binding.deltaTimeValue.setText(displayString);

                int timeStampHour = dataPort.getCloudData().getHourRec();
                if(timeStampHour < 10){
                    displayString = "0" + Integer.toString(timeStampHour);
                }
                else{
                    displayString = Integer.toString(timeStampHour);
                }
                displayString += " : ";
                int timeStampMinute = dataPort.getCloudData().getMinRec();
                if(timeStampMinute < 10){
                        displayString += "0" + Integer.toString(timeStampMinute);
                }
                else{
                        displayString += Integer.toString(timeStampMinute);
                }

                binding.timeStampValue.setText(displayString);

                int timeStampDate = dataPort.getCloudData().getDayRec();
                if(timeStampDate < 10){
                    displayString = "0" + Integer.toString(timeStampDate);
                }
                else{
                    displayString = Integer.toString(timeStampDate);
                }
                displayString += " / ";
                int timeStampMonth = dataPort.getCloudData().getMonRec();
                if(timeStampMonth < 10){
                    displayString += "0" + Integer.toString(timeStampMonth);
                }
                else{
                    displayString += Integer.toString(timeStampMonth);
                }


                binding.timeStampValue1.setText(displayString);

            }
        });

        Button choosingButton = binding.recorderChoose;
        choosingButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DataContainer dataPort =
                                new ViewModelProvider(requireActivity()).get(DataContainer.class);
                        dataPort.request(1, m_chosenReportIndx);
                    }
                }
        );
        //final TextView textView = binding.textGallery;
        //galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

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