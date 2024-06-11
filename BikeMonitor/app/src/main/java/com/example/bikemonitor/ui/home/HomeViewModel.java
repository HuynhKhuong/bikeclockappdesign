package com.example.bikemonitor.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    public HomeViewModel() {
        m_displayItem = new MutableLiveData<Integer>();
        m_displayItem.setValue(0);
    }

    private final String[] m_displayUnit = new String[]{"km", "km", "km/h", "HH:MM", "km/h"};
    private String[] m_displayValue = new String[]{"0000", "0000", "0000", "00:00", "00"};
    private final String[] m_displayLegend = new String[]{"ODO: ", "TRIP: ","ASPD: ", "TIME: "};

    private int syncupCounter = 0;
    private final MutableLiveData<Integer> m_displayItem;
    private final int odoIndex = 0;
    private final int tripIndex = 1;
    private final int avgSpdIndex = 2;
    private final int timeIndex = 3;

    private final int currentSpdIndex = 4;

    public void setDisplayedOdo(String value){
        m_displayValue[odoIndex] = value;
    }
    public void setDisplayedTrip(String value){
        m_displayValue[tripIndex] = value;
    }
    public void setDisplayedAvdSpd(String value){
        m_displayValue[avgSpdIndex] = value;
    }
    public void setDisplayedTime(String value){
        m_displayValue[timeIndex] = value;
    }

    public void setDisplayedCurrentSpeed(String value){
        m_displayValue[currentSpdIndex] = value;
    }
    public String getDisplayCurrentSpeedValue(){
        return m_displayValue[currentSpdIndex];
    }
    public String getDisplayCurrentSpeedUnit(){
        return m_displayUnit[currentSpdIndex];
    }

    public String getDisplayUnit(){
        if(m_displayItem.getValue() == null){
            return m_displayUnit[syncupCounter];
        }
        return m_displayUnit[m_displayItem.getValue()];
    }
    public String getDisplayValue(){
        if(m_displayItem.getValue() == null){
            return m_displayValue[syncupCounter];
        }
        return m_displayValue[m_displayItem.getValue()];
    }
    public String getDisplayLegend(){
        if(m_displayItem.getValue() == null){
            return m_displayLegend[syncupCounter];
        }
        return m_displayLegend[m_displayItem.getValue()];
    }

    public void updateCurrentUnit(){
        final int maxIndex = 3;
        if(m_displayItem.getValue() == null){
            if(syncupCounter == maxIndex){
                syncupCounter = 0;
            }
            else{
               syncupCounter++;
            }
            m_displayItem.setValue(syncupCounter);
        }
        else{
            if(m_displayItem.getValue() == maxIndex){
                m_displayItem.setValue(0);
            }
            else{
                m_displayItem.setValue(m_displayItem.getValue()+1);
            }
        }

    }

    public MutableLiveData<Integer> getCurrentIndex(){
        return m_displayItem;
    }

}