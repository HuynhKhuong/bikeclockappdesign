package com.example.bikemonitor.bluetoothbackgroundsetup;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bikemonitor.UserInfor;

public class DataContainer extends ViewModel {
    final int g_payloadSize = 1024;
    private MutableLiveData<UserInfor.RecordedAttribute> currentDeviceInfo = new MutableLiveData<UserInfor.RecordedAttribute>();
    private MutableLiveData<UserInfor> currentUserInfo = new MutableLiveData<UserInfor>();

    public void setCloudData(UserInfor.RecordedAttribute receivedPayload) {
        this.currentDeviceInfo.setValue(receivedPayload);
    }

    public MutableLiveData<UserInfor.RecordedAttribute> getCloudData() {
        return currentDeviceInfo;
    }

    public void setCurrentUserInfo(UserInfor userinfo) {
        this.currentUserInfo.setValue(userinfo);
    }

    public MutableLiveData<UserInfor> getCurrentUserInfo() {
        return currentUserInfo;
    }
}
