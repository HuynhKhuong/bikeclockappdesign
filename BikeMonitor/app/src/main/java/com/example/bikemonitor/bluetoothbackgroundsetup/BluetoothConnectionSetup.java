package com.example.bikemonitor.bluetoothbackgroundsetup;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.bikemonitor.combackground.ComComponent;
import com.example.bikemonitor.statemachine.DeviceConnectionStateManager;
import com.example.bikemonitor.ui.devicelist.DeviceListLiveViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.UUID;
public class BluetoothConnectionSetup {
    private final Handler m_userHandler;
    ///use singleton to centralize bluetooth setup object
    public static BluetoothConnectionSetup getBluetoothConnectionSetup(Handler userHandler) {
        if (object == null) {
            object = new BluetoothConnectionSetup(userHandler);
        }
        return object;
    }

    /// Must be called after Initial Setup Calls
    public static BluetoothConnectionSetup getBluetoothConnectionSetup(){
        return object;
    }
    private BluetoothConnectionSetup(Handler userHandler) {
        m_State = STATE_NONE;
        m_BtAdapter = BluetoothAdapter.getDefaultAdapter();
        m_userHandler = userHandler;
    }

    /// Request to turn on bluetooth
    public void initDeviceBluetooth(AppCompatActivity currentParentActivity, UserErrorHandler userErrorHandleLogic) {
        final int REQUEST_ENABLE_BT = 1;

        if (m_BtAdapter == null) {
            userErrorHandleLogic.execute();
            return;
        }

        bluetoothConnectPermissionRequest(currentParentActivity);

        if (!m_BtAdapter.isEnabled()) {
            Intent enableBltIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(currentParentActivity, enableBltIntent, REQUEST_ENABLE_BT, null);
        } else {
            //bluetooth is already enabled, no action needed
        }
    }

    public void initDeviceBluetooth(View currentParentView, UserErrorHandler userErrorHandleLogic) {
        final int REQUEST_ENABLE_BT = 1;

        if (m_BtAdapter == null) {
            userErrorHandleLogic.execute();
            return;
        }

        bluetoothConnectPermissionRequest(currentParentView);

        if (!m_BtAdapter.isEnabled()) {
            Intent enableBltIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult((Activity) currentParentView.getContext(), enableBltIntent, REQUEST_ENABLE_BT, null);
        } else {
            //bluetooth is already enabled, no action needed
        }
    }


    /// Section for Making Device Discoverable from other devices
    /// @note: Only check this if you setup your phone as client
    public void enableBluetoothDeviceDiscoverable(View currentParentView, UserErrorHandler userErrorHandleLogic) {
        final int REQUEST_ENABLE_BT = 1;
        if (m_BtAdapter == null) {
            userErrorHandleLogic.execute();
            return;
        }

        bluetoothConnectPermissionRequest(currentParentView);

        Intent enableBltIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        enableBltIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivityForResult((Activity) currentParentView.getContext(), enableBltIntent, REQUEST_ENABLE_BT, null);
    }

    public void enableBluetoothDeviceDiscoverable(AppCompatActivity currentParentActivity, UserErrorHandler userErrorHandleLogic) {
        final int REQUEST_ENABLE_BT = 1;
        if (m_BtAdapter == null) {
            userErrorHandleLogic.execute();
            return;
        }

        bluetoothConnectPermissionRequest(currentParentActivity);

        Intent enableBltIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        enableBltIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivityForResult(currentParentActivity, enableBltIntent, REQUEST_ENABLE_BT, null);
    }

    @Nullable
    @SuppressLint("MissingPermission")
    public Set<BluetoothDevice> getPairedDevices(View currentParentView, UserErrorHandler userErrorHandleLogic) {
        if (m_BtAdapter == null) {
            userErrorHandleLogic.execute();
            return null;
        }

        bluetoothDiscoveryPermissionRequest(currentParentView);
        return m_BtAdapter.getBondedDevices();
    }

    @Nullable
    @SuppressLint("MissingPermission")
    public Set<BluetoothDevice> getPairedDevices(AppCompatActivity currentParentActivity, UserErrorHandler userErrorHandleLogic) {
        if (m_BtAdapter == null) {
            userErrorHandleLogic.execute();
            return null;
        }

        bluetoothDiscoveryPermissionRequest(currentParentActivity);
        return m_BtAdapter.getBondedDevices();
    }

    @SuppressLint("MissingPermission")
    public void doDiscovery(View currentParentView, UserErrorHandler userErrorHandleLogic) {
        if (m_BtAdapter == null) {
            userErrorHandleLogic.execute();
            return;
        }

        bluetoothDiscoveryPermissionRequest(currentParentView);

        // If we're already discovering, stop it
        if (m_BtAdapter.isDiscovering()) {
            m_BtAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        m_BtAdapter.startDiscovery();
    }

    @SuppressLint("MissingPermission")
    public void doDiscovery(AppCompatActivity currentParentActivity, UserErrorHandler userErrorHandleLogic) {
        if (m_BtAdapter == null) {
            userErrorHandleLogic.execute();
            return;
        }

        bluetoothDiscoveryPermissionRequest(currentParentActivity);

        // If we're already discovering, stop it
        if (m_BtAdapter.isDiscovering()) {
            m_BtAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        m_BtAdapter.startDiscovery();
    }

    @SuppressLint("MissingPermission")
    public void disableDiscovery(View currentParentView, UserErrorHandler userErrorHandleLogic) {
        if (m_BtAdapter == null) {
            userErrorHandleLogic.execute();
            return;
        }

        bluetoothDiscoveryPermissionRequest(currentParentView);

        // If we've already turned off discovering. do nothing
        if (m_BtAdapter.isDiscovering()) {
            m_BtAdapter.cancelDiscovery();
        }

    }

    @SuppressLint("MissingPermission")
    public void disableDiscovery(AppCompatActivity currentParentActivity, UserErrorHandler userErrorHandleLogic) {
        if (m_BtAdapter == null) {
            userErrorHandleLogic.execute();
            return;
        }

        bluetoothDiscoveryPermissionRequest(currentParentActivity);

        // If we've alreay turned off discovering. do nothing
        if (m_BtAdapter.isDiscovering()) {
            m_BtAdapter.cancelDiscovery();
        }
    }

    /// Section for Accepting connections from Bluetooth Devices as Server
    /// 1. get Bluetooth Server Socket
    /// 2. Start listening for connection requests by calling accept
    /// 3. Unless you want to accept additional connections, call close()
    @SuppressLint("MissingPermission")
    public class AcceptThread extends Thread {
        private BluetoothServerSocket m_serverSocket;
        private BluetoothAdapter hardwareInterface = BluetoothAdapter.getDefaultAdapter();
        private int mState;

        final int getCurrentState() {
            return mState;
        }

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = hardwareInterface.listenUsingRfcommWithServiceRecord("NAME", m_UUID);
            } catch (IOException e) {
            }

            m_serverSocket = tmp;
            mState = STATE_LISTEN;
        }


        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            // accept is a blocking call -> It returns either by exception occurs or a socket is returned
            // When connect successfully -> accept() returns a connected BluetoothSocket
            while (mState != STATE_CONNECTED) {
                try {
                    socket = m_serverSocket.accept();
                } catch (IOException e) {
                    break;
                }

                // If a connection was accepted
                if (socket != null) {
                    // Do work to manage the connection (in a separate thread)
                    switch (mState) {
                        case STATE_LISTEN:
                        case STATE_CONNECTING:
                            // Situation normal. Start the connected thread.
                            //connected(socket, socket.getRemoteDevice());
                            break;
                        case STATE_NONE:
                        case STATE_CONNECTED:
                            // Either not ready or already connected. Terminate new socket.
                            try {
                                socket.close();
                            } catch (IOException e) {
                            }
                            break;
                    }
                }
            }
        }

        public void cancel() {
            try {
                m_serverSocket.close();
            } catch (IOException e) {

            }
        }
    }

    ///After the connection attempts are accept
    ///Establish Input/Output stream from the device
    public class ConnectedThread extends Thread {

        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private final View m_currentParentView;
        private final AppCompatActivity m_currentParrentActivity;

        public ConnectedThread(@NonNull BluetoothSocket socket, View currentParentView) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            m_currentParentView = currentParentView;
            m_currentParrentActivity = null;
            DeviceConnectionStateManager.getDeviceConnectionStateManager().updateState(
                    DeviceConnectionStateManager.DEVICE_LISTENING
            );
            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
                Message msg = m_userHandler.obtainMessage(ERROR);
                Bundle bundle = new Bundle();
                bundle.putString(ERROR_TYPE, "stream null!!!!");
                msg.setData(bundle);
                m_userHandler.sendMessage(msg);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            m_State = STATE_CONNECTED;
        }

        public ConnectedThread(@NonNull BluetoothSocket socket, AppCompatActivity currentParentActivity) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            m_currentParrentActivity = currentParentActivity;
            m_currentParentView = null;
            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                    tmpIn = socket.getInputStream();
                    tmpOut = socket.getOutputStream();


            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
                Message msg = m_userHandler.obtainMessage(ERROR);
                Bundle bundle = new Bundle();
                bundle.putString(ERROR_TYPE, "stream null!!!!");
                msg.setData(bundle);
                m_userHandler.sendMessage(msg);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            m_State = STATE_CONNECTED;
        }

        @SuppressLint("MissingPermission")
        public void run() {
            if(m_currentParrentActivity != null){
                bluetoothDiscoveryPermissionRequest(m_currentParrentActivity);
            }
            else if(m_currentParentView != null){
                bluetoothDiscoveryPermissionRequest(m_currentParentView);
            }
            if(m_BtAdapter.isDiscovering()){
                m_BtAdapter.cancelDiscovery(); //Always cancel discovery because it will slow down a connection
            }

            int bytes = 0;

            // Keep listening to the InputStream until an exception occurs
            while (m_State == STATE_CONNECTED) {
                try {
                    byte[] buffer = new byte[1024];//[availablebytes];

                    bytes = mmInStream.read(buffer);
                    byte[] readbuffer = new byte[bytes];
                    int index = 0;
                    while(index < bytes){
                        readbuffer[index] = buffer[index];
                        index++;
                    }
                    Log.d("mmInStream.rad(buffer);", new String(readbuffer));
                    Log.d("mmInStream.rad(buffer) returns ", Integer.toString(bytes));
                    m_userHandler.obtainMessage(DEVICE_READ, bytes, -1, readbuffer).sendToTarget();

                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    Message msg = m_userHandler.obtainMessage(ERROR);
                    Bundle bundle = new Bundle();
                    bundle.putString(ERROR_TYPE, "Device Disconnected");
                    msg.setData(bundle);
                    m_userHandler.sendMessage(msg);
                    connectionLost();
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
                // Share the sent message back to the UI Activity
                m_userHandler.obtainMessage(DEVICE_WRITE, -1, -1, bytes)
                        .sendToTarget();

                Log.i(TAG, new String(bytes));
            } catch (IOException e) {
                Log.e(TAG, "write exception!!!");
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                Message msg;
                msg = m_userHandler.obtainMessage(ERROR);
                Bundle bundle = new Bundle();
                bundle.putString(ERROR_TYPE, "closing socket");
                msg.setData(bundle);
                m_userHandler.sendMessage(msg);
                mmSocket.close();
                DeviceConnectionStateManager.getDeviceConnectionStateManager().updateState(
                        DeviceConnectionStateManager.DEVICE_DISCONNECTED
                );
            } catch (IOException e) {
            }
        }
    }

    ///For client side

    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;
        private BluetoothDevice mmDevice;
        private final View m_currentParentView;
        private final AppCompatActivity m_currentParentActivity;

        @SuppressLint("MissingPermission")
        public ConnectThread(@NonNull BluetoothDevice device, @NonNull View currentParentView) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;
            m_currentParentView = currentParentView;
            m_currentParentActivity = null;
            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                bluetoothConnectPermissionRequest(m_currentParentView);
                tmp = mmDevice.createRfcommSocketToServiceRecord(m_UUID);
            } catch (IOException e) {
            }
            mmSocket = tmp;
            m_State = STATE_CONNECTING;
        }
        @SuppressLint("MissingPermission")
        public ConnectThread(@NonNull BluetoothDevice device, @NonNull AppCompatActivity currentParentActivity) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;
            m_currentParentActivity = currentParentActivity;
            m_currentParentView = null;
            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                bluetoothConnectPermissionRequest(m_currentParentActivity);
                tmp = mmDevice.createRfcommSocketToServiceRecord(m_UUID);
            } catch (IOException e) {

            }
            mmSocket = tmp;
            m_State = STATE_CONNECTING;
        }

        @SuppressLint("MissingPermission")
        public void run() {
            if(m_currentParentView != null){
                bluetoothDiscoveryPermissionRequest(m_currentParentView);
            }
            else if (m_currentParentActivity != null){
                bluetoothDiscoveryPermissionRequest(m_currentParentActivity);
            }
            // Cancel discovery because it will slow down the connection
            if(m_BtAdapter.isDiscovering()){
                m_BtAdapter.cancelDiscovery();
            }
            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a successful connection or an exception
                Log.i(TAG,"Connecting to socket...");
                mmSocket.connect();
                Log.i(TAG,"Connected");
            } catch (IOException e) {
                Log.e(TAG, e.toString());
                Message msg = m_userHandler.obtainMessage(ERROR);
                Bundle bundle = new Bundle();
                bundle.putString(ERROR_TYPE, "Can't Connect to Device");
                msg.setData(bundle);
                m_userHandler.sendMessage(msg);

                // Some 4.1 devices have problems, try an alternative way to connect
                // See https://github.com/don/BluetoothSerial/issues/89
                try {
                    Log.i(TAG,"Trying fallback...");
                    mmSocket = (BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(mmDevice,1);
                    mmSocket.connect();
                    Log.i(TAG,"Connected");
                } catch (Exception e2) {
                    Log.e(TAG, "Couldn't establish a Bluetooth connection.");
                    try {
                        mmSocket.close();
                    } catch (IOException e3) {
                        Log.e(TAG, "unable to close() socket during connection failure", e3);
                    }
                    connectionFailed();
                    return;
                }
            }

            // Start the connected thread
            if(m_currentParentView != null){
                connected(mmSocket, mmDevice, m_currentParentView);
            }
            else if (m_currentParentActivity != null){
                connected(mmSocket, mmDevice, m_currentParentActivity);
            }
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                Message msg;
                msg = m_userHandler.obtainMessage(ERROR);
                Bundle bundle = new Bundle();
                bundle.putString(ERROR_TYPE, "closing socket");
                msg.setData(bundle);
                m_userHandler.sendMessage(msg);
                mmSocket.close();
                DeviceConnectionStateManager.getDeviceConnectionStateManager().updateState(
                        DeviceConnectionStateManager.DEVICE_DISCONNECTED
                );
            } catch (IOException e) { }
        }
    }
    //Section to handle standard bluetooth connection state machine
    @SuppressLint("MissingPermission")
    private void connected(BluetoothSocket socket, BluetoothDevice  device, View currentParentView) {
        // Cancel the thread that completed the connection
        //      if (m_connectThread != null) {
        //         m_connectThread.cancel();
        //         m_connectThread = null;
        //      }

        // Cancel any thread currently running a connection
        if (m_connectedThread != null) {
            m_connectedThread.cancel();
            m_connectedThread = null;
        }

        // Cancel the accept thread because we only want to connect to one device
        if (m_acceptThread != null) {
            m_acceptThread.cancel();
            m_acceptThread = null;
        }

        Message msg = m_userHandler.obtainMessage(MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(DEVICE_NAME, device.getName());
        msg.setData(bundle);
        m_userHandler.sendMessage(msg);

        if(socket == null){
            msg = m_userHandler.obtainMessage(ERROR);
            bundle = new Bundle();
            bundle.putString(ERROR_TYPE, "Socket is null!!!!");
            msg.setData(bundle);
            m_userHandler.sendMessage(msg);
        }
        else {
            // Start the thread to manage the connection and perform transmissions
            m_connectedThread = new ConnectedThread(socket, currentParentView);
            m_connectedThread.start();
        }

    }

    @SuppressLint("MissingPermission")
    private void connected(BluetoothSocket socket, BluetoothDevice  device, AppCompatActivity currentParentActivity) {
        // Cancel the thread that completed the connection
        if (m_connectThread != null) {
            m_connectThread.cancel();
            m_connectThread = null;
        }

        // Cancel any thread currently running a connection
        if (m_connectedThread != null) {
            m_connectedThread.cancel();
            m_connectedThread = null;
        }

        // Cancel the accept thread because we only want to connect to one device
        if (m_acceptThread != null) {
            m_acceptThread.cancel();
            m_acceptThread = null;
        }

        Message msg = m_userHandler.obtainMessage(MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(DEVICE_NAME, device.getName());
        msg.setData(bundle);
        m_userHandler.sendMessage(msg);

        // Start the thread to manage the connection and perform transmissions
        m_connectedThread = new ConnectedThread(socket, currentParentActivity);
        m_connectedThread.start();
    }

    private void connectionLost(){
        // Cancel any thread attempting to make a connection
        if (m_connectThread != null) {m_connectThread.cancel(); m_connectThread = null;}

        // Cancel any thread currently running a connection
        if (m_connectedThread != null) {m_connectedThread.cancel(); m_connectedThread = null;}
        m_State = STATE_NONE;

        DeviceConnectionStateManager.getDeviceConnectionStateManager().updateState(
                DeviceConnectionStateManager.DEVICE_DISCONNECTED
        );
        //start();
    }

    private void connectionFailed(){
        // Cancel any thread attempting to make a connection
        if (m_connectThread != null) {m_connectThread.cancel(); m_connectThread = null;}

        // Cancel any thread currently running a connection
        if (m_connectedThread != null) {m_connectedThread.cancel(); m_connectedThread = null;}
        m_State = STATE_NONE;
        DeviceConnectionStateManager.getDeviceConnectionStateManager().updateState(
                DeviceConnectionStateManager.DEVICE_DISCONNECTED
        );
        //start();
    }

    /// Client Initialize
    public void connect(BluetoothDevice device, View currentParentView){
        // Cancel any thread attempting to make a connection
        if (m_State == STATE_CONNECTING) {
            if (m_connectThread != null) {
                m_connectThread.cancel();
                m_connectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (m_connectedThread != null) {
            m_connectedThread.cancel();
            m_connectedThread = null;
        }

        // Start the thread to connect with the given device
        m_connectThread = new ConnectThread(device, currentParentView);
        m_connectThread.start();
    }

    public void connect(BluetoothDevice device, AppCompatActivity currentParentActivity){
        // Cancel any thread attempting to make a connection
        if (m_State == STATE_CONNECTING) {
            if (m_connectThread != null) {
                m_connectThread.cancel();
                m_connectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (m_connectedThread != null) {
            m_connectedThread.cancel();
            m_connectedThread = null;
        }

        // Start the thread to connect with the given device
        m_connectThread = new ConnectThread(device, currentParentActivity);
        m_connectThread.start();
    }

    ///Server Initialize
    public void start(){
        // Cancel any thread attempting to make a connection
        if (m_connectThread != null) {
            m_connectThread.cancel();
            m_connectThread = null;
        }

        // Cancel any thread currently running a connection
        if (m_connectedThread != null) {
            m_connectedThread.cancel();
            m_connectedThread = null;
        }

        // Start the thread to listen on a BluetoothServerSocket
        if (m_acceptThread == null) {
            m_acceptThread = new AcceptThread();
            m_acceptThread.start();
        }
    }

    /// Additional Services
    public BluetoothDevice getRemoteDevice(String address, View currentParentView){
        bluetoothConnectPermissionRequest(currentParentView);
        return m_BtAdapter.getRemoteDevice(address);
    }

    public BluetoothDevice getRemoteDevice(String address, AppCompatActivity currentParentActivity){
        bluetoothConnectPermissionRequest(currentParentActivity);
        return m_BtAdapter.getRemoteDevice(address);
    }
    private void bluetoothConnectPermissionRequest(AppCompatActivity currentParentActivity){
        if (ActivityCompat.checkSelfPermission(currentParentActivity, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(currentParentActivity, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, 1);
        }
    }

    private void bluetoothConnectPermissionRequest(@NonNull View currentParentView){
        if (ActivityCompat.checkSelfPermission(currentParentView.getContext(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity)currentParentView.getContext(), new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, 1);
        }
    }

    private void bluetoothDiscoveryPermissionRequest(@NonNull View currentView){
        if (ActivityCompat.checkSelfPermission(currentView.getContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity)currentView.getContext(), new String[]{Manifest.permission.BLUETOOTH_SCAN}, 1);
        }
        else if(ActivityCompat.checkSelfPermission(currentView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity)currentView.getContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else if(ActivityCompat.checkSelfPermission(currentView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity)currentView.getContext(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    private void bluetoothDiscoveryPermissionRequest(@NonNull AppCompatActivity currentParentActivity){
        if (ActivityCompat.checkSelfPermission(currentParentActivity, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(currentParentActivity, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 1);
        }
        else if(ActivityCompat.checkSelfPermission(currentParentActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(currentParentActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else if(ActivityCompat.checkSelfPermission(currentParentActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(currentParentActivity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    /**
     * Write to the connected OutStream.
     *
     * @param out The bytes to write
     */
    public void write(String out) {
        // Create temporary object
        ConnectedThread connectedThread = null;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (m_State != STATE_CONNECTED) return;
            connectedThread = m_connectedThread;
        }
        // Perform the write unsynchronized
        try{
            byte[] byteOut = out.getBytes("UTF-8");
            connectedThread.write(byteOut);
        }
        catch(UnsupportedEncodingException e){

            Log.e(TAG, e.toString());
        }

    }

    public int getCurrentState(){
        return m_State;
    }

    private final UUID m_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    /// State Machine of BluetoothConnectionSetup
    private int m_State;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device

    public static final int ERROR = 10;
    public static final String ERROR_TYPE = "error_type";

    public static final int MESSAGE_DEVICE_NAME = 20;
    public static final String DEVICE_NAME = "device_name";

    public static final int DEVICE_READ = 30;
    public static final String BUFFER_NAME = "buffer_name";
    public static final int DEVICE_WRITE = 31;
    private ConnectedThread m_connectedThread = null;
    private ConnectThread m_connectThread = null;
    private AcceptThread m_acceptThread = null;
    private BluetoothAdapter m_BtAdapter;
    // Debugging
    private static final String TAG = "BluetoothConnectionSetup";

    private static BluetoothConnectionSetup object;
}
