package com.example.bikemonitor.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.bikemonitor.R;
import com.example.bikemonitor.databinding.FragmentSlideshowBinding;
import com.example.bikemonitor.databinding.LogoutButtonBinding;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private class customOnBackPressed extends OnBackPressedCallback {
        customOnBackPressed(){
            super(true);
        }

        @Override
        public void handleOnBackPressed(){
            //by default do nothing
            //no Navigator.popStack() is called
        }
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        OnBackPressedCallback backGesture = new customOnBackPressed();
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), backGesture);

        LogoutButtonBinding logoutButtonBinding = binding.logoutFromSlideshow;
        Button button = logoutButtonBinding.logoutButton;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_nav_slideshow_to_nav_login);
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