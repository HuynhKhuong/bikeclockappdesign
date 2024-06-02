package com.example.bikemonitor.ui.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.bikemonitor.R;
import com.example.bikemonitor.databinding.FragmentRegisterBinding;
import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RegisterViewModel loginViewModel =
                new ViewModelProvider(this).get(RegisterViewModel.class);

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView backToLoginText = binding.registerBackloginTrigger;

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

        CircularProgressButton registerButton = binding.cirRegisterButton;
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do nothing
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
