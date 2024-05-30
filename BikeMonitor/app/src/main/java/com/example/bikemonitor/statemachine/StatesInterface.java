package com.example.bikemonitor.statemachine;

public interface StatesInterface {

  public void enterState(StateController obj);
  public void run();
  public void exitState(StateController obj);

  //leave space for UI Interfacing

}
