package com.example.bikemonitor;

public class UserInfor {
    private String m_userName;
    private String m_userEmail;
    private String m_userMobileNo;
    private String m_userPassword;
    private String m_userID;
    private boolean m_logInSuccessSts;
    private int m_index;

    // Recorded class for each user
    public static class RecordedAttribute{
        private int m_userDistance;
        private double m_userAvrVelocity;
        private int m_userDateRecorded;
        private int m_userTimeRecorded;
        private String m_userDevice;
        private int m_order;
        // minute at the recorded time
        private int m_minRec;

        // hour at the recorded time
        private int m_hourRec;
        // Day at the recorded time
        private int m_dayRec;
        // month at the recorded time
        private int m_monRec;
        private String m_userDevAddress;

        // Indicate device registered status
        private boolean m_DevRegisteredSts;
        private int m_activePeriod;


        public int getUserDistance()
        {
            return m_userDistance;
        }

        public void setUserDistance(int data)
        {
            m_userDistance = data;
        }
        public double getUserAverageVelocity()
        {
            return m_userAvrVelocity;
        }

        public void setUserAverageVelocity(double data)
        {
            m_userAvrVelocity = data;
        }

        public int getUserDateRecorded()
        {
            return m_userDateRecorded;
        }

        public int getUserTimeRecorded()
        {
            return m_userTimeRecorded;
        }

        public String getUserDevice()
        {
            return m_userDevice;
        }

        public int getOrder()
        {
            return m_order;
        }
        public int getMinRec()
        {
            return m_minRec;
        }
        public int getHourRec()
        {
            return m_hourRec;
        }
        public int getDayRec()
        {
            return m_dayRec;
        }
        public int getMonRec()
        {
            return m_monRec;
        }
        public String getDevAddr()
        {
            return m_userDevAddress;
        }

        public boolean getDevRegSts(){return m_DevRegisteredSts;}
        public int getActivePeriod(){return m_activePeriod;}

        public void setMinRec(int min){
            this.m_minRec = min;
        }

        public void setHourRec(int hour){
            this.m_hourRec = hour;
        }
        public void setDayRec(int day){
            this.m_dayRec = day;
        }
        public void setMonRec(int mon){
            this.m_monRec = mon;
        }
        public void setDevAddress(String devAddr){
            this.m_userDevAddress = devAddr;
        }
        public void setUserDevice(String usrDev){
            this.m_userDevice = usrDev;
        }
        public void setDevRegSts(boolean sts){
            this.m_DevRegisteredSts = sts;
        }
        public void setActivePeriod(int deltatime){
            this.m_activePeriod = deltatime;
        }

    }


    // Temporarily not use constructor
    // UserInfor Constructor for LogIn action
//    public UserInfor (String user_email, String user_password)
//    {
//        m_userEmail = user_email;
//        m_userPassword = user_password;
//    }
//
//
//    // UserInfor Constructor for Register action
//    public UserInfor (String user_name, String user_email, String user_mobileNo, String user_password)
//    {
//        m_userName = user_name;
//        m_userEmail = user_email;
//        m_userMobileNo = user_mobileNo;
//        m_userPassword = user_password;
//    }
    public void setUserName(String user_name)
    {
        this.m_userName = user_name;
    }

    public String getUserName()
    {
        return m_userName;
    }

    public void setUserEmail(String user_email)
    {
        this.m_userEmail = user_email;
    }

    public String getUserEmail()
    {
        return m_userEmail;
    }

    public void setUserMobileNo(String user_mobileno)
    {
        this.m_userMobileNo = user_mobileno;
    }

    public String getUserMobileNo()
    {
        return m_userMobileNo;
    }

    public void setUserPassword(String user_password)
    {
        this.m_userPassword = user_password;
    }

    public void SetIndex(int index){
        this.m_index = index;
    }
    public String getUserPassword()
    {
        return m_userPassword;
    }

    public void setUserID(String user_id)
    {
        this.m_userID = user_id;
    }
    public String getUserID()
    {
        return m_userID;
    }

    public void setLogInSts(boolean sts)
    {
        this.m_logInSuccessSts = sts;
    }
    public boolean getLogInSts()
    {
        return m_logInSuccessSts;
    }
    public int getIndex() {return m_index;}


}

