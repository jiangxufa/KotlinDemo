package com.jiangxufa.baselibrary.injection.component

import android.content.Context
import com.jiangxufa.baselibrary.injection.module.AppModule
import dagger.Component
import javax.inject.Singleton

/**
 * 创建时间：2018/9/7
 * 编写人：lenovo
 * 功能描述：
 */
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent{
    fun context():Context
}