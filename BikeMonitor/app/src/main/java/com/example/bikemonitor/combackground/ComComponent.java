package com.example.bikemonitor.combackground;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.bikemonitor.bluetoothbackgroundsetup.BluetoothConnectionSetup;
import com.example.bikemonitor.bluetoothbackgroundsetup.BluetoothPayload;

import org.json.JSONObject;

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
        sendRawPayload(data);
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
        return data;
        //sendRawPayload(data);
    }

    /// Service to support request current data
    public String unlockWithKey(String key){
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
        return data;
        //sendRawPayload(data);
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
        sendRawPayload(data);
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
        m_bluetoothPort = new ViewModelProvider(currentParentActivity).get(BluetoothPayload.class);
        m_receivedRawPayload = new byte[payloadLength];
        m_transmitRawPayload = new byte[payloadLength];
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
        }
        m_receivedDLC = length;
        return true;
    }

    public void processPayload(){

    }

    public void sendRawPayload(String sentData){
        BluetoothConnectionSetup.getBluetoothConnectionSetup().write(sentData);
    }

    public boolean get_m_isCommunictionEstablished(){return m_isCommunictionEstablished;}
    public void set_m_isCommunictionEstablished(boolean input ){m_isCommunictionEstablished = input;}

    private BluetoothPayload m_bluetoothPort;
    private byte[] m_receivedRawPayload;
    private byte[] m_transmitRawPayload;

    private int m_receivedDLC = 0;
    private boolean m_isCommunictionEstablished = false;
    private final static int payloadLength = 1024;
    private static ComComponent object;
}
