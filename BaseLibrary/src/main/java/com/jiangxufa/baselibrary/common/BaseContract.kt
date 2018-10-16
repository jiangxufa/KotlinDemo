package com.jiangxufa.baselibrary.common

/**
 * 创建时间：2018/9/11
 * 编写人：lenovo
 * 功能描述：
 */
interface BaseView {

    /**
     * 请求出错
     */
    fun showError(msg: String)

    /**
     * 请求完成
     */
    fun complete()
}

interface BaseIPresenter<T> {

    /**
     * 绑定
     *
     * @param view view
     */
    fun attachView(view: T)

    /**
     * 解绑
     */
    fun detachView()

    fun clearData()
}
