package com.jiangxufa.splashmodule.injection.module

import com.jiangxufa.splashmodule.SplashRepository
import com.jiangxufa.splashmodule.data.net.CommonApi
import dagger.Module
import dagger.Provides

/**
 * 创建时间：2018/9/11
 * 编写人：lenovo
 * 功能描述：
 */
@Module
class SplashModule{

    @Provides
    fun provideCommonApi(splashRepository: SplashRepository):CommonApi = splashRepository.commonApi

}