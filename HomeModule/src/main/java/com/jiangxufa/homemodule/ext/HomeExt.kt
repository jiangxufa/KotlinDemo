package com.jiangxufa.homemodule.ext

import com.blankj.utilcode.util.ToastUtils
import com.jiangxufa.baselibrary.common.BaseView
import com.jiangxufa.baselibrary.data.net.RetrofitException
import com.jiangxufa.baselibrary.data.protocol.ApiException
import com.jiangxufa.homemodule.model.HttpResponse
import com.jiangxufa.homemodule.utils.RxUtils
import com.trello.rxlifecycle2.LifecycleProvider
import io.reactivex.Flowable
import io.reactivex.FlowableSubscriber
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 创建时间：2018/10/13
 * 编写人：lenovo
 * 功能描述：
 */
fun <T> Observable<HttpResponse<T>>.handlerResult(): Observable<T> {
    return this.compose(RxUtils.handleResult<T>())
}

fun <T> Flowable<T>.excute(subscriber: SampleFlowableSubscriber<T>, lifecycleProvider: LifecycleProvider<*>, mView: BaseView?) {
    this.subscribeOn(Schedulers.io())
            .doOnSubscribe {
                subscriber.setView(mView)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .compose(lifecycleProvider.bindToLifecycle())
            .subscribe(subscriber)
}


abstract class SampleFlowableSubscriber<T> : FlowableSubscriber<T> {

    private var mView: BaseView? = null

    fun setView(v: BaseView?) {
        this.mView = v
    }

    override fun onError(t: Throwable) {
        mView?.complete()
        try {
            when (t) {
                is ApiException -> ToastUtils.showShort(t.message)
                else -> {
                    ToastUtils.showShort(RetrofitException.retrofitException(t).tip)
                }
            }
        } catch (e: Exception) {
            ToastUtils.showShort(t.toString())
        }
    }

    override fun onNext(t: T) {
        mView?.complete()
        onSuccess(t)
    }

    abstract fun onSuccess(t: T)


    override fun onComplete() {

    }
}
