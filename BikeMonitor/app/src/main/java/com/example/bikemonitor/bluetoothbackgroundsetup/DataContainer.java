package com.example.bikemonitor.bluetoothbackgroundsetup;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bikemonitor.UserInfor;

public class DataContainer extends ViewModel {
    final int g_payloadSize = 1024;
    private MutableLiveData<UserInfor.RecordedAttribute> currentDeviceInfo = new MutableLiveData<UserInfor.RecordedAttribute>();

    public void setReceivedPayload(UserInfor.RecordedAttribute receivedPayload) {
        this.currentDeviceInfo.setValue(receivedPayload);
    }

    public MutableLiveData<UserInfor.RecordedAttribute> getReceivedPayload() {
        return currentDeviceInfo;
    }
}
