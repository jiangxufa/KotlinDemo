package com.jiangxufa.baselibrary.common

import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.jiangxufa.baselibrary.injection.component.ActivityComponent
import com.jiangxufa.baselibrary.injection.component.DaggerActivityComponent
import com.jiangxufa.baselibrary.injection.module.ActivityModule
import com.jiangxufa.baselibrary.injection.module.LifecycleProviderModule
import com.jiangxufa.baselibrary.widgets.ProgressLoading
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import javax.inject.Inject

/**
 * 创建时间：2018/9/11
 * 编写人：lenovo
 * 功能描述：
 */
public abstract class BaseActivity<T : BaseIPresenter<*>> :
        RxAppCompatActivity(), BaseView {

    @Inject
    @JvmField
    var mPresenter: T? = null

    private val activityModule: ActivityModule
        get() = ActivityModule(this)

    private val lifecycleProviderModule: LifecycleProviderModule
        get() = LifecycleProviderModule(this)

    protected val activityComponent: ActivityComponent
        get() = DaggerActivityComponent.builder()
                .appComponent((application as BaseApplication).appComponent)
                .activityModule(activityModule)
                .lifecycleProviderModule(lifecycleProviderModule).build()

    protected val mLoading:ProgressLoading
        get() = ProgressLoading.create(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initStatusBar()
        initToolBar()
        initInject()

        initView()
        initData()

        loadData()
    }


    abstract fun initView()

    open fun initData(){}

    /**
     * 初始化状态栏
     */
    open fun initStatusBar() {}

    open fun initToolBar() {
    }

    /**
     * 获取布局id
     */
    abstract fun getLayoutId(): Int

    /**
     * 依赖注入
     */
    abstract fun initInject()

    /**
     * 加载数据
     */
    abstract fun loadData()

    override fun showError(msg: String) {
        ToastUtils.showShort(msg)
    }

    override fun complete() {
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
        mLoading.cancel()
    }
}