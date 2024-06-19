package com.example.bikemonitor.ui.login;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

// Firebase package
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.ChildEventListener;

import com.example.bikemonitor.R;
import com.example.bikemonitor.databinding.FragmentLoginBinding;

import com.example.bikemonitor.UserInfor;
import java.util.Objects;
import com.example.bikemonitor.bluetoothbackgroundsetup.DataContainer;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    // Firebase reference
    private DatabaseReference m_Ref;

    // userEmail, userPassword from firebase
    public String f_userEmail, f_userPassword;
    private Boolean f_devRegSts;
    public String f_devAddr;
    public String f_devName;
    private int logIn_status;
    private int f_activePeriod;
    private int f_distance,f_monRec,f_dayRec,f_hourRec,f_minRec, f_index;

    // Enumerate status of email, password
    final int e_niceInfo = 0;
    final int e_emailMissed = 1;
    final int e_passwordMissed = 2;
    final int e_EmailWrongFormat = 3;
    final int e_EmailNotRegistered = 4;
    final int e_WrongPassword = 5;
    final int e_userLogInSuccessfully = 6;

    private UserInfor m_userInfo;
    private UserInfor.RecordedAttribute m_recAttribute;
    private DataContainer UserInfoContainerViewModel;
    private DataContainer DeviceInfoContainerViewModel;

    public interface FirebaseCallback {
        void onLoginStatusChanged(boolean loginStatus, String email, String password,
                                  Boolean devRegSts, String devAddr, String devName,
                                  int activePeriod, int mon, int day, int hour, int min, int distance, int index);
    }

    private void VerifyDataWithFirebase(DatabaseReference dtbRef, String userID,
                                        String userEmail, String userPassword,
                                        UserInfor m_user, FirebaseCallback callback){
        dtbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(UserInfoContainerViewModel.getLoginStatus() == false){
                    // To get index of current record version
                    f_userEmail = dataSnapshot.child(userID).child("Email").getValue(String.class);
                    f_userPassword = dataSnapshot.child(userID).child("Password").getValue(String.class);
                    f_index = dataSnapshot.child(userID).child("RecIndex").getValue(int.class);

                    f_devRegSts = dataSnapshot.child(userID).child("DevList").child("Record_" + String.valueOf(f_index)).child("DeviceRegSts").getValue(Boolean.class);
                    f_devAddr = dataSnapshot.child(userID).child("DevList").child("Record_" + String.valueOf(f_index)).child("DevAddr").getValue(String.class);
                    f_devName = dataSnapshot.child(userID).child("DevList").child("Record_" + String.valueOf(f_index)).child("DevName").getValue(String.class);
                    f_activePeriod = dataSnapshot.child(userID).child("DevList").child("Record_" + String.valueOf(f_index)).child("ActivePeriod").getValue(int.class);
                    f_monRec = dataSnapshot.child(userID).child("DevList").child("Record_" + String.valueOf(f_index)).child("MonRec").getValue(int.class);
                    f_dayRec = dataSnapshot.child(userID).child("DevList").child("Record_" + String.valueOf(f_index)).child("DayRec").getValue(int.class);
                    f_hourRec = dataSnapshot.child(userID).child("DevList").child("Record_" + String.valueOf(f_index)).child("HourRec").getValue(int.class);
                    f_minRec = dataSnapshot.child(userID).child("DevList").child("Record_" + String.valueOf(f_index)).child("MinRec").getValue(int.class);
                    f_distance = dataSnapshot.child(userID).child("DevList").child("Record_" + String.valueOf(f_index)).child("Distance").getValue(int.class);


                    if (!Objects.equals(userEmail, f_userEmail)){
                        alertAction(e_EmailNotRegistered);
                        m_user.setLogInSts(false);
                    }
                    else if (!Objects.equals(userPassword, f_userPassword)){
                        alertAction(e_WrongPassword);
                        m_user.setLogInSts(false);
                    }
                    else{
                        m_user.setLogInSts(true);
                    }
                    callback.onLoginStatusChanged(m_user.getLogInSts(),userEmail,userPassword,f_devRegSts,f_devAddr,f_devName,
                            f_activePeriod,f_monRec,f_dayRec,f_hourRec,f_minRec,f_distance, f_index);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Do nothing - update later if necessary
            }

        });
    }

    private int ValidateUserInfo(String userEmail, String userPassword) {
        int retStatus = e_niceInfo;
        if(userEmail.isEmpty()){
            retStatus = e_emailMissed;
        }
        else if(userEmail.contains("@")){
            String l_afterID = userEmail.split("@")[1];
            if(!l_afterID.equals("gmail.com")){
                retStatus = e_EmailWrongFormat;
            }
        }
        else if(!userEmail.contains("@")){
            retStatus = e_EmailWrongFormat;
        }
        else if(userPassword.isEmpty()){
            retStatus = e_passwordMissed;
        }
        return retStatus;

    }

    private void alertAction(int alertType)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        switch(alertType) {
            case e_emailMissed:
                alertDialogBuilder.setMessage("Please fill-in your Email");
                alertDialogBuilder.setTitle("Missing Email");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setNegativeButton("Exit", (DialogInterface.OnClickListener) (dialog, which) -> {
                    // If user click no then dialog box is canceled.
                    dialog.cancel();
                });
                break;
            case e_passwordMissed:
                alertDialogBuilder.setMessage("Please fill-in password");
                alertDialogBuilder.setTitle("Missing password");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setNegativeButton("Exit", (DialogInterface.OnClickListener) (dialog, which) -> {
                    // If user click no then dialog box is canceled.
                    dialog.cancel();
                });
                break;
            case e_EmailWrongFormat:
                alertDialogBuilder.setMessage("Please sign in correct Email format");
                alertDialogBuilder.setTitle("Email format is incorrect !");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setNegativeButton("Exit", (DialogInterface.OnClickListener) (dialog, which) -> {
                    // If user click no then dialog box is canceled.
                    dialog.cancel();
                });
                break;
            case e_EmailNotRegistered:
                alertDialogBuilder.setMessage("Please sign-up to continue");
                alertDialogBuilder.setTitle("Your account invalid !");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setNegativeButton("Exit", (DialogInterface.OnClickListener) (dialog, which) -> {
                    // If user click no then dialog box is canceled.
                    dialog.cancel();
                });
                break;
            case e_WrongPassword:
                alertDialogBuilder.setMessage("Please fill correct password");
                alertDialogBuilder.setTitle("Your password is incorrect !");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setNegativeButton("Exit", (DialogInterface.OnClickListener) (dialog, which) -> {
                    // If user click no then dialog box is canceled.
                    dialog.cancel();
                });
                break;

        }

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LoginViewModel loginViewModel =
                new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Firebase reference
        m_Ref = FirebaseDatabase.getInstance().getReference();
        TextView m_emailText = binding.editTextEmail;
        TextView m_passwordText = binding.editTextPassword;
        m_userInfo = new UserInfor();
        UserInfor.RecordedAttribute m_recAttribute = new UserInfor.RecordedAttribute();

        UserInfoContainerViewModel = new ViewModelProvider(requireActivity()).get(DataContainer.class);
        DeviceInfoContainerViewModel = new ViewModelProvider(requireActivity()).get(DataContainer.class);


        binding.cirLoginButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.cirLoginButton.startAnimation();
                        ///Some asynchronous task

                        // get info from Textview
                        String l_email = m_emailText.getText().toString();
                        String l_password = m_passwordText.getText().toString();
                        String l_userID;
                        int l_validateInfo;
                        // Validate email and password is valid
                        l_validateInfo = ValidateUserInfo(l_email,l_password);
                        if(l_validateInfo != e_niceInfo){
                            alertAction(l_validateInfo);
                        }
                        else {
                            // Get user Email and password from firebase to verify
                            l_userID = l_email.split("@")[0];
                            VerifyDataWithFirebase(m_Ref, l_userID, l_email, l_password, m_userInfo, new FirebaseCallback() {
                                @Override
                                public void onLoginStatusChanged(boolean loginStatus, String email, String password, Boolean devRegSts, String devAddr, String devName,
                                                                 int activePeriod, int mon, int day, int hour, int min, int distance, int index) {
                                    if(loginStatus){
                                        // User infor set
                                        m_userInfo.setUserEmail(email);
                                        m_userInfo.setUserPassword(password);
                                        m_userInfo.setUserID(email.split("@")[0]);
                                        m_userInfo.setIndex(index);

                                        // Device attribute set
                                        m_recAttribute.setUserDevice(devName);
                                        m_recAttribute.setDevRegSts(devRegSts);
                                        m_recAttribute.setDevAddress(devAddr);
                                        m_recAttribute.setActivePeriod(activePeriod);
                                        m_recAttribute.setMonRec(mon);
                                        m_recAttribute.setDayRec(day);
                                        m_recAttribute.setHourRec(hour);
                                        m_recAttribute.setMinRec(min);
                                        m_recAttribute.setUserDistance(distance);

                                        // Data container set
                                        UserInfoContainerViewModel.setCurrentUserInfo(m_userInfo);
                                        DeviceInfoContainerViewModel.setCloudData(m_recAttribute);
                                        DeviceInfoContainerViewModel.setLoginStatus(true);
                                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_login_to_nav_home);
                                    }
                                    // Do something with the login status
                                    // For example, navigate to another screen
                                    // Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_login_to_nav_gallery);
                                    m_userInfo.setLogInSts(loginStatus);
                                }
                            });
                        }

                        ///move to other state
                        binding.cirLoginButton.revertAnimation();

                        //Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_login_to_nav_gallery);
                    }
                }
        );
        binding.registertrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).navigate(new NavDirections() {
                    @Override
                    public int getActionId() {
                        return R.id.action_nav_login_to_nav_register;
                    }

                    @NonNull
                    @Override
                    public Bundle getArguments() {
                        return null;
                    }
                });
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

