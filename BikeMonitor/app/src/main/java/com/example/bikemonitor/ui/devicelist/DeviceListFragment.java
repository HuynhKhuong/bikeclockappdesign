package com.example.bikemonitor.ui.devicelist;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import com.example.bikemonitor.R;
import com.example.bikemonitor.databinding.FragmentDevicelistBinding;

public class DeviceListFragment extends Fragment {
    private FragmentDevicelistBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDevicelistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        CustomAdapter myCustomAdapter = new CustomAdapter();

//        RecyclerView rvDrawer = binding.rvDrawer;
//        rvDrawer.setAdapter(myCustomAdapter);
//        rvDrawer.setLayoutManager(new LinearLayoutManager(getActivity()));
        Button addButton = binding.addButton;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).navigate(new NavDirections() {
                    @Override
                    public int getActionId() {
                        return R.id.action_nav_devicelist_to_nav_devicelistdiscover;
                    }

                    @NonNull
                    @Override
                    public Bundle getArguments() {
                        return null;
                    }
                });
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}