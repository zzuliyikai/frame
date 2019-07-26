package test.yikai.com.myapplication.base;

import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.VectorEnabledTintResources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import test.yikai.com.myapplication.BuildConfig;
import test.yikai.com.myapplication.R;
import test.yikai.com.myapplication.databinding.ActivityBaseBinding;
import test.yikai.com.myapplication.utils.ClassUtil;
import test.yikai.com.myapplication.utils.CommonUtils;
import test.yikai.com.myapplication.view.statusbar.StatusBarUtil;

public abstract class BaseActivity<VM extends AndroidViewModel, SV extends ViewDataBinding> extends AppCompatActivity {

    protected VM mViewModel;
    protected SV mBindingView;
    private View mErrorView;
    private View mLoadingView;
    private ActivityBaseBinding mBaseBinding;
    private AnimationDrawable mAnimationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mBaseBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_base, null, false);
        mBindingView = DataBindingUtil.inflate(getLayoutInflater(), layoutResID, null, false);

        //centent
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mBindingView.getRoot().setLayoutParams(layoutParams);
        RelativeLayout container = mBaseBinding.getRoot().findViewById(R.id.container);
        container.addView(mBindingView.getRoot());
        getWindow().setContentView(mBaseBinding.getRoot());

        //设置透明状态栏
        StatusBarUtil.setColor(this, CommonUtils.getColor(R.color.colorTheme), 0);
        mLoadingView = ((ViewStub) findViewById(R.id.vs_loading)).inflate();
        ImageView img = mLoadingView.findViewById(R.id.img_progress);

        //加载动画
        mAnimationDrawable = (AnimationDrawable) img.getDrawable();
        if (!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }

        setToolBar();
        mBindingView.getRoot().setVisibility(View.GONE);
        initViewModel();

    }

    protected void setToolBar() {
        setSupportActionBar(mBaseBinding.toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.icon_back);
        }

        mBaseBinding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    onBackPressed();
                }
            }
        });
    }

    protected void initViewModel() {
        Class<VM> viewModel = ClassUtil.getViewModel(this);
        if (viewModel != null) {
            mViewModel = ViewModelProviders.of(this).get(viewModel);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mBaseBinding.toolBar.setTitle(title);
    }

    protected void showLoading() {
        //显示加载页面
        if (mLoadingView != null && mLoadingView.getVisibility() != View.VISIBLE) {
            mLoadingView.setVisibility(View.VISIBLE);
        }

        //显示动画
        if (mAnimationDrawable != null && !mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }

        //隐藏内容页面
        if (mBindingView.getRoot().getVisibility() != View.GONE) {
            mBindingView.getRoot().setVisibility(View.GONE);
        }

        //隐藏错误页面
        if (mErrorView != null) {
            mBindingView.getRoot().setVisibility(View.GONE);
        }
    }

    protected void showConetentView() {
        //隐藏加载页面
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }

        //停止动画
        if (mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }

        //显示内容页面
        if (mBindingView.getRoot().getVisibility() != View.VISIBLE) {
            mBindingView.getRoot().setVisibility(View.VISIBLE);
        }
    }

    protected void showError() {
        //隐藏loding页面
        if (mLoadingView != null && mLoadingView.getVisibility() != View.GONE) {
            mLoadingView.setVisibility(View.GONE);
        }
        //停止动画
        if (mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        //隐藏conten页面
        if (mBindingView.getRoot().getVisibility() != View.GONE) {
            mBindingView.getRoot().setVisibility(View.VISIBLE);
        }

        //初始化错误页面
        if (mErrorView == null) {
            mErrorView = ((ViewStub) findViewById(R.id.vs_error_refresh)).inflate();
            mErrorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLoading();
                    onRefresh();
                }
            });
        }
    }

    protected void onRefresh() {


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.fontScale != 1) {
            getResources();
        }
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration configuration = new Configuration();
        configuration.setToDefaults();
        res.updateConfiguration(configuration,res.getDisplayMetrics());
        return res;
    }
}
