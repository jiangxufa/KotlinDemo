package com.jiangxufa.splashmodule

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.jiangxufa.baselibrary.common.BaseActivity
import com.jiangxufa.baselibrary.utils.StatusBarUtil
import com.jiangxufa.splashmodule.injection.component.DaggerSplashComponent
import com.jiangxufa.splashmodule.injection.module.SplashModule

class SplashActivity : BaseActivity<SplashPresenter>(),SplashContract.SplashIView {
    override fun initView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        // 隐藏状态栏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        super.onCreate(savedInstanceState)


    }

    override fun initStatusBar() {
        //设置透明
        StatusBarUtil.setTransparent(this)
    }

    override fun getLayoutId(): Int = R.layout.activity_splash

    override fun initInject() {
        DaggerSplashComponent.builder().activityComponent(activityComponent)
                .splashModule(SplashModule())
                .build().inject(this)
        mPresenter!!.attachView(this)
    }

    override fun onResume() {
        super.onResume()
        mPresenter!!.getAppConfig()
    }
}
