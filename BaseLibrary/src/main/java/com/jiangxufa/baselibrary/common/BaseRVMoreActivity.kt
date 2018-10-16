package com.jiangxufa.baselibrary.common

import com.blankj.utilcode.util.NetworkUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView
import com.jiangxufa.baselibrary.utils.AppUtils

/**
 * 创建时间：2018/10/8
 * 编写人：lenovo
 * 功能描述：
 */
abstract class BaseRVMoreActivity<T : BaseIPresenter<*>, H : BaseQuickAdapter<*, *>> : BaseRefreshActivity<T>(),
    BaseQuickAdapter.RequestLoadMoreListener{

    protected var mAdapter: H? = null
    protected var mPage = 1
    protected val PS = 20
    protected var mTotal = 0
    protected var mIsError = false
    protected var mIsLoadMore = false

    override fun initRecyclerView() {
        mAdapter?.setLoadMoreView(SimpleLoadMoreView())
    }

    override fun onLoadMoreRequested() {
        if (NetworkUtils.isConnected()) {
            AppUtils.runOnUIDelayed(650) {
                if (mAdapter?.data!!.size >= mTotal) {
                    mAdapter?.loadMoreEnd() // 结束加载
                } else {
                    if (!mIsError) {
                        mPage++
                        loadData()
                    } else {
                        mIsError = true
                        mAdapter?.loadMoreFail()//加载失败
                    }
                }
            }
        } else {
            mIsError = true
            mAdapter?.loadMoreFail()//加载失败
        }
    }

    override fun clearData() {
        super.clearData()
        mPage = 1
        mIsLoadMore = false
        mIsError = false
        //刷新时候关闭上拉加载
        mAdapter?.setEnableLoadMore(false)
    }

    override fun showError(msg: String) {
        super.showError(msg)
        mIsError = true
    }

    override fun complete() {
        super.complete()
        //需要重新开启监听
        mAdapter?.setEnableLoadMore(true)
    }
}