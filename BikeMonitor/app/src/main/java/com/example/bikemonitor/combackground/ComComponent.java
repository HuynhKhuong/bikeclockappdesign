package com.example.bikemonitor.combackground;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.bikemonitor.R;
import com.example.bikemonitor.UserInfor;
import com.example.bikemonitor.bluetoothbackgroundsetup.BluetoothConnectionSetup;
import com.example.bikemonitor.bluetoothbackgroundsetup.DataContainer;
import com.example.bikemonitor.statemachine.DeviceConnectionStateManager;
import com.example.bikemonitor.ui.home.HomeViewModel;
import com.example.bikemonitor.ui.slideshow.SlideshowFragment;
import com.example.bikemonitor.ui.slideshow.SlideshowViewModel;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class ComComponent {
    // Debugging
    private static final String TAG = "BluetoothConnectionSetup";
    /// Service to support request key
    // {"cmd":"connect","skey":"00000000"}
    public String initiateConnectWithoutKey(){
        String data = "";
        PackageInterface firstJsonPackage = new JsonPackage(AttributesIndex.cmd, AttributesIndex.cmd_commands.connect);
        PackageInterface secondJsonPackage = new JsonPackage(AttributesIndex.skey, "00000000");
        try {
            data = new JSONObject().put(firstJsonPackage.get_attributes().getStringDatas(), firstJsonPackage.get_datas().getStringDatas()).
                    put(secondJsonPackage.get_attributes().getStringDatas(), secondJsonPackage.get_datas().getStringDatas()).toString();
        }
        catch(org.json.JSONException e){
            Log.e(TAG, e.toString());
        }

        if(data != null){
            requestSendService(data);
        }
        return data;
    }

    /// Service to support request connect
    public String initiateConnectWithKey(String key){
        String data = "";
        PackageInterface firstJsonPackage = new JsonPackage(AttributesIndex.cmd, AttributesIndex.cmd_commands.connect);
        PackageInterface secondJsonPackage = new JsonPackage(AttributesIndex.skey, key);
        try {
            data = new JSONObject().put(firstJsonPackage.get_attributes().getStringDatas(), firstJsonPackage.get_datas().getStringDatas()).
                    put(secondJsonPackage.get_attributes().getStringDatas(), secondJsonPackage.get_datas().getStringDatas()).toString();
        }
        catch(org.json.JSONException e){
            Log.e(TAG, e.toString());
        }
        if(data != null){
            requestSendService(data);
        }
        return data;
    }

    /// Service to support request current data
    public String unlockWithKey(String key){
        String data = "";
        PackageInterface firstJsonPackage = new JsonPackage(AttributesIndex.cmd, AttributesIndex.cmd_commands.ulk);
        PackageInterface secondJsonPackage = new JsonPackage(AttributesIndex.skey, key);
        try {
            data = new JSONObject().put(firstJsonPackage.get_attributes().getStringDatas(), firstJsonPackage.get_datas().getStringDatas()).
                    put(secondJsonPackage.get_attributes().getStringDatas(), secondJsonPackage.get_datas().getStringDatas()).toString();
        }
        catch(org.json.JSONException e){
            Log.e(TAG, e.toString());
        }
        if(data != null){
            requestSendService(data);
        }
        return data;
    }

    /// Service to support request write wheel sise
    public String readConfig(String key){
        String data = "";
        PackageInterface firstJsonPackage = new JsonPackage(AttributesIndex.cmd, AttributesIndex.cmd_commands.rconf);
        PackageInterface secondJsonPackage = new JsonPackage(AttributesIndex.skey, key);
        try {
            data = new JSONObject().put(firstJsonPackage.get_attributes().getStringDatas(), firstJsonPackage.get_datas().getStringDatas()).
                    put(secondJsonPackage.get_attributes().getStringDatas(), secondJsonPackage.get_datas().getStringDatas()).toString();
        }
        catch(org.json.JSONException e){
            Log.e(TAG, e.toString());
        }
        if(data != null){
            requestSendService(data);
        }
        return data;
    }

    private int m_targetSettingsUpdate = 0;

    public void setTargetSettingsUpdate(int value){m_targetSettingsUpdate = value;}
    public int getTargetSettingsUpdate(){return m_targetSettingsUpdate;}


    public String writeConfig(String key){
        String data = "";
        PackageInterface firstJsonPackage = new JsonPackage(AttributesIndex.cmd, AttributesIndex.cmd_commands.wconf);
        PackageInterface secondJsonPackage = new JsonPackage(AttributesIndex.skey, key);
        PackageInterface thirdJsonPackage = new JsonPackage(AttributesIndex.wd, "");
        try {
            data = new JSONObject().put(firstJsonPackage.get_attributes().getStringDatas(), firstJsonPackage.get_datas().getStringDatas()).
                    put(secondJsonPackage.get_attributes().getStringDatas(), secondJsonPackage.get_datas().getStringDatas()).
                    put(thirdJsonPackage.get_attributes().getStringDatas(), m_targetSettingsUpdate).
                    toString();
        }
        catch(org.json.JSONException e){
            Log.e(TAG, e.toString());
        }
        if(data != null){
            requestSendService(data);
        }
        return data;
    }

    public String writeConfig(String key, String wheelSize){
        String data = "";
        PackageInterface firstJsonPackage = new JsonPackage(AttributesIndex.cmd, AttributesIndex.cmd_commands.wconf);
        PackageInterface secondJsonPackage = new JsonPackage(AttributesIndex.skey, key);
        PackageInterface thirdJsonPackage = new JsonPackage(AttributesIndex.wd, wheelSize);
        try {
            data = new JSONObject().put(firstJsonPackage.get_attributes().getStringDatas(), firstJsonPackage.get_datas().getStringDatas()).
                    put(secondJsonPackage.get_attributes().getStringDatas(), secondJsonPackage.get_datas().getStringDatas()).
                    put(thirdJsonPackage.get_attributes().getStringDatas(), thirdJsonPackage.get_datas().getStringDatas()).
                    toString();
        }
        catch(org.json.JSONException e){
            Log.e(TAG, e.toString());
        }
        if(data != null){
            requestSendService(data);
        }
        return data;
    }

    public String readCurrentData(String key){
        String data = "";
        PackageInterface firstJsonPackage = new JsonPackage(AttributesIndex.cmd, AttributesIndex.cmd_commands.inf);
        PackageInterface secondJsonPackage = new JsonPackage(AttributesIndex.skey, key);
        try {
            data = new JSONObject().put(firstJsonPackage.get_attributes().getStringDatas(), firstJsonPackage.get_datas().getStringDatas()).
                    put(secondJsonPackage.get_attributes().getStringDatas(), secondJsonPackage.get_datas().getStringDatas()).
                    toString();
        }
        catch(org.json.JSONException e){
            Log.e(TAG, e.toString());
        }

        if(data != null){
            requestSendService(data);
        }
        return data;
    }


    public static ComComponent getComComponent(AppCompatActivity currentParentActivity){
        if(object == null){
            object = new ComComponent(currentParentActivity);
        }
        return object;
    }

    public static ComComponent getComComponent(){
        return object;
    }

    private ComComponent(AppCompatActivity currentParentActivity){
        m_comPort = new ViewModelProvider(currentParentActivity).get(DataContainer.class);
        m_displayPort = new ViewModelProvider(currentParentActivity).get(HomeViewModel.class);
        m_currentParentActivity = currentParentActivity;
        m_receivedRawPayload = new byte[payloadLength];
    }

    public boolean getRawPayload(@NonNull byte[] payload, int length){
        int index = 0;
        if(length > m_receivedRawPayload.length){
            //DLC not satisfied
            return false;
        }
        while(index < m_receivedRawPayload.length){
            if(index < length){
                m_receivedRawPayload[index] = payload[index];
            }
            else{
                m_receivedRawPayload[index] = 0; //unused field are reset to 0;
            }
            index++;
        }
        m_receivedDLC = length;
        Log.d(TAG, "Received DLC" + Integer.toString(m_receivedDLC));
        return true;
    }

    public void processPayload(){
        Log.d(TAG, "Processing payload");
        byte[] completePayload = new byte[m_receivedDLC];
        boolean errorOccurred = false;
        int index = 0;
        JSONObject root = null;
        while(index < m_receivedDLC){
            completePayload[index] = m_receivedRawPayload[index];
            index++;
        }
        try{
            root = new JSONObject(new String(completePayload));
        }
        catch(JSONException e){
            Log.d(TAG, e.toString());
            errorOccurred = true;
        }

        if(errorOccurred == false){
            try{

                ///User data handle right here
                JsonPackage expectedCmd = new JsonPackage(AttributesIndex.cmd, "");
                JsonPackage expectedRespond = new JsonPackage(AttributesIndex.res, "");


                expectedCmd.set_datas(new AttributesIndex.ESP32StringData(
                        root.getString(expectedCmd.get_attributes().getStringDatas())));


                if( (DeviceConnectionStateManager.getDeviceConnectionStateManager().getCurrentState() ==
                        DeviceConnectionStateManager.DEVICE_ACCEPTED_UPDATESETTINGS) &&
                    (expectedCmd.get_datas().getStringDatas().equals(
                        AttributesIndex.getAttributesIndex().dataProvider(AttributesIndex.cmd_commands.rconf).
                                getStringDatas()))) {

                }
                else{

                        expectedRespond.set_datas(new AttributesIndex.ESP32StringData(
                                root.getString(expectedRespond.get_attributes().getStringDatas())));
                        Log.d(TAG, "res: " + root.getString(expectedRespond.get_attributes().getStringDatas()));
                        if(!expectedRespond.get_datas().getStringDatas().equals("1")){
                            return;
                        }

                }

                if(DeviceConnectionStateManager.getDeviceConnectionStateManager().getCurrentState() ==
                                DeviceConnectionStateManager.DEVICE_LISTENING ||
                    DeviceConnectionStateManager.getDeviceConnectionStateManager().getCurrentState() ==
                                DeviceConnectionStateManager.DEVICE_FORCEUNLOCK){
                    DeviceConnectionStateManager.getDeviceConnectionStateManager().updateState(
                            DeviceConnectionStateManager.DEVICE_ACCEPTED);
                }

                Log.d(TAG, "expectedCmd: " + expectedCmd.get_datas().getStringDatas());

                if(expectedCmd.get_datas().getStringDatas().equals(
                        AttributesIndex.getAttributesIndex().dataProvider(AttributesIndex.cmd_commands.inf).
                                getStringDatas())){
                    try{
                        /// Hardcoded work around due to short timeline
                        /// Later refactor would be planned
                        m_displayPort.setDisplayedOdo(root.getInt("odo"));
                        Log.d(TAG, "Odo: " + root.getString("odo"));
                        m_displayPort.setDisplayedTime(root.getInt("time"));
                        Log.d(TAG, "DisplayedTime: " + root.getString("time"));
                        m_displayPort.setDisplayedTrip(root.getInt("trip"));
                        Log.d(TAG, "DisplayedTrip: " + root.getString("trip"));
                        m_displayPort.setDisplayedAvdSpd(root.getDouble("avg"));
                        Log.d(TAG, "DisplayedAvdSpd: " + root.getString("avg"));
                        m_displayPort.setDisplayedCurrentSpeed(root.getInt("spd"));
                        Log.d(TAG, "DisplayedCurrentSpeed: " + root.getString("spd"));
                        m_displayPort.setLockIndicator(root.getBoolean("islock"));
                    }
                    catch(org.json.JSONException e){
                        Log.e(TAG, e.toString());
                    }
                }
                else if(expectedCmd.get_datas().getStringDatas().equals(
                        AttributesIndex.getAttributesIndex().dataProvider(AttributesIndex.cmd_commands.rconf).
                                getStringDatas())){
                    try {
                        /// Hardcoded work around due to short timeline
                        /// Later refactor would be planned
                        if(m_targetSettingsUpdate == root.getInt("wd")){
                            DeviceConnectionStateManager.getDeviceConnectionStateManager().updateState(
                                    DeviceConnectionStateManager.DEVICE_ACCEPTED);
                            Snackbar notificationBar = Snackbar.make(
                                    m_currentParentActivity.findViewById(R.id.drawer_layout),
                                    "Write Successfully",
                                    5000);
                            notificationBar.show();

                            SlideshowViewModel uiNotifier = new ViewModelProvider(m_currentParentActivity).get(SlideshowViewModel.class);
                            uiNotifier.setbuttonAnimationStart(true);
                        }
                        else
                        {
                            //do nothing
                        }
                    }
                    catch(org.json.JSONException e){
                            Log.e(TAG, e.toString());
                    }
                }
            }
            catch(org.json.JSONException e){
//                Snackbar error = Snackbar.make(
//                        m_currentParentActivity.getCurrentFocus(),
//                        "Data is unpatched with wrong format",
//                        5000);
//                error.show();
                errorOccurred = true;
            }
        }

    }

    public void cleanServiceQueue(){
        if(!writingQueue.isEmpty()){
            writingQueue.clear();
        }
    }

    public void requestSendService(@NonNull String payload){
        /// This method manually loads writing request in a request queue
        /// This queue will be later handled in timer background callback
        writingQueue.add(payload);
    }

    public void sendRawPayload(){
        ///Handle writing queue
        if(writingQueue.isEmpty()){
            return;
        }

        ///Check current connection
        if(BluetoothConnectionSetup.getBluetoothConnectionSetup().getCurrentState() !=
                BluetoothConnectionSetup.STATE_CONNECTED){
            return;
        }
        BluetoothConnectionSetup.getBluetoothConnectionSetup().write(writingQueue.poll());
    }
    private byte[] m_receivedRawPayload;

    private Queue<String> writingQueue = new LinkedList<String>();

    DataContainer m_comPort;
    HomeViewModel m_displayPort;
    AppCompatActivity m_currentParentActivity;
    private int m_receivedDLC = 0;
    private final static int payloadLength = 1024;
    private static ComComponent object;
}
