package cn.edu.dlut.mail.wuchen2020.signinapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import cn.edu.dlut.mail.wuchen2020.signinapp.databinding.FragmentUserBinding;
import cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel.MainViewModel;
import cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel.UserViewModel;

public class UserFragment extends Fragment {
    private FragmentUserBinding viewBinding;
    private MainViewModel mainViewModel;
    private UserViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewBinding = FragmentUserBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        mainViewModel.getUserType().observe(getViewLifecycleOwner(), userType -> {
            if (userType == null) {
                return;
            }
            if (userType == 0) { // 学生
                viewModel.updateStudentInfo();
            } else if (userType == 1) { // 教师
                viewModel.updateTeacherInfo();
            }
        });
        viewModel.getStudent().observe(getViewLifecycleOwner(), student -> {
            if (student == null) {
                return;
            }
            String str = "姓名: " + student.getName() + "\n学号: " + student.getNumber()
                    + "\n班级: " + student.getClassName();
            viewBinding.textUser.setText(str);
        });
        viewModel.getTeacher().observe(getViewLifecycleOwner(), teacher -> {
            if (teacher == null) {
                return;
            }
            String str = "姓名: " + teacher.getName() + "\n工号: " + teacher.getNumber();
            viewBinding.textUser.setText(str);
        });
        // TODO 画个好看点的UI
    }
}
