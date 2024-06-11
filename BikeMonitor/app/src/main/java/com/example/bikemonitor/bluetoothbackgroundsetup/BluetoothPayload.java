package com.example.bikemonitor.bluetoothbackgroundsetup;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bikemonitor.UserInfor;

public class BluetoothPayload extends ViewModel {
    final int g_payloadSize = 1024;
    private MutableLiveData<UserInfor.RecordedAttribute> currentDeviceInfo = new MutableLiveData<UserInfor.RecordedAttribute>();

    public void setReceivedPayload(MutableLiveData<UserInfor.RecordedAttribute> receivedPayload) {
        this.currentDeviceInfo = receivedPayload;
    }

    public MutableLiveData<UserInfor.RecordedAttribute> getReceivedPayload() {
        return currentDeviceInfo;
    }
}
