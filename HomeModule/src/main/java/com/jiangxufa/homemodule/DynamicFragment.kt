package com.jiangxufa.homemodule

/**
 * 创建时间：2018/9/30
 * 编写人：lenovo
 * 功能描述：
 */
//interface DynamicContract {
//    interface View : BaseView {
//        fun showMulDynamic(t: ArrayList<String>)
//    }
//
//    interface Presenter : BaseIPresenter<View> {
//        fun getMulDynamicData()
//    }
//}
//
//class DynamicPresenter @Inject constructor() : BasePresenter<DynamicContract.View>(), DynamicContract.Presenter {
//    override fun getMulDynamicData() {
//        var list = ArrayList<String>()
//        for (i in 1..20) {
//            list.add("这是第几个$i")
//        }
//        Observable.just(list)
//                .excute(object : SampleSubscriber<ArrayList<String>>(mCompositeDisposable) {
//                    override fun onSuccess(t: ArrayList<String>) {
//                        mView?.showMulDynamic(t)
//                    }
//                }, lifecycleProvider, mView = mView)
//    }
//}
//
//class DynamicFragment : BaseRefreshFragment<DynamicContract.View, DynamicPresenter>(), DynamicContract.View {
//
//    override fun initData() {
//    }
//
//    companion object {
//        fun instance(): Fragment = DynamicFragment()
//    }
//
//    lateinit var rlAdapter: RLAdapter
//
//    override fun getLayoutId(): Int = R.layout.home_refresh_recycler
//
//    override fun initInject() {
//        super.initInject()
//        DaggerHomeComponent.builder()
//                .activityComponent(activityComponent)
//                .homeModule(HomeModule())
//                .build().inject(this)
//        mPresenter?.attachView(this)
//    }
//
//    override fun initRecyclerView() {
//        rlAdapter = RLAdapter()
//
//        with(recycler) {
//            layoutManager = LinearLayoutManager(mContext)
//            adapter = rlAdapter
//        }
//    }
//
//    override fun lazyLoadData() {
//        if (isFirstVisible) {
//            recycler?.post {
//                refresh!!.isRefreshing = true
//                mPresenter?.getMulDynamicData()
//            }
//        }
//    }
//
//    override fun refreshLoadData() {
//        mPresenter?.getMulDynamicData()
//    }
//
//    override fun showMulDynamic(t: ArrayList<String>) {
//        rlAdapter.setNewData(t)
//    }
//
//
//}