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


  ///Constructor
  private StateController(){ 

  }

  ///Static instance 
  private StateController g_StateController = null;

  ///States List
  private StatesInterface m_currentState = null;

}
