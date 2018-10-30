package com.jiangxufa.baselibrary.common

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.jiangxufa.baselibrary.R
import com.jiangxufa.baselibrary.utils.AppUtils

/**
 * 创建时间：2018/10/8
 * 编写人：lenovo
 * 功能描述：
 */

abstract class BaseRefreshActivity<T : BaseIPresenter<*>> : BaseActivity<T>(), SwipeRefreshLayout.OnRefreshListener {
    protected var mRecycler: RecyclerView? = null
    protected var mRefresh: SwipeRefreshLayout? = null
    protected var mIsRefreshing = false
//    protected var mList: MutableList<K>? = ArrayList()


    override fun initView() {
        initRefreshLayout()
        initRecyclerView()
    }

    protected fun initRefreshLayout() {
        if (mRefresh != null) {
            mRefresh!!.setColorSchemeResources(R.color.colorPrimary)
            mRecycler?.post {
                mRefresh!!.isRefreshing = true
                loadData()
            }
            mRefresh!!.setOnRefreshListener(this)
        }
    }

    abstract fun initRecyclerView()

    override fun onRefresh() {
        clearData()
        loadData()
    }

    protected open fun clearData() {
        mIsRefreshing = true
    }

    override fun complete() {
        super.complete()
        AppUtils.runOnUIDelayed(650) {
            if (mRefresh != null)
                mRefresh!!.isRefreshing = false
        }
        if (mIsRefreshing) {
            mPresenter?.clearData()
            clear()
            ToastUtils.showShort("刷新成功")
        }
        mIsRefreshing = false

        mLoading.hide()
    }

    protected open fun clear() {

    }

    override fun initData() {
        super.initData()
        if (mRefresh == null) {
            mLoading.show()
            AppUtils.runOnUIDelayed(650) { this.loadData() }
        }
    }
}