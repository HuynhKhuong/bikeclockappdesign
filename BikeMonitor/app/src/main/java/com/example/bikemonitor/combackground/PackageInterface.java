package com.example.bikemonitor.combackground;

public abstract class PackageInterface{
  public PackageInterface(DataInterface attributes, DataInterface datas){
    m_attributes = attributes;
    m_datas = datas;
  }

  public abstract String packData();

  public DataInterface get_attributes(){
    return m_attributes;
  }

  public DataInterface get_datas(){
    return m_datas;
  }
  protected DataInterface m_attributes;
  protected DataInterface m_datas;
}


