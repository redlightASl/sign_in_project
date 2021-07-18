package cn.edu.dlut.mail.wuchen2020.signinapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import cn.edu.dlut.mail.wuchen2020.signinapp.databinding.FragmentTimetableBinding;
import cn.edu.dlut.mail.wuchen2020.signinapp.model.Course;
import cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel.MainViewModel;
import cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel.TimetableViewModel;

public class TimetableFragment extends Fragment {
    private FragmentTimetableBinding viewBinding;
    private MainViewModel mainViewModel;
    private TimetableViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewBinding = FragmentTimetableBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        viewModel = new ViewModelProvider(requireActivity()).get(TimetableViewModel.class);
        mainViewModel.getUserType().observe(getViewLifecycleOwner(), userType -> {
            if (userType == null) {
                return;
            }
            viewModel.updateTimetable(userType);
        });
        viewModel.getTimetable().observe(getViewLifecycleOwner(), courses -> {
            StringBuilder sb = new StringBuilder();
            for (Course course : courses) {
                sb.append(course.getName())
                    .append(" ")
                    .append(course.getLocation());
                if (course.getTeacherName() != null) {
                    sb.append(" ")
                        .append(course.getTeacherName());
                }
                sb.append("\n");
            }
            viewBinding.textTest.setText(sb.toString());
        });
    }
}
