package cn.edu.dlut.mail.wuchen2020.signinapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import cn.edu.dlut.mail.wuchen2020.signinapp.databinding.FragmentTimetableBinding;
import cn.edu.dlut.mail.wuchen2020.signinapp.util.AndroidUtil;
import cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel.MainViewModel;
import cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel.TimetableViewModel;

public class TimetableFragment extends Fragment {
    private FragmentTimetableBinding viewBinding;
    private MainViewModel mainViewModel;
    private TimetableViewModel viewModel;
    private ArrayAdapter<Integer> weekAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewBinding = FragmentTimetableBinding.inflate(inflater, container, false);
        viewBinding.getRoot().setOnRefreshListener(() -> {
            if (mainViewModel.getUserType().getValue() != null) {
                viewModel.updateTotalWeeks();
            }
        });
        weekAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        weekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewBinding.spinnerWeek.setAdapter(weekAdapter);
        viewBinding.spinnerWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateTimetable();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        viewBinding.buttonPrev.setOnClickListener(view -> {
            int pos = viewBinding.spinnerWeek.getSelectedItemPosition();
            if (pos > 0) {
                viewBinding.spinnerWeek.setSelection(--pos);
                updateTimetable();
            }
        });
        viewBinding.buttonNext.setOnClickListener(view -> {
            int pos = viewBinding.spinnerWeek.getSelectedItemPosition();
            if (pos < weekAdapter.getCount() - 1) {
                viewBinding.spinnerWeek.setSelection(++pos);
                updateTimetable();
            }
        });
        viewBinding.timetable.setOnCourseClickListener(course -> {
            AndroidUtil.toast(getContext(), "您点击了 " + course.getName());
        });
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
            viewModel.updateTotalWeeks();
        });
        viewModel.getTotalWeeks().observe(getViewLifecycleOwner(), totalWeeks -> {
            if (totalWeeks == null) {
                return;
            }
            weekAdapter.clear();
            for (int i = 1; i <= totalWeeks; i++) {
                weekAdapter.add(i);
            }
            weekAdapter.notifyDataSetChanged();
            viewModel.updateCurrentWeek();
        });
        viewModel.getCurrentWeek().observe(getViewLifecycleOwner(), currentWeek -> {
            if (currentWeek == null) {
                return;
            }
            viewBinding.spinnerWeek.setSelection(currentWeek - 1);
            viewModel.updateLessonTimes();
        });
        viewModel.getLessonTimes().observe(getViewLifecycleOwner(), lessonTimes -> {
            if (lessonTimes == null) {
                return;
            }
            viewBinding.timetable.setTimeList(lessonTimes);
            updateTimetable();
        });
        viewModel.getTimetable().observe(getViewLifecycleOwner(), courses -> {
            if (courses == null) {
                return;
            }
            viewBinding.timetable.setCourseList(courses);
            viewBinding.timetable.refreshView();
            viewBinding.getRoot().setRefreshing(false);
        });
    }

    private void updateTimetable() {
        int week = viewBinding.spinnerWeek.getSelectedItemPosition() + 1;
        viewModel.updateTimetable(mainViewModel.getUserType().getValue(), week);
    }
}
