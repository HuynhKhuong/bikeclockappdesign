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
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.example.bikemonitor.bluetoothbackgroundsetup.BluetoothConnectionSetup;
import com.example.bikemonitor.bluetoothbackgroundsetup.UserErrorHandler;
import com.example.bikemonitor.combackground.ComComponent;
import com.example.bikemonitor.combackground.JsonPackage;
import com.example.bikemonitor.statemachine.DeviceConnectionStateManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
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

public class MainActivity extends AppCompatActivity {
    private class UserHandler extends Handler{
        private AppCompatActivity m_currentParentActivity;
        private byte[] currentReceivedPayload = new byte[1024];
        private int receivedDLC = 0;
        private boolean payloadReceived = true;
        UserHandler(AppCompatActivity currentParentActivity){
            super();
            m_currentParentActivity = currentParentActivity;
        }
        @Override
        public void handleMessage(@NonNull Message msg){
            switch(msg.what){
                case BluetoothConnectionSetup.ERROR:
                    String errorText = "Error detected: ";
                    errorText += msg.getData().getString(BluetoothConnectionSetup.ERROR_TYPE);

                    Snackbar errorSnackbar = Snackbar.make(
                            binding.drawerLayout,
                            errorText,
                            5000);
                    errorSnackbar.show();
                    break;
                case BluetoothConnectionSetup.MESSAGE_DEVICE_NAME:
                    Snackbar deviceNameSnackbar = Snackbar.make(
                            binding.drawerLayout,
                            msg.getData().getString(BluetoothConnectionSetup.DEVICE_NAME),
                            5000);
                    deviceNameSnackbar.show();
                    break;
                case BluetoothConnectionSetup.DEVICE_READ:
                    ///Cache handling to ensure the whole package is received completely before processing
                    final byte endPayloadCharacter = (byte)'}';
                    payloadReceived = false;
                    int index = 0;
                    while(index < (int)msg.arg1){
                        currentReceivedPayload[receivedDLC] = ((byte[])msg.obj)[index];
                        if(((byte[])msg.obj)[index] == endPayloadCharacter){
                            payloadReceived = true;
                            receivedDLC++;
                            break;
                        }

                        index++;
                        receivedDLC++;
                    }
                    if(payloadReceived){
                        ComComponent.getComComponent().getRawPayload(currentReceivedPayload,receivedDLC);
                        ComComponent.getComComponent().processPayload();
                        receivedDLC = 0;
                    }
                    break;
                case BluetoothConnectionSetup.DEVICE_WRITE:
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + msg.what);
            }
        }
    }
    private class ActivityUserError implements UserErrorHandler {
        @Override
        public void execute()
        {
            Snackbar snackbar = Snackbar.make(
                    binding.drawerLayout,
                    "Bluetooth Is Not Enabled!",
                    5000);
            snackbar.show();
        }
    }


    //Timer implementation
    private long startTimeMillis = 0;

    Handler timerCollapseHandler = new Handler();
    Runnable timerRunnable = new Runnable(){
        @Override
        public void run(){
            //background cyclic transmission
            //Check current device connection status
            switch(DeviceConnectionStateManager.getDeviceConnectionStateManager().getCurrentState()){
                case DeviceConnectionStateManager.DEVICE_DISCONNECTED:
                    //no transmission
                    //clean up data connection
                    ComComponent.getComComponent().cleanServiceQueue();
                    break;

                case DeviceConnectionStateManager.DEVICE_ACCEPTED:
                    //cyclic transmission
                    //default payload is read current data
                    ComComponent.getComComponent().readCurrentData("11111111");
                    ComComponent.getComComponent().sendRawPayload();
                    break;
                case DeviceConnectionStateManager.DEVICE_LISTENING:
                    //cyclic initiate transmission attempts
                    ComComponent.getComComponent().cleanServiceQueue();
                    ComComponent.getComComponent().initiateConnectWithoutKey();
                    ComComponent.getComComponent().sendRawPayload();
                    break;
                case DeviceConnectionStateManager.DEVICE_FORCEUNLOCK:
                    Log.d("" , "Current Device Connection State " + Integer.toString(DeviceConnectionStateManager.getDeviceConnectionStateManager().getCurrentState()));
                    ComComponent.getComComponent().cleanServiceQueue();
                    ComComponent.getComComponent().unlockWithKey("11111111");
                    ComComponent.getComComponent().sendRawPayload();
                default:
                    break;
            }
            //Reschedule timer 500ms
            timerCollapseHandler.postDelayed(this, 500);
        }
    };

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new UserHandler(this);

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ///Background components Initialize
        ///1.Initialize ComComponent Object
        ComComponent obj = ComComponent.getComComponent(this);
        ///2.Initialize Background timer
        timerCollapseHandler.postDelayed(timerRunnable, 0);
        ///3.Bluetooth startup sequence
        BluetoothConnectionSetup.getBluetoothConnectionSetup(mHandler).initDeviceBluetooth(this, new ActivityUserError());
        ///4.Device state Initialize mapping with connection status
        DeviceConnectionStateManager deviceState = DeviceConnectionStateManager.getDeviceConnectionStateManager();

        ///UI Related Initialize
        setSupportActionBar(binding.toolbar);
        ActionBar mainBar = getSupportActionBar();

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
                    binding.toolbar.setVisibility(View.GONE);
                }
                else {
                    bottomNav.setVisibility(View.VISIBLE);
                    binding.toolbar.setVisibility(View.VISIBLE);
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
