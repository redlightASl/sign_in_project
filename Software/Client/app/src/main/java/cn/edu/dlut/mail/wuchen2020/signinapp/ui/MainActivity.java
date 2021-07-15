package cn.edu.dlut.mail.wuchen2020.signinapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import cn.edu.dlut.mail.wuchen2020.signinapp.R;
import cn.edu.dlut.mail.wuchen2020.signinapp.SigninApplication;
import cn.edu.dlut.mail.wuchen2020.signinapp.databinding.ActivityMainBinding;
import cn.edu.dlut.mail.wuchen2020.signinapp.util.AndroidUtil;
import cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding viewBinding;
    private NavController navController;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(viewBinding.bottomNavigation, navController);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getUserType().observe(this, userType -> {
            if (userType == null) {
                return;
            }
            if (userType == -1) {
                AndroidUtil.toast(this, "登录过期, 请重新登录");
                startLoginActivity();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = SigninApplication.getSharedPreferences();
        if (preferences.contains("cookies")) {
            // 登录过了, 检查登录是否有效
            viewModel.updateUserType();
        } else {
            // 尚未登录
            startLoginActivity();
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
