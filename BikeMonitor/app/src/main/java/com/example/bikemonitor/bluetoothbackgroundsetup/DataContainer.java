package com.example.bikemonitor.bluetoothbackgroundsetup;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bikemonitor.UserInfor;

public class DataContainer extends ViewModel {
    final int g_payloadSize = 1024;
    private UserInfor.RecordedAttribute currentDeviceInfo = new UserInfor.RecordedAttribute();
    private MutableLiveData<UserInfor> currentUserInfo = new MutableLiveData<UserInfor>();
    private MutableLiveData<Boolean> dataChangeNotify = new MutableLiveData<Boolean>();
    private boolean isFirstStartup = true;
    private boolean loginDone = false;

    public void setCloudData(UserInfor.RecordedAttribute receivedPayload) {
        this.currentDeviceInfo = receivedPayload;
    }

    public UserInfor.RecordedAttribute getCloudData() {
        return currentDeviceInfo;
    }

    public void notifyDataChange(){
        if(isFirstStartup){
            isFirstStartup = false;
            dataChangeNotify.setValue(true);
        }
        else{
            boolean currentDummyStatus = dataChangeNotify.getValue();
            dataChangeNotify.setValue(!currentDummyStatus);
        }

    }

    public MutableLiveData<Boolean> getDataChangeNotifier(){
        return dataChangeNotify;
    }

    public void setCurrentUserInfo(UserInfor userinfo) {
        this.currentUserInfo.setValue(userinfo);
    }

    public MutableLiveData<UserInfor> getCurrentUserInfo() {
        return currentUserInfo;
    }

    public void setLoginStatus(boolean status){
        loginDone = status;
    }

    public boolean getLoginStatus(){
        return loginDone;
    }
}
