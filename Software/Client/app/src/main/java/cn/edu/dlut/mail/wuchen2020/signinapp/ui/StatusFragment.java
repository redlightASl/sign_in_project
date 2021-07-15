package cn.edu.dlut.mail.wuchen2020.signinapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import cn.edu.dlut.mail.wuchen2020.signinapp.databinding.FragmentStatusBinding;
import cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel.MainViewModel;
import cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel.StatusViewModel;

public class StatusFragment extends Fragment {
    private FragmentStatusBinding viewBinding;
    private MainViewModel mainViewModel;
    private StatusViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewBinding = FragmentStatusBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        viewModel = new ViewModelProvider(requireActivity()).get(StatusViewModel.class);
        mainViewModel.getUserType().observe(getViewLifecycleOwner(), userType -> {
            if (userType == null) {
                return;
            }
            if (userType == 0) { // 学生

            } else if (userType == 1) { // 教师

            }
        });
    }
}
