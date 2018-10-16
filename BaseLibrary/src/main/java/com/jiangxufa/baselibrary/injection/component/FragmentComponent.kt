package com.jiangxufa.baselibrary.injection.component

import com.jiangxufa.baselibrary.injection.FragmentScope
import com.jiangxufa.baselibrary.injection.module.LifecycleProviderModule
import com.trello.rxlifecycle2.LifecycleProvider
import dagger.Component

/**
 * 创建时间：2018/10/13
 * 编写人：lenovo
 * 功能描述：
 */
@FragmentScope
@Component(dependencies = [AppComponent::class],
        modules = [LifecycleProviderModule::class])
interface FragmentComponent{
    fun lifecycleProvider(): LifecycleProvider<*>
}