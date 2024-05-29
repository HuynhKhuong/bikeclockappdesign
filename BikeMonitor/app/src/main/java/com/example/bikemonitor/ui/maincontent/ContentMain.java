package com.example.bikemonitor.ui.maincontent;

//import static androidx.constraintlayout.widget.ConstraintSet.VISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.bikemonitor.databinding.ContentMainBinding;
import com.example.bikemonitor.R;
import com.google.android.material.snackbar.Snackbar;

public class ContentMain extends Fragment{
    private ContentMainBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = ContentMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                       .setAction("Action", null)
                       .setAnchorView(R.id.fab).show();
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
