package cn.edu.dlut.mail.wuchen2020.signinapp.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import cn.edu.dlut.mail.wuchen2020.signinapp.R;
import cn.edu.dlut.mail.wuchen2020.signinapp.databinding.FragmentStatusBinding;
import cn.edu.dlut.mail.wuchen2020.signinapp.model.Course;
import cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel.MainViewModel;
import cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel.StatusViewModel;

public class StatusFragment extends Fragment {
    private FragmentStatusBinding viewBinding;
    private MainViewModel mainViewModel;
    private StatusViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewBinding = FragmentStatusBinding.inflate(inflater, container, false);
        viewBinding.getRoot().setOnRefreshListener(() -> {
            Integer userType = mainViewModel.getUserType().getValue();
            if (userType != null) {
                updateSigninStatus(userType);
            }
        });
        return viewBinding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        viewModel = new ViewModelProvider(requireActivity()).get(StatusViewModel.class);
        mainViewModel.getUserType().observe(getViewLifecycleOwner(), userType -> {
            if (userType == null) {
                return;
            }
            updateSigninStatus(userType);
        });
        viewModel.getStatus().observe(getViewLifecycleOwner(), signinStatus -> {
            int color;
            int icon;
            String str;
            switch (signinStatus.getStatus()) {
                case 0: color = android.R.color.holo_green_dark; icon = R.drawable.ic_check_box_checked_24; str = "您已签到"; break;
                case 1: color = android.R.color.holo_red_dark; icon = R.drawable.ic_check_box_blank_24; str = "尚未签到"; break;
                case 2: color = android.R.color.holo_green_dark; icon = R.drawable.ic_check_box_checked_24; str = "当前无课"; break;
                case 4: color = android.R.color.holo_orange_dark; icon = R.drawable.ic_check_box_checked_24; str = "暂离中"; break;
                case 5: color = android.R.color.holo_green_dark; icon = R.drawable.ic_check_box_checked_24; str = "您已返回"; break;
                case 6: color = android.R.color.holo_green_dark; icon = R.drawable.ic_check_box_checked_24; str = "您已签退"; break;
                default: color = android.R.color.holo_orange_dark; icon = R.drawable.ic_check_box_blank_24; str = "发生未知错误"; break;
            }
            viewBinding.cardStatus.setCardBackgroundColor(getResources().getColor(color));
            viewBinding.imageStatus.setImageResource(icon);
            viewBinding.textStatus.setText(str);
            if (signinStatus.getStatus() != 2) {
                Course course = signinStatus.getCourse();
                viewBinding.textLesson.setText(course.getName() + " " + course.getLocation());
            } else {
                viewBinding.textLesson.setText("");
            }
            viewBinding.getRoot().setRefreshing(false);
        });
    }

    private void updateSigninStatus(int userType) {
        if (userType == 0) { // 学生
            viewModel.updateStudentSigninStatus();
        } else if (userType == 1) { // 教师
            viewModel.updateTeacherSigninStatus();
        }
    }
}
