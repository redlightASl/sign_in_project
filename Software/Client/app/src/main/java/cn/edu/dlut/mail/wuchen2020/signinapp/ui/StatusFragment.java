package cn.edu.dlut.mail.wuchen2020.signinapp.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Objects;

import cn.edu.dlut.mail.wuchen2020.signinapp.R;
import cn.edu.dlut.mail.wuchen2020.signinapp.databinding.FragmentStatusBinding;
import cn.edu.dlut.mail.wuchen2020.signinapp.model.Course;
import cn.edu.dlut.mail.wuchen2020.signinapp.ui.adapter.SigninRecordAdapter;
import cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel.MainViewModel;
import cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel.StatusViewModel;

public class StatusFragment extends Fragment {
    private FragmentStatusBinding viewBinding;
    private MainViewModel mainViewModel;
    private StatusViewModel viewModel;
    private SigninRecordAdapter signinRecordAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewBinding = FragmentStatusBinding.inflate(inflater, container, false);
        viewBinding.getRoot().setOnRefreshListener(() -> {
            Integer userType = mainViewModel.getUserType().getValue();
            if (userType != null) {
                updateSigninStatus(userType);
            }
        });
        signinRecordAdapter = new SigninRecordAdapter();
        viewBinding.listHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        viewBinding.listHistory.setAdapter(signinRecordAdapter);
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
            if (signinStatus == null) {
                return;
            }
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
                viewBinding.textLesson.setVisibility(View.VISIBLE);
            } else {
                viewBinding.textLesson.setText("");
                viewBinding.textLesson.setVisibility(View.GONE);
            }
            viewBinding.getRoot().setRefreshing(false);
        });
        viewModel.getRecords().observe(getViewLifecycleOwner(), signinRecords -> {
            if (signinRecords == null) {
                return;
            }
            signinRecordAdapter.setRecords(signinRecords);
            signinRecordAdapter.notifyDataSetChanged();
        });
        viewModel.getStatusTeacher().observe(getViewLifecycleOwner(), signinStatus -> {
            if (signinStatus == null) {
                return;
            }
            boolean allSignin = Objects.equals(signinStatus.getSigninCount(), signinStatus.getTotalCount());
            int color = allSignin ? android.R.color.holo_green_dark : android.R.color.holo_red_dark;
            int icon = allSignin ? R.drawable.ic_check_box_checked_24 : R.drawable.ic_check_box_blank_24;
            viewBinding.cardStatus.setCardBackgroundColor(getResources().getColor(color));
            viewBinding.imageStatus.setImageResource(icon);
            if (signinStatus.getCourse() != null) {
                viewBinding.textStatus.setText(allSignin ? "已全部签到" : "还有同学没有签到");
                Course course = signinStatus.getCourse();
                viewBinding.textLesson.setText(course.getName() + " " + course.getLocation());
                viewBinding.textLesson.setVisibility(View.VISIBLE);
            } else {
                viewBinding.textStatus.setText("您当前无课");
                viewBinding.textLesson.setText("");
                viewBinding.textLesson.setVisibility(View.GONE);
            }
            viewBinding.getRoot().setRefreshing(false);
        });
    }

    private void updateSigninStatus(int userType) {
        if (userType == 0) { // 学生
            viewModel.updateStudentStatus();
            viewModel.updateStudentRecords();
        } else if (userType == 1) { // 教师
            viewModel.updateTeacherStatus();
        }
    }
}
