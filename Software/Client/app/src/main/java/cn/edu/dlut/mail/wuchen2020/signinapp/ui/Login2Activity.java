package cn.edu.dlut.mail.wuchen2020.signinapp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import cn.edu.dlut.mail.wuchen2020.signinapp.SigninApplication;
import cn.edu.dlut.mail.wuchen2020.signinapp.databinding.ActivityLogin2Binding;
import cn.edu.dlut.mail.wuchen2020.signinapp.viewmodel.LoginViewModel;

public class Login2Activity extends AppCompatActivity {
    private ActivityLogin2Binding viewBinding;
    private LoginViewModel viewModel;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityLogin2Binding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult == null) {
                return;
            }
            progressDialog.dismiss();
            progressDialog = null;
            if (loginResult == 1) {
                showMessage("请输入学号/工号");
                return;
            } else if (loginResult == 2) {
                showMessage("请输入密码");
                return;
            } else if (loginResult == 3) {
                showMessage("账号或密码错误");
                SigninApplication.clearCookies();
                return;
            } else if (loginResult == 4) {
                showMessage("尝试次数过多, 请稍后再试");
                SigninApplication.clearCookies();
                return;
            } else if (loginResult == 5) {
                showMessage("您已登录");
            } else if (loginResult == 6) {
                showMessage("客户端目前尚不支持管理员使用");
                SigninApplication.clearCookies();
                return;
            } else if (loginResult != 0) {
                showMessage("登录失败, 请检查网络连接");
                SigninApplication.clearCookies();
                return;
            }
            startMainActivity();
        });
        viewBinding.buttonLogin.setOnClickListener(v -> {
            progressDialog = ProgressDialog.show(this, "", "正在登录中...", true);
            viewModel.username = viewBinding.textUsername.getText().toString().trim();
            viewModel.password = viewBinding.textPassword.getText().toString().trim();
            viewModel.login();
        });
    }

    private void showMessage(String message) {
        Snackbar.make(viewBinding.getRoot(), message, Snackbar.LENGTH_LONG)
                .show();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
