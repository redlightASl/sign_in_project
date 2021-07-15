package cn.edu.dlut.mail.wuchen2020.signinapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import cn.edu.dlut.mail.wuchen2020.signinapp.databinding.FragmentTimetableBinding;

public class TimetableFragment extends Fragment {
    private FragmentTimetableBinding viewBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewBinding = FragmentTimetableBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
