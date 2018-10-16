package com.jiangxufa.baselibrary.injection.module

import com.trello.rxlifecycle2.LifecycleProvider
import dagger.Module
import dagger.Provides

/**
 * 创建时间：2018/9/7
 * 编写人：lenovo
 * 功能描述：
 */
@Module
class LifecycleProviderModule(private var lifecycleProvider: LifecycleProvider<*>){

    @Provides
    fun privideLifecycleProvider() = this.lifecycleProvider;

}