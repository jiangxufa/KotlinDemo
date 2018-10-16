package com.jiangxufa.homemodule

import android.support.v4.app.Fragment
import com.jiangxufa.baselibrary.common.BaseFragment
import com.jiangxufa.baselibrary.common.BasePresenter
import com.jiangxufa.baselibrary.common.BaseView

/**
 * 创建时间：2018/9/29
 * 编写人：lenovo
 * 功能描述：
 */
class LiveFragment:BaseFragment<BaseView, BasePresenter<BaseView>>(){

    companion object {
        fun instance(): Fragment = LiveFragment()
    }

    override fun initView() {
    }

    override fun initData() {
    }

    override fun getLayoutId(): Int = R.layout.fragment_live

}