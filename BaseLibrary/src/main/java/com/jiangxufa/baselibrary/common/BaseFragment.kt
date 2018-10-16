package com.jiangxufa.baselibrary.common

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jiangxufa.baselibrary.injection.component.DaggerFragmentComponent
import com.jiangxufa.baselibrary.injection.component.FragmentComponent
import com.jiangxufa.baselibrary.injection.module.LifecycleProviderModule
import com.trello.rxlifecycle2.components.support.RxFragment
import javax.inject.Inject

/**
 * 创建时间：2018/9/11
 * 编写人：lenovo
 * 功能描述：
 */
abstract class BaseFragment<V : BaseView, T : BaseIPresenter<V>> : RxFragment(), BaseView {

    @Inject
    @JvmField
    var mPresenter: T? = null

    protected var mRootView: View? = null
    protected lateinit var inflater: LayoutInflater

    protected lateinit var mActivity: Activity
    protected lateinit var mContext: Context

    // 标志位 标志已经初始化完成。
    protected var isPrepared: Boolean = false
    //标志位 fragment是否可见
    protected var isFragmentVisible: Boolean = false

    protected var isFirstVisible = true

    private val lifecycleProviderModule: LifecycleProviderModule
        get() = LifecycleProviderModule(this)

    protected val fragmentComponent:FragmentComponent
        get() = DaggerFragmentComponent.builder()
                .appComponent((mActivity.application as BaseApplication).appComponent)
                .lifecycleProviderModule(lifecycleProviderModule).build()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = context as Activity
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mRootView != null) {
            val parent = mRootView!!.parent as ViewGroup?
            parent?.removeView(mRootView)
        } else {
            mRootView = inflater.inflate(getLayoutId(), container, false)
            this.inflater = inflater
        }
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInject()
        initPresenter()
        initView()
        finishCreateView(savedInstanceState)
        initData()
    }

    open fun initInject() {
    }

    open fun initPresenter() {
    }

    abstract fun initView()

    private fun finishCreateView(savedInstanceState: Bundle?) {
        isPrepared = true
        if (isFirstVisible && isPrepared && isFragmentVisible) {
            lazyLoadData()
            isFirstVisible = false
        }
    }

    private fun lazyLoad() {
        if (!isPrepared || !isVisible) return
        lazyLoadData()
        isPrepared = false
    }

    open fun lazyLoadData() {
    }

    abstract fun initData()

    abstract fun getLayoutId(): Int

    override fun showError(msg: String) {
    }

    override fun complete() {
    }


    /**
     * Fragment数据的懒加载
     *
     * @param isVisibleToUser
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            isFragmentVisible = true
            onVisible()
        } else {
            isFragmentVisible = false
            onInvisible()
        }
    }

    private fun onInvisible() {
    }

    private fun onVisible() {
        if (isPrepared && isAdded) {
            if (isFirstVisible) {
                lazyLoadData()
                isFirstVisible = false
            }
            resumeLoad()
        }
    }

    private fun resumeLoad() {
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
    }
}