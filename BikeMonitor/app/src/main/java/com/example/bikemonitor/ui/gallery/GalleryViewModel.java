package com.example.bikemonitor.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GalleryViewModel extends ViewModel {

    private MutableLiveData<Boolean> dataChangeNotify = new MutableLiveData<Boolean>();
    private boolean isFirstStartup = true;

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
}