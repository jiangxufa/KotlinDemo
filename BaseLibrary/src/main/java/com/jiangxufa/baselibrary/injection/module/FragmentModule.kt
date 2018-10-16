package com.jiangxufa.baselibrary.injection.module

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.jiangxufa.baselibrary.injection.FragmentScope
import dagger.Module
import dagger.Provides

/**
 * 创建时间：2018/10/13
 * 编写人：lenovo
 * 功能描述：
 */
@Module
class FragmentModule(private val fragment: Fragment){

    @Provides
    @FragmentScope
    fun providerActivity(): FragmentActivity? = fragment.activity

}