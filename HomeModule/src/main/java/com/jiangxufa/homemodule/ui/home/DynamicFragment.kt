package com.jiangxufa.homemodule.ui.home

import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageView
import com.blankj.utilcode.util.TimeUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.jiangxufa.baselibrary.common.BaseIPresenter
import com.jiangxufa.baselibrary.common.BasePresenter
import com.jiangxufa.baselibrary.common.BaseRefreshFragment
import com.jiangxufa.baselibrary.common.BaseView
import com.jiangxufa.baselibrary.ext.excute
import com.jiangxufa.baselibrary.ext.onClick
import com.jiangxufa.baselibrary.rx.SampleSubscriber
import com.jiangxufa.baselibrary.utils.AppUtils
import com.jiangxufa.baselibrary.utils.EmptyUtils
import com.jiangxufa.baselibrary.widgets.CircleImageView
import com.jiangxufa.homemodule.R
import com.jiangxufa.homemodule.injection.component.DaggerHomeComponent
import com.jiangxufa.homemodule.injection.module.HomeModule
import com.jiangxufa.homemodule.model.dynamic.Dynamic
import com.jiangxufa.homemodule.model.dynamic.MulDynamic
import com.jiangxufa.homemodule.utils.FormatUtils
import com.jiangxufa.homemodule.utils.JsonUtils
import com.jiangxufa.homemodule.utils.NumberUtils
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_home_dynamic.*
import kotlinx.android.synthetic.main.home_refresh_recycler.*
import javax.inject.Inject

/**
 * 创建时间：2018/10/11
 * 编写人：lenovo
 * 功能描述：
 */
interface DynamicContract {
    interface View : BaseView {
        fun showMulDynamicData(dynamics: List<MulDynamic>)
    }

    interface Presenter : BaseIPresenter<View> {
        fun getMulDynamicData()
    }
}

class DynamicFragment : BaseRefreshFragment<DynamicContract.View, DynamicPresenter>(),
        DynamicContract.View {

    companion object {
        fun instance(): Fragment = DynamicFragment()
    }

    private var dynamicAdapter: DynamicAdapter? = null

    override fun getLayoutId(): Int = R.layout.fragment_home_dynamic

    override fun initInject() {
        @Suppress("DEPRECATION")
        DaggerHomeComponent.builder()
//                .fragmentComponent(fragmentComponent)
                .activityComponent(activityComponent)
                .homeModule(HomeModule())
                .build().inject(this)
        mPresenter?.attachView(this)
    }

    override fun initView() {
        super.initView()
        tv_all.onClick {
            if (expand.isExpanded) {
                expand.collapse()
            } else {
                expand.expand()
            }
        }
    }

    override fun initRecyclerView() {
        dynamicAdapter = DynamicAdapter(arrayListOf())
        with(recycler) {
            adapter = dynamicAdapter
            layoutManager = LinearLayoutManager(mContext)
        }
    }

    override fun lazyLoadData() {
        if (isFirstVisible) {
            recycler?.post {
                refresh!!.isRefreshing = true
                mPresenter?.getMulDynamicData()
            }
        }
    }

    override fun refreshLoadData() {
        mPresenter?.getMulDynamicData()
    }

    override fun initData() {
    }

    override fun showMulDynamicData(dynamics: List<MulDynamic>) {
        dynamicAdapter?.setNewData(dynamics)
    }

}

class DynamicPresenter @Inject constructor(): BasePresenter<DynamicContract.View>(), DynamicContract.Presenter {

    override fun getMulDynamicData() {
        Observable.just(JsonUtils.readJson("dynamic.json"))
                .map {
                    val gson = Gson()
                    val obj = JsonParser().parse(it).asJsonObject
                    val item = obj.getAsJsonObject("data").getAsJsonArray("item")
                    val itemBeans = ArrayList<Dynamic.ItemBean>()
                    for (jsonElement in item) {
                        itemBeans.add(gson.fromJson<Dynamic.ItemBean>(jsonElement, Dynamic.ItemBean::class.java))
                    }
                    return@map itemBeans
                }
                .map { it ->
                    val muDynamics = ArrayList<MulDynamic>()
                    it.forEach {
                        muDynamics.add(MulDynamic()
                                .setGroup(it)
                                .setItemType(MulDynamic.TYPE_LV0)
                                .setItemType(MulDynamic.TYPE_LV0)
                                .addSubItem(getRecent(it))
                                .setFlag(!getRecent(it).isEmpty()))
                    }
                    return@map muDynamics
                }
                .excute(object : SampleSubscriber<ArrayList<MulDynamic>>(mCompositeDisposable) {
                    override fun onSuccess(t: ArrayList<MulDynamic>) {
                        mView?.showMulDynamicData(t)
                    }
                }, lifecycleProvider, mView)
    }

    private fun getRecent(itemBean: Dynamic.ItemBean): List<MulDynamic> {
        val recent = itemBean.recent
        val list = ArrayList<MulDynamic>()
        if (EmptyUtils.isNotEmpty(recent)) {
            recent.forEach { recentBean ->
                list.add(MulDynamic()
                        .setItemType(MulDynamic.TYPE_LV1)
                        .setRecent(recentBean)
                        .setLevel(MulDynamic.TYPE_LV1))
            }
        }
        return list
    }
}

class DynamicAdapter(data: List<MulDynamic>) : BaseMultiItemQuickAdapter<MulDynamic, BaseViewHolder>(data) {

    init {
        addItemType(MulDynamic.TYPE_LV0, R.layout.item_home_dynamic)
        addItemType(MulDynamic.TYPE_LV1, R.layout.item_home_dynamic)
    }

    override fun convert(helper: BaseViewHolder, mulDynamic: MulDynamic) {
        when (helper.itemViewType) {
            MulDynamic.TYPE_LV0 -> {
                val itemBean = mulDynamic.group
                if (mulDynamic.flag) {
                    helper.setGone(R.id.fl_recent, true)
                    helper.setText(R.id.tv_recent, "还有" + itemBean.recent_count + "个视频被隐藏")
                } else {
                    helper.setGone(R.id.fl_recent, false)
                }

                when (itemBean.type) {
                    0//关注up
                    -> {
                        helper.setGone(R.id.iv_avatar, true)
                                .setGone(R.id.tv_tag, false)
                                .setGone(R.id.tv_title_time, true)
                                .setGone(R.id.tv_title_tag_time, false)
                                .setGone(R.id.tv_title, true)
                                .setText(R.id.tv_title_time,
                                        TimeUtils.getFriendlyTimeSpanByNow((itemBean.ctime * Math.pow(10.0, 3.0)).toLong()))
                                .setText(R.id.tv_title, itemBean.name)
                                .setText(R.id.tv_video_title, itemBean.title)
                                .setText(R.id.tv_duration, FormatUtils.formatDuration(itemBean.duration.toString() + ""))
                                .setGone(R.id.tv_duration, true)
                                .setGone(R.id.iv_video_play_num, true)
                                .setGone(R.id.tv_video_play_num, true)
                                .setGone(R.id.tv_video_favourite, true)
                                .setGone(R.id.iv_video_online_region, true)
                                .setText(R.id.tv_video_play_num, " " + NumberUtils.format(itemBean.play.toString() + ""))
                                .setText(R.id.tv_video_favourite, " " + NumberUtils.format(itemBean.favorite.toString() + ""))
                                .setGone(R.id.iv_tag_video_play_num, false)
                                .setGone(R.id.tv_tag_video_favourite, false)
                                .setGone(R.id.iv_tag_video_online_region, false)
                                .setText(R.id.tv_tag_video_play_num, itemBean.tname + if (itemBean.tag == null) "" else " · " + itemBean.tag.tag_name)
                        Glide.with(mContext)
                                .load<Any>(itemBean.face)
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.mipmap.bili_default_avatar)
                                .dontAnimate()
                                .into(helper.getView(R.id.iv_avatar) as CircleImageView)
                        Glide.with(mContext)
                                .load<Any>(itemBean.cover)
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.bili_default_image_tv)
                                .dontAnimate()
                                .into(helper.getView(R.id.iv_preview) as ImageView)
                    }
                    2//国产动画
                    -> {
                        helper.setGone(R.id.iv_avatar, false)
                                .setGone(R.id.tv_title, false)
                                .setGone(R.id.tv_title_time, false)
                                .setGone(R.id.tv_title_tag_time, true).setGone(R.id.tv_duration, false)
                                .setText(R.id.tv_title_tag_time, TimeUtils.getFriendlyTimeSpanByNow((itemBean.ctime * Math.pow(10.0, 3.0)).toLong()))
                                .setGone(R.id.tv_tag, true)
                                .setText(R.id.tv_tag, "国产动画")
                                .setBackgroundColor(R.id.tv_tag, AppUtils.getColor(R.color.yellow_30))
                                .setText(R.id.tv_video_title, itemBean.title)
                                .setGone(R.id.iv_video_play_num, false)
                                .setGone(R.id.tv_video_play_num, true)
                                .setGone(R.id.tv_video_favourite, false)
                                .setGone(R.id.iv_video_online_region, false)
                                .setText(R.id.tv_video_play_num, "第" + itemBean.index + "话" + " " + itemBean.index_title)
                                .setText(R.id.tv_tag_video_play_num, NumberUtils.format(itemBean.play.toString() + ""))
                                .setGone(R.id.iv_tag_video_play_num, true)
                                .setGone(R.id.tv_tag_video_play_num, true)
                                .setGone(R.id.iv_tag_video_online_region, true)
                                .setGone(R.id.tv_tag_video_favourite, true)
                                .setText(R.id.tv_tag_video_favourite, NumberUtils.format(itemBean.danmaku.toString() + ""))
                        Glide.with(mContext)
                                .load<Any>(itemBean.cover)
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.bili_default_image_tv)
                                .dontAnimate()
                                .into(helper.getView(R.id.iv_preview) as ImageView)
                    }

                    1//可能是番剧 不知道参数意思
                    -> {
                        helper.setGone(R.id.iv_avatar, false)
                                .setGone(R.id.tv_tag, true)
                                .setGone(R.id.tv_title, false)
                                .setGone(R.id.tv_title_time, false)
                                .setGone(R.id.tv_title_tag_time, true)
                                .setGone(R.id.tv_duration, false)
                                .setText(R.id.tv_title_tag_time, TimeUtils.getFriendlyTimeSpanByNow((itemBean.ctime * Math.pow(10.0, 3.0)).toLong()))
                                .setText(R.id.tv_tag, "番剧")
                                .setBackgroundColor(R.id.tv_tag, AppUtils.getColor(R.color.pink_text_color))
                                .setText(R.id.tv_video_title, itemBean.title)
                                .setGone(R.id.iv_video_play_num, false)
                                .setGone(R.id.tv_video_play_num, true)
                                .setGone(R.id.tv_video_favourite, false)
                                .setGone(R.id.iv_video_online_region, false)
                                .setText(R.id.tv_video_play_num, "第" + itemBean.index + "话" + " " + itemBean.index_title)
                                .setText(R.id.tv_tag_video_play_num, NumberUtils.format(itemBean.play.toString() + ""))
                                .setGone(R.id.iv_tag_video_play_num, true)
                                .setGone(R.id.tv_tag_video_play_num, true)
                                .setGone(R.id.iv_tag_video_online_region, true)
                                .setGone(R.id.tv_tag_video_favourite, true)
                                .setText(R.id.tv_tag_video_favourite, NumberUtils.format(itemBean.danmaku.toString() + ""))
                        Glide.with(mContext)
                                .load<Any>(itemBean.cover)
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.bili_default_image_tv)
                                .dontAnimate()
                                .into(helper.getView(R.id.iv_preview) as ImageView)
                    }
                }
            }
            MulDynamic.TYPE_LV1 -> {
                val recent = mulDynamic.recent
                helper.setGone(R.id.iv_avatar, true)
                        .setGone(R.id.tv_tag, false)
                        .setText(R.id.tv_title, recent.name)
                        .setText(R.id.tv_title_time, TimeUtils.getFriendlyTimeSpanByNow((recent.ctime * Math.pow(10.0, 3.0)).toLong()))
                        .setGone(R.id.fl_recent, false)
                        .setText(R.id.tv_video_title, recent.title)
                        .setText(R.id.tv_duration, FormatUtils.formatDuration(recent.duration.toString() + ""))
                        .setGone(R.id.tv_duration, true)
                        .setGone(R.id.iv_video_play_num, true)
                        .setGone(R.id.tv_video_play_num, true)
                        .setGone(R.id.tv_video_favourite, true)
                        .setGone(R.id.iv_video_online_region, true)
                        .setText(R.id.tv_video_play_num, " " + NumberUtils.format(recent.play.toString() + ""))
                        .setText(R.id.tv_video_favourite, " " + NumberUtils.format(recent.favorite.toString() + ""))
                        .setGone(R.id.iv_tag_video_play_num, false)
                        .setGone(R.id.tv_tag_video_favourite, false)
                        .setGone(R.id.iv_tag_video_online_region, false)
                        .setText(R.id.tv_tag_video_play_num, recent.tname + if (recent.tag == null) "" else " · " + recent.tag.tag_name)
                Glide.with(mContext)
                        .load<Any>(recent.face)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.bili_default_avatar)
                        .dontAnimate()
                        .into(helper.getView(R.id.iv_avatar) as CircleImageView)
                Glide.with(mContext)
                        .load<Any>(recent.cover)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.bili_default_image_tv)
                        .dontAnimate()
                        .into(helper.getView(R.id.iv_preview) as ImageView)
            }
        }
    }

}