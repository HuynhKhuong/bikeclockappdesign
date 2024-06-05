package com.example.bikemonitor.ui.devicelist;

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
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bikemonitor.R;
import com.example.bikemonitor.databinding.FragmentDevicelistBinding;

public class DeviceListFragment extends Fragment {

    private FragmentDevicelistBinding binding;
//    private class customOnBackPressed extends OnBackPressedCallback{
//        customOnBackPressed(){
//            super(true);
//        }
//
//        @Override
//        public void handleOnBackPressed(){
//            //by default do nothing
//            //no Navigator.popStack() is called
//        }
//    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView m_textView;
        public MyViewHolder(View view){
            super(view);
            m_textView = (TextView) view.findViewById(R.id.text_View);
        }

        public TextView getTextView(){
            return m_textView;
        }
    };

    private class CustomAdapter extends RecyclerView.Adapter<MyViewHolder>{
        private String[] localDataSet;
        CustomAdapter(){
            localDataSet = new String[]{"A", "B", "C"};
        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View currentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.devicelist, parent, false);
            return new MyViewHolder(currentView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.getTextView().setText(localDataSet[position]);
        }

        @Override
        public int getItemCount() {
            return localDataSet.length;
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDevicelistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        CustomAdapter myCustomAdapter = new CustomAdapter();
        RecyclerView rvDrawer = binding.rvDrawer;
        rvDrawer.setAdapter(myCustomAdapter);
        rvDrawer.setLayoutManager(new LinearLayoutManager(getActivity()));

//        OnBackPressedCallback backGesture = new customOnBackPressed();
//        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), backGesture);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}