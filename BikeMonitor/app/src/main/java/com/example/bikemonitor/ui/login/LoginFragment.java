package com.example.bikemonitor.ui.login;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.bikemonitor.R;
import com.example.bikemonitor.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LoginViewModel loginViewModel =
                new ViewModelProvider(this).get(LoginViewModel.class);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.cirLoginButton.setOnClickListener(
        new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  binding.cirLoginButton.startAnimation();
                  ///Some asynchronous task 
                  ///move to other state
                  binding.cirLoginButton.stopAnimation();

                  Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_login_to_nav_home);
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
