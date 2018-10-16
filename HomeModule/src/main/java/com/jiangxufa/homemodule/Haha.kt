package com.jiangxufa.homemodule

import com.jiangxufa.baselibrary.data.net.util.RxNetUtil
import io.reactivex.Observable

/**
 * 创建时间：2018/10/9
 * 编写人：lenovo
 * 功能描述：
 */
class Haha {

    internal fun getData() {
        Observable.just("ssdsfsdf")
                .map { s -> s }.compose(RxNetUtil.rxSchedulerHelper())
                .subscribe()
    }

}
