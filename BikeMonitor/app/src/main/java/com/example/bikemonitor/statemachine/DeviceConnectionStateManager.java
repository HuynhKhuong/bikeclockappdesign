package com.example.bikemonitor.statemachine;

import android.bluetooth.BluetoothClass;

public class DeviceConnectionStateManager{

  public static DeviceConnectionStateManager getDeviceConnectionStateManager(){
    if(obj == null){
      obj = new DeviceConnectionStateManager();
    }
    return obj;
  }
  private DeviceConnectionStateManager(){
    currentState = DEVICE_DISCONNECTED;
  }

  public int getCurrentState(){
    return currentState;
  }
  public void updateState(int nextState){
    if(nextState > DEVICE_ACCEPTED_UPDATESETTINGS) return;

    if(currentState == DEVICE_FORCEUNLOCK ||
        currentState == DEVICE_ACCEPTED_UPDATESETTINGS){
      currentState = lastState;
    }
    else{
      lastState = currentState;
      currentState = nextState;
    }
  }
  public static final int DEVICE_ACCEPTED = 0;
  public static final int DEVICE_ACCEPTED_READCURRENTDATA = 4;
  public static final int DEVICE_ACCEPTED_UPDATESETTINGS = 5;
  public static final int DEVICE_DISCONNECTED = 1;
  public static final int DEVICE_LISTENING = 2;
  public static final int DEVICE_FORCEUNLOCK = 3;
  private int currentState;
  private int lastState;
  private int m_targetSettingsUpdate = 0;

  private static DeviceConnectionStateManager obj;
}



