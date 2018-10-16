package com.jiangxufa.homemodule.injection.module

import com.jiangxufa.homemodule.data.HomeRepository
import com.jiangxufa.homemodule.data.protocol.HomeService
import dagger.Module
import dagger.Provides

/**
 * 创建时间：2018/9/30
 * 编写人：lenovo
 * 功能描述：
 */
@Module
class HomeModule {

    @Provides
    fun provideHomeApi(homeRepository: HomeRepository): HomeService = homeRepository.homeService


}