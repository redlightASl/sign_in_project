package cn.edu.dlut.mail.wuchen2020.signinapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import cn.edu.dlut.mail.wuchen2020.signinapp.databinding.FragmentUserBinding;
import cn.edu.dlut.mail.wuchen2020.signinapp.ui.adapter.UserInfoAdapter;
import cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel.MainViewModel;
import cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel.UserViewModel;

public class UserFragment extends Fragment {
    private FragmentUserBinding viewBinding;
    private MainViewModel mainViewModel;
    private UserViewModel viewModel;
    private UserInfoAdapter userInfoAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewBinding = FragmentUserBinding.inflate(inflater, container, false);
        userInfoAdapter = new UserInfoAdapter();
        viewBinding.recyclerUserInfo.setLayoutManager(new LinearLayoutManager(getActivity()));
        viewBinding.recyclerUserInfo.setAdapter(userInfoAdapter);
        viewBinding.buttonLogout.setOnClickListener(view -> {
            viewModel.logout();
            startLoginActivity();
        });
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
            viewBinding.textName.setText(student.getName());
            viewBinding.textType.setText("学生");
            userInfoAdapter.putUserInfo("姓名", student.getName());
            userInfoAdapter.putUserInfo("学号", student.getNumber());
            userInfoAdapter.putUserInfo("班级", student.getClassName());
            userInfoAdapter.putUserInfo("专业", student.getMajor() != null ? student.getMajor() : "无");
            userInfoAdapter.putUserInfo("学院", student.getDepartment() != null ? student.getDepartment() : "无");
        });
        viewModel.getTeacher().observe(getViewLifecycleOwner(), teacher -> {
            if (teacher == null) {
                return;
            }
            viewBinding.textName.setText(teacher.getName());
            viewBinding.textType.setText("教师");
            userInfoAdapter.putUserInfo("姓名", teacher.getName());
            userInfoAdapter.putUserInfo("工号", teacher.getNumber());
            userInfoAdapter.putUserInfo("任课班级", teacher.getClassName() != null ? teacher.getClassName() : "无");
        });
    }

    private void startLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
}
