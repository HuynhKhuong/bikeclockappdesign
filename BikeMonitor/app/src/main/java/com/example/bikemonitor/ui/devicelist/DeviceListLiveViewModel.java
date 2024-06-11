package com.example.bikemonitor.ui.devicelist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DeviceListLiveViewModel extends ViewModel{
    private final MutableLiveData<String> deviceName = new MutableLiveData<String>();
    private String deviceMac = new String("");
    private boolean hasUserSearchedNewDevice = false;

    public void setDeviceInfo(String msg){
        deviceName.setValue(msg);
    }

    public MutableLiveData<String> getDeviceInfo(){
        return deviceName;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    public final String getDeviceMac() {
        return deviceMac;
    }

    public void set_hasUserSearchedNewDevice(boolean flag){
        hasUserSearchedNewDevice = flag;
    }

    public boolean get_hasUserSearchedNewDevice(){
        return hasUserSearchedNewDevice;
    }
}
