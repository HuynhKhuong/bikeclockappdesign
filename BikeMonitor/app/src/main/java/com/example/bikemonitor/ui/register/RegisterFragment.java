package com.example.bikemonitor.ui.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.bikemonitor.R;
import com.example.bikemonitor.UserInfor;
import com.example.bikemonitor.bluetoothbackgroundsetup.DataContainer;
import com.example.bikemonitor.databinding.FragmentRegisterBinding;
import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

// Firebase package
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.ChildEventListener;

import com.example.bikemonitor.UserInfor;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;

    // Firebase database reference
    private DatabaseReference m_Ref;
    private String m_userName;
    private String m_userEmail;
    private String m_userMobileNo;
    private String m_userPassword;
    private String m_userID;
    private UserInfor m_userInfo;

    public static final int invalidEmail = 0;
    public static final int invalidPhoneNo = 1;
    public static final int EmailUsed = 2;
    public static final int emptyUser = 3;
    public static final int successfullyReg = 4;

    private DataContainer UserInfoContainerViewModel;

    private void alertAction(int alertType)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        switch(alertType) {
            case emptyUser:
                alertDialogBuilder.setMessage("Please fill user Information");
                alertDialogBuilder.setTitle("User Information missed");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setNegativeButton("Exit", (DialogInterface.OnClickListener) (dialog, which) -> {
                    // If user click no then dialog box is canceled.
                    dialog.cancel();
                });
                break;
            case EmailUsed:
                alertDialogBuilder.setMessage("Please sign up other Email");
                alertDialogBuilder.setTitle("Email already used !");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setNegativeButton("Exit", (DialogInterface.OnClickListener) (dialog, which) -> {
                    // If user click no then dialog box is canceled.
                    dialog.cancel();
                });
                break;
            case invalidEmail:
                alertDialogBuilder.setMessage("Please sign up correct Email format");
                alertDialogBuilder.setTitle("Email format is incorrect !");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setNegativeButton("Exit", (DialogInterface.OnClickListener) (dialog, which) -> {
                    // If user click no then dialog box is canceled.
                    dialog.cancel();
                });
                break;
            case invalidPhoneNo:
                alertDialogBuilder.setMessage("Please sign up correct phone Number");
                alertDialogBuilder.setTitle("Phone number format is incorrect !");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setNegativeButton("Exit", (DialogInterface.OnClickListener) (dialog, which) -> {
                    // If user click no then dialog box is canceled.
                    dialog.cancel();
                });
                break;
            case successfullyReg:
                alertDialogBuilder.setMessage("Congratulation");
                alertDialogBuilder.setTitle("Your account registered successfully !");
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
    private boolean checkEmailFormat(String userEmail){
        String emailAT = userEmail.split("@")[1];
        return emailAT.equals("gmail.com");
    }

    private boolean checkPhoneFormat(String userPhoneNo){
        int phoneNoLength = userPhoneNo.length();
        return phoneNoLength == 10; // Phone Number Length must be 10 in Vietnam area
    }

    private boolean checkMailAlreadyUsed(String userID, String currentUser)
    {
            ///Temporarily not used
//        if (userID.equals(currentUser.split("@")[0])){
//            m_Ref.child(userID).child("Email2checkcc").setValue("true");
//            m_Ref.child(userID).child("userID2Check").setValue(userID);
//            m_Ref.child(userID).child("current").setValue(currentUser.split("@")[0]);
//            return true;
//        }
//        m_Ref.child(userID).child("Email2checkcc").setValue("false");
//        m_Ref.child(userID).child("userID2Check").setValue(userID);
//        m_Ref.child(userID).child("current").setValue(currentUser.split("@")[0]);
//        return false;
        return false;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RegisterViewModel loginViewModel =
                new ViewModelProvider(this).get(RegisterViewModel.class);

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView backToLoginText = binding.registerBackloginTrigger;

        m_userInfo = new UserInfor();
        UserInfoContainerViewModel = new ViewModelProvider(requireActivity()).get(DataContainer.class);

        backToLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).navigate(new NavDirections() {
                            @Override
                            public int getActionId() {
                                return R.id.action_nav_register_to_nav_login;
                            }

                            @NonNull
                            @Override
                            public Bundle getArguments() {
                                return null;
                            }
                });
            }
        });

        // Firebase reference
        m_Ref = FirebaseDatabase.getInstance().getReference();
        TextView m_nameText = binding.editTextName;
        TextView m_emailText = binding.editTextEmail;
        TextView m_mobileNoText = binding.editTextMobile;
        TextView m_passwordText = binding.editTextPassword;

        CircularProgressButton registerButton = binding.cirRegisterButton;
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_userName = m_nameText.getText().toString();
                m_userEmail = m_emailText.getText().toString();
                m_userMobileNo = m_mobileNoText.getText().toString();
                m_userPassword = m_passwordText.getText().toString();

                // Variable to check validity of user info
                boolean l_emailIsOK;
                boolean l_phoneNumberOK;
                boolean l_emailIsUsed;
                String l_currentUser;

                m_userInfo = new UserInfor();

                // get character before AT in email for user ID
                m_userID = m_userEmail.split("@")[0];

                if(m_userID != null && !m_userID.trim().isEmpty() &&
                        m_userMobileNo != null && !m_userMobileNo.trim().isEmpty() &&
                        m_userPassword != null && !m_userPassword.trim().isEmpty()) {
                    l_emailIsOK = checkEmailFormat(m_userEmail);
                    l_phoneNumberOK = checkPhoneFormat(m_userMobileNo);

//                    l_currentUser = (m_Ref.child(m_userID).child("Email").get()).toString();
//                    l_emailIsUsed = checkMailAlreadyUsed(m_userID, l_currentUser);
                    if(!l_emailIsOK){
                        alertAction(invalidEmail);
                    }
                    else if (!l_phoneNumberOK){
                        alertAction(invalidPhoneNo);
                    }
//                    else if (l_emailIsUsed)
//                        alertAction(EmailUsed);
//                    }

                    if(l_emailIsOK && l_phoneNumberOK)
                    {
                        m_Ref.child(m_userID).child("Name").setValue(m_userName);
                        m_Ref.child(m_userID).child("Email").setValue(m_userEmail);
                        m_Ref.child(m_userID).child("Mobile").setValue(m_userMobileNo);
                        m_Ref.child(m_userID).child("Password").setValue(m_userPassword);
                        m_Ref.child(m_userID).child("RecIndex").setValue(0);
                        m_Ref.child(m_userID).child("DevList").child("Record_0").child("DeviceRegSts").setValue(false);
                        m_Ref.child(m_userID).child("DevList").child("Record_0").child("Distance").setValue(0);
                        m_Ref.child(m_userID).child("DevList").child("Record_0").child("MinRec").setValue(0);
                        m_Ref.child(m_userID).child("DevList").child("Record_0").child("HourRec").setValue(0);
                        m_Ref.child(m_userID).child("DevList").child("Record_0").child("DayRec").setValue(0);
                        m_Ref.child(m_userID).child("DevList").child("Record_0").child("MonRec").setValue(0);
                        m_Ref.child(m_userID).child("DevList").child("Record_0").child("DevAddr").setValue("");
                        m_Ref.child(m_userID).child("DevList").child("Record_0").child("DevName").setValue("");
                        m_Ref.child(m_userID).child("DevList").child("Record_0").child("ActivePeriod").setValue(0);
                        alertAction(successfullyReg);
                    }

                }
                else{
                    alertAction(emptyUser);
                }
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
