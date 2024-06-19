package com.example.bikemonitor.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    public HomeViewModel() {
        m_displayItem = new MutableLiveData<Integer>();
        m_displayItem.setValue(0);
        m_mainSpeedDisplay = new MutableLiveData<Integer>();
        m_mainSpeedDisplay.setValue(0);
        m_lockStatusDisplay = new MutableLiveData<Boolean>();
        m_lockStatusDisplay.setValue(true);
        m_additionalOdoDisplay = new MutableLiveData<Integer>();
        m_additionalTripDisplay = new MutableLiveData<Integer>();
        m_addtionalAverageSpeedDisplay = new MutableLiveData<Double>();
        m_addtionalTimeDisplay = new MutableLiveData<Integer>();
        m_recorderStartFlag = new MutableLiveData<Boolean>();
        m_recorderStartFlag.setValue(false);
        startRecordTimeStamp = 0;
    }

    private final String[] m_displayUnit = new String[]{"km", "km", "km/h", "HH:MM", "km/h"};
    private String[] m_displayValue = new String[]{"0000", "0000", "0000", "00:00", "00"};
    private final String[] m_displayLegend = new String[]{"ODO: ", "TRIP: ","ASPD: ", "TIME: "};

    public int startRecordTimeStamp;
    private int syncupCounter = 0;
    private final MutableLiveData<Integer> m_displayItem;
    private final MutableLiveData<Integer> m_mainSpeedDisplay;

    private final MutableLiveData<Integer> m_additionalOdoDisplay;
    private final MutableLiveData<Integer> m_additionalTripDisplay;
    private final MutableLiveData<Double> m_addtionalAverageSpeedDisplay;
    private final MutableLiveData<Integer> m_addtionalTimeDisplay;
    private final MutableLiveData<Boolean> m_lockStatusDisplay;
    private final MutableLiveData<Boolean> m_recorderStartFlag;
    public static final int odoIndex = 0;
    public static final int tripIndex = 1;
    public static final int avgSpdIndex = 2;
    public static final int timeIndex = 3;

    private final int currentSpdIndex = 4;

    public void setDisplayedOdo(String value){
        m_displayValue[odoIndex] = value;
    }

    public void setDisplayedOdo(int value){
        String reportedValud = "";
        if(value < 10){
            reportedValud = "000"+Integer.toString(value);
        }
        else if(value < 100){
            reportedValud = "00"+Integer.toString(value);
        }
        else if(value < 1000){
            reportedValud = "0"+Integer.toString(value);
        }
        else{
            reportedValud = Integer.toString(value);
        }

        m_displayValue[odoIndex] = reportedValud;
        m_additionalOdoDisplay.setValue(value);
    }

    public void setDisplayedTrip(String value){
        m_displayValue[tripIndex] = value;
    }
    public void setDisplayedTrip(int value){
        String reportedValud = "";

        if(value < 10){
            reportedValud = "000"+Integer.toString(value);
        }
        else if(value < 100){
            reportedValud = "00"+Integer.toString(value);
        }
        else if(value < 1000){
            reportedValud = "0"+Integer.toString(value);
        }
        else{
            reportedValud = Integer.toString(value);
        }

        m_displayValue[tripIndex] = reportedValud;
        m_additionalTripDisplay.setValue(value);
    }

    public void setDisplayedAvdSpd(double value){
        String reportedValue = "";
        int integerPart = (int)value;
        int floatPart = (int) ((value - integerPart)*10);
        if(integerPart < 10){
            reportedValue = "0" + Integer.toString(integerPart);
            reportedValue += ".";
            reportedValue += Integer.toString(floatPart);
        }
        if(value < 100){
            reportedValue = Integer.toString(integerPart);
            reportedValue += ".";
            reportedValue += Integer.toString(floatPart);

        }
        else if(value < 1000){
            reportedValue = "0"+Integer.toString(integerPart);
        }
        else{
            reportedValue = Integer.toString(integerPart);
        }
        m_displayValue[avgSpdIndex] = reportedValue;
        m_addtionalAverageSpeedDisplay.setValue(value);
    }
    public void setDisplayedAvdSpd(String value){
        m_displayValue[avgSpdIndex] = value;
    }
    public void setDisplayedTime(String value){
        m_displayValue[timeIndex] = value;
    }
    public void setDisplayedTime(int value){
        int hours = value/60;
        int minutes = value%60;
        String reportedValue = "";
        if(hours < 10){
            reportedValue = "0" + Integer.toString(hours);
        }
        else{
            reportedValue = Integer.toString(hours);
        }

        reportedValue += ":";

        if(minutes < 10){
            reportedValue += "0" + Integer.toString(minutes);
        }
        else{
            reportedValue += Integer.toString(minutes);
        }

        m_displayValue[timeIndex] = reportedValue;
        m_addtionalTimeDisplay.setValue(value);
    }

    public void setDisplayedCurrentSpeed(String value){
            m_displayValue[currentSpdIndex] = value;
    }

    public void setDisplayedCurrentSpeed(int value){
        String reportValue = "";
        if(value < 10){
            reportValue = "000"+Integer.toString(value);
        }
        else{
            reportValue = Integer.toString(value);
        }
        m_displayValue[currentSpdIndex] = reportValue;
        m_mainSpeedDisplay.setValue(value);
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

    public void setLockIndicator(boolean status){
        m_lockStatusDisplay.setValue(status);
    }
    public void setRecorderStartFlag(Boolean status){m_recorderStartFlag.setValue(status);}
    public MutableLiveData<Boolean> getRecorderStartFlag() {
        return m_recorderStartFlag;
    }

    public MutableLiveData<Integer> getCurrentIndex(){
        return m_displayItem;
    }

    public MutableLiveData<Integer> getMainSpeedDisplay(){
        return m_mainSpeedDisplay;
    }
    public MutableLiveData<Boolean> getLockIndicator(){
        return m_lockStatusDisplay;
    }

    public MutableLiveData<Integer> getAdditionalOdoDisplay(){
        return m_additionalOdoDisplay;
    }
    public MutableLiveData<Integer> getAdditionalTripDisplay(){
        return m_additionalTripDisplay;
    }
    public MutableLiveData<Double> getAdditionalAvgSpdDisplay(){
        return m_addtionalAverageSpeedDisplay;
    }


    public MutableLiveData<Integer> getAdditionalTimeDisplay(){
        return m_addtionalTimeDisplay;
    }
}