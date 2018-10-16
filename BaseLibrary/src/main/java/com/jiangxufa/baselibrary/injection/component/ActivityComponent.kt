package com.jiangxufa.baselibrary.injection.component

import android.app.Activity
import android.content.Context
import com.jiangxufa.baselibrary.injection.ActivityScope
import com.jiangxufa.baselibrary.injection.module.ActivityModule
import com.jiangxufa.baselibrary.injection.module.LifecycleProviderModule
import com.trello.rxlifecycle2.LifecycleProvider
import dagger.Component

/**
 * 创建时间：2018/9/7
 * 编写人：lenovo
 * 功能描述：
 */
@ActivityScope
@Component(dependencies = [AppComponent::class],
        modules = [ActivityModule::class, LifecycleProviderModule::class])
interface ActivityComponent{

    fun activity():Activity

    fun context():Context

    fun lifecycleProvider(): LifecycleProvider<*>
}