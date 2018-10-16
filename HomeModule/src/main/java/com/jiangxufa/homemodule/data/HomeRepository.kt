package com.jiangxufa.homemodule.data

import com.jiangxufa.baselibrary.data.net.RetrofitFactory
import com.jiangxufa.businesslibrary.UrlHelper
import com.jiangxufa.homemodule.data.protocol.AppService
import com.jiangxufa.homemodule.data.protocol.HomeService
import com.jiangxufa.homemodule.data.protocol.LiveService
import javax.inject.Inject

/**
 * 创建时间：2018/9/30
 * 编写人：lenovo
 * 功能描述：
 */
class HomeRepository @Inject constructor(){

    var homeService: HomeService = RetrofitFactory.instance.create(UrlHelper.getApiCommonUrl(), service = HomeService::class.java)
    var appService: AppService = RetrofitFactory.instance.create(UrlHelper.getAppUrl(), service = AppService::class.java)
    var liveService: LiveService = RetrofitFactory.instance.create(UrlHelper.getLiveUrl(), service = LiveService::class.java)
}