package com.jiangxufa.homemodule.injection.component

import com.jiangxufa.baselibrary.injection.PerComponentScope
import com.jiangxufa.baselibrary.injection.component.FragmentComponent
import com.jiangxufa.homemodule.LiveFragment
import com.jiangxufa.homemodule.injection.module.HomeModule
import com.jiangxufa.homemodule.ui.home.*
import dagger.Component

/**
 * 创建时间：2018/9/30
 * 编写人：lenovo
 * 功能描述：
 */
@PerComponentScope
@Component(dependencies = [FragmentComponent::class], modules = [HomeModule::class])
interface HomeComponent {

    fun inject(fragment: LiveDataFragment)
    fun inject(fragment: LiveFragment)
    fun inject(fragment: RecommendFragment)
    fun inject(fragment: ChaseBangumiFragment)
    fun inject(fragment: RegionFragment)
    fun inject(fragment: DynamicFragment)
    fun inject(fragment: DiscoverFragment)
}