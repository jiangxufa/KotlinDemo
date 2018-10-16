package com.jiangxufa.baselibrary.common

/**
 * 创建时间：2018/9/30
 * 编写人：lenovo
 * 功能描述：
 */

//interface BaseRefreshContract {
//    interface BaseRefreshIView : BaseView {
//        fun updateData(datas: List<String>)
//        fun loadMoreEnd(isEnd: Boolean)
//        fun loadMoreComplete()
//        fun enableLoadMore(enable: Boolean)
//    }
//
//    interface BaseRefreshIPresenter<T : BaseRefreshIView> : BaseIPresenter<T> {
//        fun getData(isRefresh: Boolean)
//    }
//}
//
//open class BaseRefreshFragment<V : BaseRefreshContract.BaseRefreshIView, P : BaseRefreshContract.BaseRefreshIPresenter<V>>
//    : BaseFragment<V, P>(), BaseRefreshContract.BaseRefreshIView,
//        SwipeRefreshLayout.OnRefreshListener,
//        BaseQuickAdapter.RequestLoadMoreListener {
//
//    private lateinit var mAdapter: RLAdapter
//    private var mIsRefreshing = false
//    private var mIsError = false
//    private var mIsLoadMore = false
//
//    /**
//     * 是否显示底部的加载View
//     */
//    override fun loadMoreEnd(isEnd: Boolean) {
//        mIsLoadMore = false
//        mAdapter.loadMoreEnd(isEnd)
//    }
//
//    /**
//     * 取消加载更多的显示view
//     */
//    override fun loadMoreComplete() {
//        mIsLoadMore = false
//        mAdapter.loadMoreComplete()
//    }
//
//    /**
//     * 是否能够使用加载更多
//     */
//    override fun enableLoadMore(enable: Boolean) {
//        mAdapter.setEnableLoadMore(enable)
//    }
//
//    override fun onRefresh() {
//
//        mIsRefreshing = true
//        //禁止加载更多
//        mAdapter.setEnableLoadMore(false)
//
//        mPresenter?.getData(true)
//    }
//
//    override fun onLoadMoreRequested() {
//        mIsLoadMore = true
//        if (NetworkUtils.isConnected()) {
//            AppUtils.runOnUIDelayed(650) {
//                mPresenter?.getData(false)
//            }
//        } else {
//            mIsError = true
//            mAdapter.loadMoreFail()//加载失败
//        }
//    }
//
//    override fun complete() {
//        if (mIsRefreshing) {
//            AppUtils.runOnUIDelayed(650) {
//                refresh?.isRefreshing = false
//            }
//            if (mIsRefreshing) {
//                ToastUtils.showLong("刷新成功")
//            }
//            mIsRefreshing = false
//            mAdapter.setEnableLoadMore(true)
//        }
//
//        if (mIsLoadMore) {
//            mAdapter.loadMoreComplete()
//            mIsLoadMore = false
//        }
//
//    }
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
//            layoutManager = LinearLayoutManager(mContext)
//            adapter = mAdapter
//        }
//    }
//
//    override fun initData() {
//
//    }
//
//    override fun lazyLoadData() {
//        refresh.post {
//            refresh!!.isRefreshing = true
//            onRefresh()
//        }
//    }
//
//    override fun updateData(datas: List<String>) {
//        if (mAdapter.data == datas) {
//            mAdapter.notifyDataSetChanged()
//        } else {
//            mAdapter.setNewData(datas)
//        }
//    }
//}
//
//open class BaseRefreshPresenter<V : BaseRefreshContract.BaseRefreshIView> : BasePresenter<V>(), BaseRefreshContract.BaseRefreshIPresenter<V> {
//
//    private var lists = ArrayList<String>()
//
//    override fun getData(isRefresh: Boolean) {
//        if (isRefresh) {
//            lists.clear()
//        }
//        val datas = ArrayList<String>()
//        for (i in 1..15) {
//            datas.add("这是数字呀呀呀$i")
//        }
//        isListComplete(datas, 59, isRefresh)
//    }
//
//
//    private fun isListComplete(datas: ArrayList<String>, total: Int, isRefresh: Boolean) {
//        lists.addAll(datas)
//        if (lists.size >= total) {
//            mView?.updateData(lists)
//            if (isRefresh) {
//                mView?.complete()
//            }
//            mView?.loadMoreEnd(false)
//        } else {
//            mView?.updateData(lists)
//            mView?.complete()
//        }
//    }
//}
