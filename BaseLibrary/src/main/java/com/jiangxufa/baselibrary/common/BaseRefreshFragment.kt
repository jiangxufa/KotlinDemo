package com.jiangxufa.baselibrary.common

import android.support.v4.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ToastUtils
import com.jiangxufa.baselibrary.R
import com.jiangxufa.baselibrary.utils.AppUtils
import kotlinx.android.synthetic.main.common_refresh_recycler.*

/**
 * 创建时间：2018/10/8
 * 编写人：lenovo
 * 功能描述：
 */
abstract class BaseRefreshFragment<V : BaseView, T : BaseIPresenter<V>> : BaseFragment<V, T>(),
        SwipeRefreshLayout.OnRefreshListener {

    protected var mIsRefreshing = false

    override fun initView() {
        initRefreshLayout()
        initRecyclerView()
    }

    protected fun initRefreshLayout() {
        if (refresh != null) {
            refresh!!.setColorSchemeResources(R.color.colorPrimary)
            refresh!!.setOnRefreshListener(this)
        }
    }

    abstract fun initRecyclerView()

    override fun onRefresh() {
        clearData()
        refreshLoadData()
    }

    abstract fun refreshLoadData()

    protected open fun clearData() {
        mIsRefreshing = true
    }

    override fun complete() {
        super.complete()
        AppUtils.runOnUIDelayed(650) {
            if (refresh != null)
                refresh!!.isRefreshing = false
        }
        if (mIsRefreshing) {
            mPresenter?.clearData()
            clear()
            ToastUtils.showShort("刷新成功")
        }
        mIsRefreshing = false
    }

    protected open fun clear() {

    }
}