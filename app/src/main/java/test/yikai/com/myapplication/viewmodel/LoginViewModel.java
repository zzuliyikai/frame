package test.yikai.com.myapplication.viewmodel;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import test.yikai.com.myapplication.base.BaseViewModel;

public class LoginViewModel extends BaseViewModel {

    public final ObservableField<String> username = new ObservableField<>();
    public final ObservableField<String> password = new ObservableField<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }
}
