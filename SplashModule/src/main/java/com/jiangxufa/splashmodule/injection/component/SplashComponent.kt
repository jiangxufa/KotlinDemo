package com.jiangxufa.splashmodule.injection.component

import com.jiangxufa.baselibrary.injection.PerComponentScope
import com.jiangxufa.baselibrary.injection.component.ActivityComponent
import com.jiangxufa.splashmodule.SplashActivity
import com.jiangxufa.splashmodule.injection.module.SplashModule
import dagger.Component

/**
 * 创建时间：2018/9/11
 * 编写人：lenovo
 * 功能描述：
 */
@PerComponentScope
@Component(dependencies = [ActivityComponent::class],
        modules = [SplashModule::class])
interface SplashComponent {

    fun inject(activity: SplashActivity)
}