package com.jiangxufa.splashmodule

import com.jiangxufa.baselibrary.common.BaseIPresenter
import com.jiangxufa.baselibrary.common.BaseView


/**
 * 创建时间：2018/9/11
 * 编写人：lenovo
 * 功能描述：
 */
interface SplashContract{
    interface SplashIView: BaseView {

    }

    interface SplashIPresenter: BaseIPresenter<SplashIView> {

        fun getAppConfig()

    }
}