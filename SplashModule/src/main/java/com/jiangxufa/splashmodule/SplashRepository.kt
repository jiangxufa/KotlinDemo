package com.jiangxufa.splashmodule

import com.jiangxufa.baselibrary.data.net.ParamsInterceptor
import com.jiangxufa.baselibrary.data.net.RetrofitFactory
import com.jiangxufa.businesslibrary.CommonHelper
import com.jiangxufa.businesslibrary.UrlHelper
import com.jiangxufa.splashmodule.data.net.CommonApi
import okhttp3.Interceptor
import javax.inject.Inject

/**
 * 创建时间：2018/9/11
 * 编写人：lenovo
 * 功能描述：
 */
class SplashRepository @Inject constructor() {

    var map = HashMap<String, String>()

    private var paramsInterceptor:Interceptor

    init {
        map.put("versionCode", CommonHelper.getVersionCode().toString())
        map.put("terminalType", CommonHelper.TERMINAL_TYPE.toString())
        map.put("appType", CommonHelper.AppType.toString())
        map.put("userType", "2")
        map.put("uniqid", CommonHelper.getUniquePsuedoID())

        paramsInterceptor = ParamsInterceptor(map)
    }

    var commonApi: CommonApi = RetrofitFactory.instance.create(UrlHelper.getApiCommonUrl(),paramsInterceptor, service = CommonApi::class.java)

}