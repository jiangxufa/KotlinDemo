package com.jiangxufa.homemodule.ui.home

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.widget.ImageView
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
import com.jiangxufa.baselibrary.rx.SampleSubscriber
import com.jiangxufa.baselibrary.utils.AppUtils
import com.jiangxufa.baselibrary.widgets.divider.VerticalDividerItemDecoration
import com.jiangxufa.homemodule.R
import com.jiangxufa.homemodule.injection.component.DaggerHomeComponent
import com.jiangxufa.homemodule.injection.module.HomeModule
import com.jiangxufa.homemodule.model.MulRecommend
import com.jiangxufa.homemodule.model.Recommend
import com.jiangxufa.homemodule.utils.FormatUtils
import com.jiangxufa.homemodule.utils.JsonUtils
import com.jiangxufa.homemodule.utils.NumberUtils
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.loader.ImageLoader
import io.reactivex.Observable
import kotlinx.android.synthetic.main.home_refresh_recycler.*
import javax.inject.Inject

/**
 * 创建时间：2018/10/9
 * 编写人：lenovo
 * 功能描述：
 */
interface RecommendContract {
    interface View : BaseView {
        fun showRecommends(datas: List<Recommend>)
    }

    interface Presenter : BaseIPresenter<View> {
        fun getRecommends()
    }
}

class RecommendFragment : BaseRefreshFragment<RecommendContract.View, RecommendPresenter>()
        , RecommendContract.View {

    companion object {
        fun instance(): Fragment = RecommendFragment()
    }

    private lateinit var recommendAdapter: RecommendAdapter
    private var mList: MutableList<MulRecommend> = ArrayList()

    override fun getLayoutId(): Int = R.layout.fragment_recommend

    override fun initInject() {
        DaggerHomeComponent.builder()
                .fragmentComponent(fragmentComponent)
                .homeModule(HomeModule())
                .build().inject(this)
        mPresenter?.attachView(this)
    }

    override fun initRecyclerView() {
        recommendAdapter = RecommendAdapter(mList)
        with(recycler) {
            recommendAdapter.setSpanSizeLookup { gridLayoutManager, i -> recommendAdapter.data.get(i).spanSize }
            layoutManager = GridLayoutManager(mContext, 2)
            adapter = recommendAdapter
            addItemDecoration(VerticalDividerItemDecoration.Builder(mContext)
                    .color(AppUtils.getColor(R.color.transparent))
                    .sizeResId(R.dimen.dp_10)
                    .showLastDivider()
                    .build())
        }
    }

    override fun refreshLoadData() {
        mPresenter?.getRecommends()
    }

    override fun initData() {

    }

    override fun clearData() {
        super.clearData()
        mList.clear()
    }

    override fun lazyLoadData() {
        if (isFirstVisible) {
            recycler?.post {
                refresh!!.isRefreshing = true
                mPresenter?.getRecommends()
            }
        }
    }

    override fun showRecommends(datas: List<Recommend>) {
        datas.forEach {
            if (it.banner_item != null) {
                mList.add(MulRecommend(MulRecommend.TYPR_HEADER, MulRecommend.HEADER_SPAN_SIZE, it.banner_item))
            } else {
                mList.add(MulRecommend(MulRecommend.TYPE_ITEM, MulRecommend.ITEM_SPAN_SIZE, it))
            }
        }
        recommendAdapter.setNewData(mList)
    }
}

class RecommendPresenter @Inject constructor() : BasePresenter<RecommendContract.View>(), RecommendContract.Presenter {

    override fun getRecommends() {
        Observable.just(JsonUtils.readJson("recommend.json"))
                .map {
                    val gson = Gson()
                    val obj = JsonParser().parse(it).getAsJsonObject()
                    val array = obj.getAsJsonArray("data")
                    val recommends = ArrayList<Recommend>()
                    for (i in array) {
                        recommends.add(gson.fromJson<Any>(i, Recommend::class.java) as Recommend)
                    }
                    return@map recommends
                }
                .excute(object : SampleSubscriber<ArrayList<Recommend>>(mCompositeDisposable) {
                    override fun onSuccess(t: ArrayList<Recommend>) {
                        mView?.showRecommends(t)
                    }
                }, lifecycleProvider, mView)
    }

}

class RecommendAdapter(data: List<MulRecommend>) : BaseMultiItemQuickAdapter<MulRecommend, BaseViewHolder>(data) {

    init {
        addItemType(MulRecommend.TYPR_HEADER, R.layout.layout_recommend_banner)
        addItemType(MulRecommend.TYPE_ITEM, R.layout.layout_item_home_recommend_body)
    }

    override fun convert(helper: BaseViewHolder, item: MulRecommend) {
        when (helper.itemViewType) {
            MulRecommend.TYPR_HEADER -> {
                val bannerItem = item.mBannerItemBean
                val urls = bannerItem.map { bannerBean -> bannerBean.image }.toList()
                val banner = helper.getView<Banner>(R.id.banner)
                banner.setIndicatorGravity(BannerConfig.RIGHT)
                        .setImages(urls)
                        .setImageLoader(GlidImageLoader())
                        .start()
                banner.setOnBannerListener { i ->
                    val bannerBean = bannerItem[i]
//                    BrowerActivity.startActivity(mContext, bannerBean.uri, bannerBean.title, bannerBean.image)
                }
            }
            MulRecommend.TYPE_ITEM -> {
                Glide.with(mContext)
                        .load<Any>(item.mRecommend.cover)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.bili_default_image_tv)
                        .dontAnimate()
                        .into(helper.getView(R.id.iv_video_preview))
                helper.setText(R.id.tv_video_play_num, NumberUtils.format(item.mRecommend.play.toString()))
                        .setText(R.id.tv_video_time, FormatUtils.formatDuration(item.mRecommend.duration.toString()))
                        .setText(R.id.tv_video_danmaku, NumberUtils.format(item.mRecommend.danmaku.toString()))
                        .setText(R.id.tv_video_title, item.mRecommend.title)
                if (item.mRecommend.open != 0) {
                    //直播
                    helper.setText(R.id.tv_video_tag, item.mRecommend.area)
                } else {
                    //推荐
                    helper.setText(R.id.tv_video_tag, item.mRecommend.tname + " · " + item.mRecommend.tag.tag_name)
                }
                helper.itemView.setOnClickListener {
                    //                    mContext.startActivity(Intent(mContext, VideoDetailActivity::class))
                }
            }
        }
    }

    companion object {
        class GlidImageLoader : ImageLoader() {
            override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
                Glide.with(context)
                        .load(path as String)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .into(imageView)
            }

        }
    }
}