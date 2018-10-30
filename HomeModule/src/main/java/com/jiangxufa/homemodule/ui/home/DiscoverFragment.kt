package com.jiangxufa.homemodule.ui.home

import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.ToastUtils
import com.jiangxufa.baselibrary.common.BaseFragment
import com.jiangxufa.baselibrary.common.BaseIPresenter
import com.jiangxufa.baselibrary.common.BasePresenter
import com.jiangxufa.baselibrary.common.BaseView
import com.jiangxufa.baselibrary.ext.excute
import com.jiangxufa.baselibrary.ext.onClick
import com.jiangxufa.baselibrary.rx.SampleSubscriber
import com.jiangxufa.baselibrary.utils.AppUtils
import com.jiangxufa.baselibrary.widgets.flowlayout.FlowLayout
import com.jiangxufa.baselibrary.widgets.flowlayout.TagAdapter
import com.jiangxufa.homemodule.R
import com.jiangxufa.homemodule.data.HomeRepository
import com.jiangxufa.homemodule.injection.component.DaggerHomeComponent
import com.jiangxufa.homemodule.injection.module.HomeModule
import com.jiangxufa.homemodule.model.HttpResponse
import com.jiangxufa.homemodule.model.discover.HotSearchTag
import kotlinx.android.synthetic.main.fragment_home_discover.*
import javax.inject.Inject

/**
 * 创建时间：2018/10/12
 * 编写人：lenovo
 * 功能描述：
 */
interface DiscoverContract {
    interface View : BaseView {
        fun showHotSearchTag(recommend: HotSearchTag)
    }

    interface Presenter : BaseIPresenter<View> {
        fun getHotSearchTagData()
    }
}

class DiscoverFragment : BaseFragment<DiscoverContract.View, DiscoverPresenter>(), DiscoverContract.View {

    companion object {
        fun instance(): Fragment = DiscoverFragment()
    }

    private var isShowMore = false
    lateinit var tagAdapter: DisconverTagAdapter

    override fun initInject() {
        DaggerHomeComponent.builder()
//                .fragmentComponent(fragmentComponent)
                .activityComponent(activityComponent)
                .homeModule(HomeModule())
                .build().inject(this)
        mPresenter?.attachView(this)
    }

    override fun getLayoutId(): Int = R.layout.fragment_home_discover

    override fun initView() {
        ll_more.onClick {
            if (isShowMore) {
                tv_more.text = "查看更多"
                val downDrawable = AppUtils.getDrawable(R.mipmap.ic_arrow_down_gray_round)
                downDrawable.setBounds(0, 0, downDrawable.minimumWidth, downDrawable.minimumHeight)
                tv_more.setCompoundDrawables(
                        downDrawable, null, null, null)
                tagAdapter.setMaxShowCount(10)
            } else {
                tv_more.text = "收起"
                val upDrawable = AppUtils.getDrawable(R.mipmap.ic_arrow_up_gray_round)
                upDrawable.setBounds(0, 0, upDrawable.minimumWidth, upDrawable.minimumHeight)
                tv_more.setCompoundDrawables(
                        upDrawable, null, null, null)
                tagAdapter.setMaxShowCount(0)
            }
            isShowMore = !isShowMore
        }
        tagAdapter = DisconverTagAdapter()
        tags_layout.adapter = tagAdapter
    }

    override fun initData() {

    }

    override fun lazyLoadData() {
        mPresenter?.getHotSearchTagData()
    }

    override fun showHotSearchTag(recommend: HotSearchTag) {
        tagAdapter.setNewData(recommend.list)
    }
}

class DiscoverPresenter @Inject constructor(): BasePresenter<DiscoverContract.View>(), DiscoverContract.Presenter {

    @Inject
    @JvmField
    var homeRepository: HomeRepository? = null

    override fun getHotSearchTagData() {
        homeRepository!!.appService.hotSearchTag
                .excute(object : SampleSubscriber<HttpResponse<HotSearchTag>>(mCompositeDisposable) {
                    override fun onSuccess(t: HttpResponse<HotSearchTag>) {
                        mView?.showHotSearchTag(t.data)
                    }

                }, lifecycleProvider, mView)
    }
}

class DisconverTagAdapter(list: List<HotSearchTag.ListBean> = arrayListOf()) : TagAdapter<HotSearchTag.ListBean>(list) {

    var mShowCount: Int = 0

    override fun getView(parent: FlowLayout, position: Int, t: HotSearchTag.ListBean): View {
        val mTags = LayoutInflater.from(parent.context).inflate(R.layout.layout_hot_tags_item, parent, false) as TextView
        mTags.text = t.keyword
        mTags.onClick { ToastUtils.showShort("点击") }
        return mTags
    }

    fun setMaxShowCount(count: Int) {
        this.mShowCount = count
        notifyDataChanged()
    }

    fun setNewData(list: List<HotSearchTag.ListBean>) {
        mTagDatas.clear()
        mTagDatas.addAll(list)
        setMaxShowCount(10)
        notifyDataChanged()
    }

    override fun getCount(): Int {
        if (mTagDatas.isEmpty()) {
            return 0
        }
        return if (mShowCount == 0) mTagDatas.size else mShowCount
    }
}