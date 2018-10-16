package com.jiangxufa.businesslibrary

import android.os.Build
import java.util.*

/**
 * 创建时间：2018/9/11
 * 编写人：lenovo
 * 功能描述：
 */
class CommonHelper {
    companion object {
        /**1 iOS 2 android 3 web 4 wxxcx**/
        const val TERMINAL_TYPE = 2
        const val AppType = 6
        fun getVersionCode():Int = BuildConfig.VERSION_CODE

        //获得独一无二的Psuedo ID
        fun getUniquePsuedoID(): String {
            var serial: String? = null

            val m_szDevIDShort = "35" +
                    Build.BOARD.length % 10 + Build.BRAND.length % 10 +
                    Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 +
                    Build.DISPLAY.length % 10 + Build.HOST.length % 10 +
                    Build.ID.length % 10 + Build.MANUFACTURER.length % 10 +
                    Build.MODEL.length % 10 + Build.PRODUCT.length % 10 +
                    Build.TAGS.length % 10 + Build.TYPE.length % 10 +
                    Build.USER.length % 10 //13 位
            try {
                serial = Build::class.java.getField("SERIAL").get(null).toString()
                //API>=9 使用serial号
                return UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
            } catch (exception: Exception) {
                //serial需要一个初始化
                serial = "serial" // 随便一个初始化
            }

            //使用硬件信息拼凑出来的15位号码
            return UUID(m_szDevIDShort.hashCode().toLong(), serial!!.hashCode().toLong()).toString()
        }

    }

}