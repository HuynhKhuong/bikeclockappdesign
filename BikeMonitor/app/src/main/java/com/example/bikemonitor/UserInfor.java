package com.example.bikemonitor;

public class UserInfor {
    private String m_userName;
    private String m_userEmail;
    private String m_userMobileNo;
    private String m_userPassword;
    private String m_userID;
    private boolean m_logInSuccessSts;

    // Recorded class for each user
    static class RecordedAttribute{
        private int m_userDistance;
        private int m_userAvrVelocity;
        private int m_userDateRecorded;
        private int m_userTimeRecorded;
        private int m_userDevice;
        private int m_order;

        public RecordedAttribute(int order, int distance, int avr_velo, int date, int time, int device)
        {
            this.m_order = order;
            this.m_userDistance = distance;
            this.m_userAvrVelocity = avr_velo;
            this.m_userDateRecorded = date;
            this.m_userTimeRecorded = time;
            this.m_userDevice = device;
        }

        public int getUserDistance()
        {
            return m_userDistance;
        }

        public int getUserAverageVelocity()
        {
            return m_userAvrVelocity;
        }

        public int getUserDateRecorded()
        {
            return m_userDateRecorded;
        }

        public int getUserTimeRecorded()
        {
            return m_userTimeRecorded;
        }

        public int getUserDevice()
        {
            return m_userDevice;
        }

        public int getOrder()
        {
            return m_order;
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


}

