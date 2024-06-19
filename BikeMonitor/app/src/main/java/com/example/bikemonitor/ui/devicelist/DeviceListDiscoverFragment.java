package com.example.bikemonitor.ui.devicelist;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.bikemonitor.R;
import com.example.bikemonitor.bluetoothbackgroundsetup.BluetoothConnectionSetup;
import com.example.bikemonitor.bluetoothbackgroundsetup.UserErrorHandler;
import com.example.bikemonitor.databinding.FragmentDevicediscoverBinding;

import java.util.Set;

public class DeviceListDiscoverFragment extends Fragment {

    private class ActivityUserError implements UserErrorHandler {
        @Override
        public void execute(){
            binding.textDebug.setVisibility(View.VISIBLE);
            binding.textDebug.setText("m_btAdapter IS NULL");
        }
    }

    /**
     * Newly discovered devices
     */
    private ArrayAdapter<String> mNewDevicesArrayAdapter;

    /**
     * The on-click listener for all devices in the ListViews
     */
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            BluetoothConnectionSetup.getBluetoothConnectionSetup().disableDiscovery(binding.getRoot(),
                    new ActivityUserError());

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            m_deviceChosenNotify.setDeviceInfo(info.substring(0, info.length() - 18));
            m_deviceChosenNotify.setDeviceMac(address);
            m_deviceChosenNotify.set_hasUserSearchedNewDevice(true);
            NavHostFragment hostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(
                                                                        R.id.nav_host_fragment_content_main);
            NavController navController = hostFragment.getNavController();
            navController.popBackStack();
        }
    };

    /**
     * The BroadcastReceiver that listens for discovered devices and changes the title when
     * discovery is finished
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        @SuppressLint("MissingPermission")
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device != null && device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };

    private FragmentDevicediscoverBinding binding;
    private DeviceListLiveViewModel m_deviceChosenNotify;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDevicediscoverBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        binding.textDebug.setVisibility(View.GONE);

        m_deviceChosenNotify = new ViewModelProvider(requireActivity()).get(DeviceListLiveViewModel.class);
        //Start doing discover
        BluetoothConnectionSetup.getBluetoothConnectionSetup().doDiscovery(root,new ActivityUserError());
            
      if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.BLUETOOTH_SCAN}, 1);
      }
      else if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
          ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
      }
      else if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
          ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
      }
//      m_btAdapter = BluetoothAdapter.getDefaultAdapter();

        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        ArrayAdapter<String> pairedDevicesArrayAdapter =
                new ArrayAdapter<>(getContext(), R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.device_name);

        // Find and set up the ListView for paired devices
        ListView pairedListView = binding.pairedDevices;
        pairedListView.setAdapter(pairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // Find and set up the ListView for newly discovered devices
        ListView newDevicesListView = binding.newDevices;
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        requireActivity().registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        requireActivity().registerReceiver(mReceiver, filter);


        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = BluetoothConnectionSetup.getBluetoothConnectionSetup().getPairedDevices(root, new ActivityUserError());

        // If there are paired devices, add each one to the ArrayAdapter
        if(pairedDevices == null){
            //do nothing
        }
        else{
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            } else {
                String noDevices = getResources().getText(R.string.none_paired).toString();
                pairedDevicesArrayAdapter.add(noDevices);
            }
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Make sure we're not doing discovery anymore
        BluetoothConnectionSetup.getBluetoothConnectionSetup().disableDiscovery(binding.getRoot(), new ActivityUserError());
        // Unregister broadcast listeners
        requireActivity().unregisterReceiver(mReceiver);
    }
}