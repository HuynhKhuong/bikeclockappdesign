package com.example.bikemonitor.bluetoothbackgroundsetup;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bikemonitor.UserInfor;

public class DataContainer extends ViewModel {
    final int g_payloadSize = 1024;
    private UserInfor.RecordedAttribute currentDeviceInfo = new UserInfor.RecordedAttribute();
    private MutableLiveData<UserInfor> currentUserInfo = new MutableLiveData<UserInfor>();
    private MutableLiveData<Boolean> dataChangeNotify = new MutableLiveData<Boolean>();
    private MutableLiveData<Integer> indexNotifier = new MutableLiveData<Integer>();
    private MutableLiveData<Integer> requestNotifier = new MutableLiveData<Integer>();
    private int requestData = 0;

    private boolean isFirstStartup = true;
    private boolean firstIndexUpdate = true;
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


    public void updateRequestedId(){
        if(firstIndexUpdate && (currentUserInfo.getValue().getIndex() == 0)){
            firstIndexUpdate = false;
            currentUserInfo.getValue().setIndex(-1);
        }

        if(currentUserInfo.getValue().getIndex() < 5){
            currentUserInfo.getValue().setIndex(currentUserInfo.getValue().getIndex() + 1);
        }
        else{
            currentUserInfo.getValue().setIndex(0);
        }

        indexNotifier.setValue(currentUserInfo.getValue().getIndex());
    }

    public MutableLiveData<Integer> getIndexNotifier(){
        return indexNotifier;
    }

    public int getCurrentRequestedId(){
        return currentUserInfo.getValue().getIndex();
    }

    public void request(int requestCode, int requestData){
        this.requestData = requestData;
        requestNotifier.setValue(requestCode);
    }

    public int getRequestData(){
        return this.requestData;
    }
    public MutableLiveData<Integer> requestListener(){
        return requestNotifier;
    }

    public void setLoginStatus(boolean status){
        loginDone = status;
    }

    public boolean getLoginStatus(){
        return loginDone;
    }
}
