package com.example.bikemonitor;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.example.bikemonitor.databinding.ActivityMainBinding;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

//    public class BluetoothBroadcastReceiver extends BroadcastReceiver{
//        private Set<BluetoothDevice> m_systemDeviceList;
//        public BluetoothBroadcastReceiver(Set<BluetoothDevice> systemDeviceList){
//            super();
//            m_systemDeviceList = systemDeviceList;
//        }
//
//        public Set<BluetoothDevice> getSystemDeviceList(){
//            return m_systemDeviceList;
//        }
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if(BluetoothDevice.ACTION_FOUND.equals(action)){
//                //Discovery has found a device. Get the bluetooth device object
//                //and its info from the intent
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                m_systemDeviceList.add(device);
//            }
//        }
//    }

    private ActivityMainBinding binding;
    private BluetoothAdapter m_BtAdapter;

    //    private BluetoothBroadcastReceiver m_bluetoothDiscoveryHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        ActionBar mainBar = getSupportActionBar();

//For debug purpose
        TextView debugText = binding.textDebug;
        debugText.setVisibility(View.GONE);

        ///Bluetooth startup sequence
        //BluetoothConnectionSetup.initDeviceBluetooth(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, 1);
        }
        m_BtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!m_BtAdapter.isEnabled()) {
            final int REQUEST_ENABLE_BT = 1;
            if (m_BtAdapter == null) {
                debugText.setVisibility(View.VISIBLE);
                debugText.setText("ERROR BLUETOOTH ADAPTER RETURNS NULL");
            } else if (!m_BtAdapter.isEnabled()) {
                Intent enableBltIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBltIntent, REQUEST_ENABLE_BT);
            }
        }

        //m_BtAdapter.cancelDiscovery();

//        Set<BluetoothDevice> blankSet = new HashSet<BluetoothDevice>();
//        m_bluetoothDiscoveryHandler = new BluetoothBroadcastReceiver(blankSet);
//
//        IntentFilter bluetoothFilterIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        registerReceiver(m_bluetoothDiscoveryHandler, bluetoothFilterIntent);

        NavHostFragment hostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(
                                        R.id.nav_host_fragment_content_main);

        NavController navController = hostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNav , navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if((navDestination.getId() == R.id.nav_login) || (navDestination.getId() == R.id.nav_register)||
                        (navDestination.getId() == R.id.nav_devicelist) || (navDestination.getId() == R.id.nav_devicelistdiscover)){
                    bottomNav.setVisibility(View.GONE);
                    binding.appBarMain.toolbar.setVisibility(View.GONE);
                }
                else {
                    bottomNav.setVisibility(View.VISIBLE);
                    binding.appBarMain.toolbar.setVisibility(View.VISIBLE);
                }
            }
        });
}

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         // Inflate the menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(R.menu.main, menu);
         return true;
     }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout_button){
            NavHostFragment hostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(
                                                                    R.id.nav_host_fragment_content_main);
            NavController navController = hostFragment.getNavController();
            NavDestination currentDestination = navController.getCurrentDestination();
            if(currentDestination.getId() == R.id.nav_gallery) {
                navController.navigate(new NavDirections() {
                    @Override
                    public int getActionId() {
                        return R.id.action_nav_gallery_to_nav_login;
                    }

                    @NonNull
                    @Override
                    public Bundle getArguments() {
                        return null;
                    }
                });
            }
            else if(currentDestination.getId() == R.id.nav_home){
                navController.navigate(new NavDirections() {
                    @Override
                    public int getActionId() {
                        return R.id.action_nav_home_to_nav_login;
                    }

                    @NonNull
                    @Override
                    public Bundle getArguments() {
                        return null;
                    }
                });
            }
            else if(currentDestination.getId() == R.id.nav_slideshow){

                navController.navigate(new NavDirections() {
                    @Override
                    public int getActionId() {
                        return R.id.action_nav_slideshow_to_nav_login;
                    }

                    @NonNull
                    @Override
                    public Bundle getArguments() {
                        return null;
                    }
                });
            }
        }
        else if(item.getItemId() == R.id.device_list){
            NavHostFragment hostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(
                    R.id.nav_host_fragment_content_main);
            NavController navController = hostFragment.getNavController();
            NavDestination currentDestination = navController.getCurrentDestination();
            if(currentDestination.getId() == R.id.nav_gallery) {
                navController.navigate(new NavDirections() {
                    @Override
                    public int getActionId() {
                        return R.id.action_nav_gallery_to_nav_devicelist;
                    }

                    @NonNull
                    @Override
                    public Bundle getArguments() {
                        return null;
                    }
                });
            }
            else if(currentDestination.getId() == R.id.nav_home){
                navController.navigate(new NavDirections() {
                    @Override
                    public int getActionId() {
                        return R.id.action_nav_home_to_nav_devicelist;
                    }

                    @NonNull
                    @Override
                    public Bundle getArguments() {
                        return null;
                    }
                });
            }
            else if(currentDestination.getId() == R.id.nav_slideshow){

                navController.navigate(new NavDirections() {
                    @Override
                    public int getActionId() {
                        return R.id.action_nav_slideshow_to_nav_devicelist;
                    }

                    @NonNull
                    @Override
                    public Bundle getArguments() {
                        return null;
                    }
                });
            }
        }
//        else if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        //actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(m_bluetoothDiscoveryHandler);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE.equals(data.getAction())) {
//            if (resultCode == RESULT_CANCELED) {
//                //Retry Discover again
//            }
//        }
//        if(BluetoothAdapter.ACTION_REQUEST_ENABLE.equals(data.getAction())) {
//            if (resultCode == RESULT_CANCELED) {
//                binding.textDebug.setVisibility(View.VISIBLE);
//                binding.textDebug.setText("ERROR BLUETOOTH ADAPTER CAN'T OPEN");
//            } else if (resultCode == RESULT_OK) {
//
//                binding.textDebug.setVisibility(View.VISIBLE);
//                binding.textDebug.setText("ERROR BLUETOOTH ADAPTER OPENS");
//            }
//        }
    }
}
