package com.example.bikemonitor.ui.devicelist;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.bikemonitor.R;
import com.example.bikemonitor.UserInfor;
import com.example.bikemonitor.bluetoothbackgroundsetup.BluetoothConnectionSetup;
import com.example.bikemonitor.bluetoothbackgroundsetup.DataContainer;
import com.example.bikemonitor.bluetoothbackgroundsetup.UserErrorHandler;
import com.example.bikemonitor.databinding.FragmentDevicelistBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class DeviceListFragment extends Fragment {
    private FragmentDevicelistBinding binding;
    private DeviceListLiveViewModel m_ChosenDeviceNotifier;
    private DataContainer m_cloudDataHandler;
    private boolean isDummyTitleCleared = false;

    /**
     * Existing devices
     */
    /**
     * The on-click listener for all devices in the ListViews
     */
    private class ActivityUserError implements UserErrorHandler {
        @Override
        public void execute(){
            //do nothing
        }
    }
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            TextView data = (TextView)v;
            if(data.getText().equals("No Device Existing")){
                //Do nothing
            }
            else{
                // Cancel discovery because it's costly and we're about to connect
                BluetoothConnectionSetup.getBluetoothConnectionSetup().disableDiscovery(binding.getRoot(),
                        new ActivityUserError());

                // Get the device MAC address, which is the last 17 chars in the View
                String info = ((TextView) v).getText().toString();
                String address = info.substring(info.length() - 17);

                BluetoothDevice device = BluetoothConnectionSetup.getBluetoothConnectionSetup().getRemoteDevice(
                        address, binding.getRoot());

                Log.d("", "Bluetooth connect attempts: " + address + " " + info);
                if(device != null){
                    BluetoothConnectionSetup.getBluetoothConnectionSetup().connect(device, binding.getRoot());
                }
            }
        }
    };

    private ArrayList<String> m_devicesName = new ArrayList<String>(Arrays.asList(new String[]{"No Device Existing"}));
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDevicelistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView mScreenTitle = binding.screentitle;


        m_ChosenDeviceNotifier = new ViewModelProvider(requireActivity()).get(DeviceListLiveViewModel.class);
        m_cloudDataHandler = new ViewModelProvider(requireActivity()).get(DataContainer.class);

        mScreenTitle.setText("Devices Name");

        ///Evaluate data container to get device list
        if(m_cloudDataHandler.getCloudData().getDevRegSts() == false){
            //do nothing
            //keep default value, device list will get displayed as no device existing
        }
        else{
            //query data container
            final int dummyTitleIndex = 0;
            m_devicesName.remove(dummyTitleIndex);
            m_devicesName.add(m_cloudDataHandler.getCloudData().getUserDevice() + "\n" +
                    m_cloudDataHandler.getCloudData().getDevAddr());
        }
        mNewDevicesArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.device_name,m_devicesName);
        ListView existingDevicesList = binding.existingDevices;

        existingDevicesList.setAdapter(mNewDevicesArrayAdapter);
        existingDevicesList.setOnItemClickListener(mDeviceClickListener);

        m_ChosenDeviceNotifier.getDeviceInfo().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                final int dummyTitleIndex = 0;
                if(m_ChosenDeviceNotifier.get_hasUserSearchedNewDevice()){
                    if(!isDummyTitleCleared){
                        m_devicesName.remove(dummyTitleIndex);
                        isDummyTitleCleared = true;
                    }

                    if(!m_devicesName.contains(s)){
                        m_devicesName.clear();
                        m_devicesName.add(s);

                        ///Current workaround, other robust setter will be checked
                        m_cloudDataHandler.request(0,0);
                        m_cloudDataHandler.getCloudData().setUserDevice(m_ChosenDeviceNotifier.getDeviceInfo().getValue());
                        m_cloudDataHandler.getCloudData().setDevAddress(m_ChosenDeviceNotifier.getDeviceMac());
                        m_cloudDataHandler.getCloudData().setDevRegSts(true);
                        m_cloudDataHandler.notifyDataChange();
                    }

                    mNewDevicesArrayAdapter.notifyDataSetChanged();
                    BluetoothDevice device = BluetoothConnectionSetup.getBluetoothConnectionSetup().getRemoteDevice(
                            m_ChosenDeviceNotifier.getDeviceMac(), root);
                    if(device != null){
                        BluetoothConnectionSetup.getBluetoothConnectionSetup().connect(device, root);
                    }
                }
            }
        });


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
        m_ChosenDeviceNotifier.set_hasUserSearchedNewDevice(false);
        binding = null;
    }
}