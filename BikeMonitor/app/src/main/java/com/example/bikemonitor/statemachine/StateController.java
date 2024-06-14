package com.example.bikemonitor.statemachine;

import androidx.annotation.NonNull;

public class StateController {

  ///Make use of Singleton for file Sharing
  @NonNull
  public StateController getInstance(){
    if(g_StateController == null)
    {
      g_StateController = new StateController();
    }

    return g_StateController;
  }

  public void transitState(){
    m_currentState.exitState(this);  
    m_currentState.enterState(this);
  }

  public void setState(StatesInterface nextState){
    m_currentState = nextState;
  }

  public void currentStateRun(){
    m_currentState.run();
  }

  public interface StatesInterface {

    public void enterState(StateController obj);
    public void run();
    public void exitState(StateController obj);

    //leave space for UI Interfacing

  }

  ///Constructor
  private StateController(){ 

  }

  ///Static instance 
  private StateController g_StateController = null;

  ///States List
  private StatesInterface m_currentState = null;

  ///States List
  private StatesInterface m_loginState = null;
  private StatesInterface m_registerState = null;
  private StatesInterface m_deviceConnectEstablishState = null;
  private StatesInterface m_deviceConfigurationState = null;
  private StatesInterface m_deviceDisplayState = null;
}
