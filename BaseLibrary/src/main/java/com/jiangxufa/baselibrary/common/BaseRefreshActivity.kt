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


//abstract class BaseRefreshActivity<T : BaseIPresenter<*>, K> : BaseActivity<T>(),
//        SwipeRefreshLayout.OnRefreshListener,
//        BaseQuickAdapter.RequestLoadMoreListener {
//
//    protected var mRecycler: RecyclerView? = null
//    protected var mRefresh: SwipeRefreshLayout? = null
//    protected var mList: MutableList<K>? = ArrayList()
//    private lateinit var mAdapter: RLAdapter
//    private var mIsRefreshing = false
//    private var mIsError = false
//    private var mIsLoadMore = false
//    protected var mTotal = 0
//    protected var mPage = 1
//
//    override fun onRefresh() {
//        claerData()
//        loadData()
//    }
//
//    fun claerData() {
//        mPage = 1
//        mIsLoadMore = false
//        mIsError = false
//        //刷新时候关闭上拉加载
//        mAdapter.setEnableLoadMore(false)
//    }
//
//    override fun onLoadMoreRequested() {
//        if (NetworkUtils.isConnected()) {
//            AppUtils.runOnUIDelayed(650) {
//                if (mAdapter.itemCount >= mTotal) {
//                    mAdapter.loadMoreEnd() //结束加载
//                } else {
//                    if (!mIsError) {
//                        mPage++
//                        loadData()
//                    } else {
//                        mIsError = true
//                        mAdapter.loadMoreFail()//加载失败
//                    }
//                }
//            }
//        } else {
//            mIsError = true
//            mAdapter.loadMoreFail()//加载失败
//        }
//    }
//
//
//    override fun getLayoutId(): Int = R.layout.common_refresh_recycler
//
//    override fun initView() {
//        initRefreshLayout()
//        initRecyclerView(recycler)
//    }
//
//    private fun initRefreshLayout() {
//        if (refresh != null) {
//            refresh!!.setColorSchemeResources(R.color.colorPrimary)
//            refresh!!.setOnRefreshListener(this)
//        }
//    }
//
//    protected open fun initRecyclerView(recycler: RecyclerView) {
//        mAdapter = RLAdapter()
//        mAdapter.setLoadMoreView(SimpleLoadMoreView())
//        mAdapter.setOnLoadMoreListener(this, recycler)
//        with(recycler) {
//            layoutManager = LinearLayoutManager(this@BaseRefreshActivity)
//            adapter = mAdapter
//        }
//    }
//
//    override fun showError(msg: String) {
//        super.showError(msg)
//        mIsError = true
//    }
//
//    override fun complete() {
//        super.complete()
//        //需要重新开启监听
//        mAdapter.setEnableLoadMore(true)
//    }
//}