package test.yikai.com.myapplication.ui;

import android.os.Bundle;

import test.yikai.com.myapplication.R;
import test.yikai.com.myapplication.base.BaseActivity;
import test.yikai.com.myapplication.databinding.ActivityBaseBinding;
import test.yikai.com.myapplication.viewmodel.LoginViewModel;

public class LoginActivity extends BaseActivity<LoginViewModel, ActivityBaseBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("登录");
        showConetentView();




    }
}
