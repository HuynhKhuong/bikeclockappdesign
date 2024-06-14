package com.example.bikemonitor.combackground;

import android.util.Log;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;


import org.json.JSONException;
import org.json.JSONObject;
import java.util.*;

public class JsonPackage extends PackageInterface {
    // Debugging
    private static final String TAG = "BluetoothConnectionSetup";

    JsonPackage(int header, String valueStr){
        super(AttributesIndex.getAttributesIndex().attributeProvider(header),
                AttributesIndex.getAttributesIndex().dataProvider(valueStr));
    }

    JsonPackage(int header, int commandcode){
        super(AttributesIndex.getAttributesIndex().attributeProvider(header),
                AttributesIndex.getAttributesIndex().dataProvider(commandcode));
    }

    @Override
    public String packData(){
        String data = "";
        try{
            data = new JSONObject().put(m_attributes.getStringDatas(), m_datas.getStringDatas()).toString();
        }
        catch(org.json.JSONException e){
            Log.e(TAG, e.toString());
        }
        return data;
    }
}

class AttributesIndex{
    // Debugging
    private static final String TAG = "BluetoothConnectionSetup";
    private class ESP32Header implements DataInterface{
        ESP32Header(String userHeader){
            super();
            m_userHeader = userHeader;
        }

        @Override
        public String getStringDatas(){
            return m_userHeader;
        }

        @Override
        public int getIntDatas(){
            return 0;
        }

        private String m_userHeader;
    }


    public static class ESP32StringData implements DataInterface{
        ESP32StringData(String userHeader){
            super();
            m_userHeader = userHeader;
        }

        @Override
        public String getStringDatas(){
            return m_userHeader;
        }

        @Override
        public int getIntDatas(){
            return 0;
        }

        private String m_userHeader;
    }
    public static class ESP32IntValues implements DataInterface{
        ESP32IntValues(int data){
            super();
            m_userData = data;
        }

        @Override
        public int getIntDatas(){
            return m_userData ;
        }

        @Override
        public String getStringDatas(){
            return null ;
        }

        private int m_userData;
    }
    public static AttributesIndex getAttributesIndex(){
        if(m_AttributesIndex == null){
            m_AttributesIndex = new AttributesIndex();
        }
        return m_AttributesIndex;
    }

    private void initializeAttributesMap(){
        if(m_attributeselector.size() == 0){
            m_attributeselector.put(cmd, new ESP32Header("cmd"));
            m_attributeselector.put(skey, new ESP32Header("skey"));
            m_attributeselector.put(res, new ESP32Header("res"));
            m_attributeselector.put(islock, new ESP32Header("islock"));
            m_attributeselector.put(wd, new ESP32Header("wd"));
        }
    }

    private void initializeCommandCodeMap(){
        if(m_commandcodeselector.size() == 0){
            m_commandcodeselector.put(cmd_commands.connect, new ESP32StringData("connect"));
            m_commandcodeselector.put(cmd_commands.ulk, new ESP32StringData("ulk"));
            m_commandcodeselector.put(cmd_commands.inf, new ESP32StringData("inf"));
            m_commandcodeselector.put(cmd_commands.wconf, new ESP32StringData("wconf"));
            m_commandcodeselector.put(cmd_commands.rconf, new ESP32StringData("rconf"));
        }
    }

    public static final int cmd = 1;
    public static final int skey  = 2;
    public static final int res  = 3;
    public static final int islock  = 4;
    public static final int wd  = 5;

    public static class cmd_commands{
        public static int connect = 0;
        public static int ulk = 1;
        public static int inf = 2;
        public static int wconf = 3;
        public static int rconf = 4;
    }

    private Map<Integer, ESP32Header> m_attributeselector = new HashMap<Integer, ESP32Header>();
    private Map<Integer, ESP32StringData> m_commandcodeselector = new HashMap<Integer, ESP32StringData>();
    @Nullable
    public DataInterface attributeProvider(final int datacode){
        initializeAttributesMap();
        return m_attributeselector.get(datacode);
    }


    @Nullable
    public DataInterface dataProvider(final String dataStringcode){
        return new ESP32StringData(dataStringcode);
    }

    @Nullable
    public DataInterface dataProvider(final int datacommandcode){
        initializeCommandCodeMap();
        return m_commandcodeselector.get(datacommandcode);
    }

    private static AttributesIndex m_AttributesIndex;
}
