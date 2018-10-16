package com.jiangxufa.splashmodule

import com.blankj.utilcode.util.LogUtils
import com.jiangxufa.baselibrary.common.BasePresenter
import com.jiangxufa.baselibrary.ext.excute
import com.jiangxufa.baselibrary.ext.handlerResult
import com.jiangxufa.baselibrary.rx.SampleSubscriber
import com.jiangxufa.businesslibrary.model.AppConfig
import com.jiangxufa.splashmodule.data.net.CommonApi
import javax.inject.Inject

/**
 * 创建时间：2018/9/11
 * 编写人：lenovo
 * 功能描述：
 */
class SplashPresenter @Inject constructor() : BasePresenter<SplashContract.SplashIView>(), SplashContract.SplashIPresenter {
    @Inject
    lateinit var commonApi: CommonApi

    override fun getAppConfig() {
        commonApi.appConfig
                .handlerResult()
                .excute(object : SampleSubscriber<AppConfig>(mCompositeDisposable) {
                    override fun onSuccess(t: AppConfig) {
                        LogUtils.i(t.toString())
                    }
                }, lifecycleProvider)
    }



}