package com.jiangxufa.businesslibrary

/**
 * 创建时间：2018/9/11
 * 编写人：lenovo
 * 功能描述：
 */
class UrlHelper{
    companion object {

        fun getHost():String{
            if (BuildConfig.DEBUG) {
                return "dev.ifeixiu.com"
            } else {
                return "www.ifeixiu.com"
            }
        }

        fun getApiBaseUrl():String = "https://" + getHost() + "/api/fr/v3/customer-provider/"

        fun getApiCommonUrl():String = "https://" + getHost() + "/api/fr/v3/common/"

        fun getAppUrl():String = "http://app.bilibili.com/"

        fun getLiveUrl():String = "http://api.live.bilibili.com/"
    }
}