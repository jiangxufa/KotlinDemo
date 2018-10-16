package com.jiangxufa.baselibrary.injection.module

import android.content.Context
import com.jiangxufa.baselibrary.common.BaseApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * 创建时间：2018/9/7
 * 编写人：lenovo
 * 功能描述：
 */
@Module
class AppModule(private val context:BaseApplication){

    @Provides
    @Singleton
    fun provideContext():Context = this.context
}