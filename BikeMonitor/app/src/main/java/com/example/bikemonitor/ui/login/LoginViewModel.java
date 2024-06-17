package com.example.bikemonitor.ui.login;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel{

    public LoginViewModel() {
        m_buttonAnimationStart = new MutableLiveData<Boolean>();
    }

    private MutableLiveData<Boolean> m_buttonAnimationStart;

    public MutableLiveData<Boolean> getbuttonAnimationStart() {
        return m_buttonAnimationStart;
    }

    public void setbuttonAnimationStart(Boolean buttonAnimationStart) {
        this.m_buttonAnimationStart.setValue(buttonAnimationStart);
    }
}
