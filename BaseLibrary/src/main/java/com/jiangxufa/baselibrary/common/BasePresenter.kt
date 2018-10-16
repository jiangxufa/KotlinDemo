package com.jiangxufa.baselibrary.common

import com.trello.rxlifecycle2.LifecycleProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * 创建时间：2018/9/11
 * 编写人：lenovo
 * 功能描述：
 */
open class BasePresenter<T : BaseView> @Inject constructor(): BaseIPresenter<T> {
    override fun clearData() {
    }

    protected var mView: T? = null
        get() = mViewRef?.get()

    var mViewRef: WeakReference<T>?=null

    protected var mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    @Inject
    lateinit var lifecycleProvider: LifecycleProvider<*>

    private fun unSubscribe() {
        mCompositeDisposable.dispose()
    }

    /**
     * 删除
     *
     * @param disposable disposable
     */
    protected fun remove(disposable: Disposable): Boolean {
        return mCompositeDisposable.remove(disposable)
    }

    protected fun addSubscribe(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }


    override fun attachView(view: T) {
        this.mView = view
        mViewRef=WeakReference(view)
    }

    override fun detachView() {
        mViewRef?.clear()
        mViewRef = null
    }

}