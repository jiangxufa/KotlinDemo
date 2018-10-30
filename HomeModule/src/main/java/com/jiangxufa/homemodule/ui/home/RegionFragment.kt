package com.jiangxufa.homemodule.ui.home

import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.jiangxufa.baselibrary.common.BaseIPresenter
import com.jiangxufa.baselibrary.common.BasePresenter
import com.jiangxufa.baselibrary.common.BaseRefreshFragment
import com.jiangxufa.baselibrary.common.BaseView
import com.jiangxufa.baselibrary.ext.excute
import com.jiangxufa.baselibrary.rx.SampleSubscriber
import com.jiangxufa.baselibrary.utils.AppUtils
import com.jiangxufa.baselibrary.widgets.section.SectionedRVAdapter
import com.jiangxufa.baselibrary.widgets.section.StatelessSection
import com.jiangxufa.baselibrary.widgets.section.ViewHolder
import com.jiangxufa.homemodule.R
import com.jiangxufa.homemodule.data.HomeRepository
import com.jiangxufa.homemodule.injection.component.DaggerHomeComponent
import com.jiangxufa.homemodule.injection.module.HomeModule
import com.jiangxufa.homemodule.model.HttpResponse
import com.jiangxufa.homemodule.model.region.Region
import com.jiangxufa.homemodule.model.region.RegionEnter
import com.jiangxufa.homemodule.model.region.RegionTagType
import com.jiangxufa.homemodule.model.region.RegionX
import com.jiangxufa.homemodule.ui.adapter.RegionEntranceSection
import com.jiangxufa.homemodule.utils.JsonUtils
import com.jiangxufa.homemodule.utils.NumberUtils
import io.reactivex.Observable
import kotlinx.android.synthetic.main.home_refresh_recycler.*
import java.util.*
import javax.inject.Inject

/**
 * 创建时间：2018/10/10
 * 编写人：lenovo
 * 功能描述：
 */
interface RegionContract {
    interface View : BaseView {
        fun showGrid(grids: List<RegionEnter>)
        fun showRegionX(regions: List<RegionX>)
    }

    interface Presenter : BaseIPresenter<View> {
        fun createGridData()
        fun getRegionxs()
    }
}

class RegionFragment : BaseRefreshFragment<RegionContract.View, RegionPresenter>(), RegionContract.View {

    companion object {
        fun instance(): Fragment = RegionFragment()
    }

    var mSetionAdapter: SectionedRVAdapter? = null

    override fun getLayoutId(): Int = R.layout.fragment_region

    override fun initInject() {
        super.initInject()
        DaggerHomeComponent.builder()
//                .fragmentComponent(fragmentComponent)
                .activityComponent(activityComponent)
                .homeModule(HomeModule())
                .build().inject(this)
        mPresenter?.attachView(this)
    }

    override fun initRecyclerView() {
        mSetionAdapter = SectionedRVAdapter()
        val mLayoutManager = GridLayoutManager(mContext, 2)
        mLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (mSetionAdapter!!.getSectionItemViewType(position)) {
                    SectionedRVAdapter.VIEW_TYPE_HEADER -> 2
                    SectionedRVAdapter.VIEW_TYPE_FOOTER -> 2
                    else -> 1
                }
            }
        }

        with(recycler) {
            layoutManager = mLayoutManager
            adapter = mSetionAdapter
        }
    }

    override fun refreshLoadData() {
        mPresenter?.getRegionxs()
    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        if (isFirstVisible) {
            recycler?.post {
                refresh!!.isRefreshing = true
                mPresenter?.createGridData()
                mPresenter?.getRegionxs()
            }
        }
    }

    override fun initData() {
    }

    override fun showGrid(grids: List<RegionEnter>) {
        mSetionAdapter!!.addSection(RegionEntranceSection(grids))
    }

    override fun showRegionX(regions: List<RegionX>) {
        regions.forEach {
            mSetionAdapter!!.addSection(RegionSection(it))
        }
        mSetionAdapter!!.notifyDataSetChanged()
        refresh.isEnabled = false
    }
}

class RegionPresenter @Inject constructor() : BasePresenter<RegionContract.View>(), RegionContract.Presenter {

    @Inject
    @JvmField
    var homeRepository: HomeRepository? = null

    private var grids = ArrayList<RegionEnter>()

    private var images = arrayListOf(
            R.mipmap.ic_category_t13,
            R.mipmap.ic_category_t1,
            R.mipmap.ic_category_t167,
            R.mipmap.ic_category_t3,
            R.mipmap.ic_category_t129,
            R.mipmap.ic_category_t4,
            R.mipmap.ic_category_t36,
            R.mipmap.ic_category_t160,
            R.mipmap.ic_category_t13,
            R.mipmap.ic_category_t155,
            R.mipmap.ic_category_t165,
            R.mipmap.ic_category_t5,
            R.mipmap.ic_category_t23,
            R.mipmap.ic_category_t11
    )

    override fun createGridData() {
        grids.add(RegionEnter("直播", R.mipmap.ic_category_live))
        grids.add(RegionEnter("番剧", R.mipmap.ic_category_t13))
        grids.add(RegionEnter("动画", R.mipmap.ic_category_t1))
        grids.add(RegionEnter("国创", R.mipmap.ic_category_t167))
        grids.add(RegionEnter("音乐", R.mipmap.ic_category_t3))
        grids.add(RegionEnter("舞蹈", R.mipmap.ic_category_t129))
        grids.add(RegionEnter("游戏", R.mipmap.ic_category_t4))
        grids.add(RegionEnter("科技", R.mipmap.ic_category_t36))
        grids.add(RegionEnter("生活", R.mipmap.ic_category_t160))
        grids.add(RegionEnter("鬼畜", R.mipmap.ic_category_t11))
        grids.add(RegionEnter("时尚", R.mipmap.ic_category_t155))
        grids.add(RegionEnter("广告", R.mipmap.ic_category_t165))
        grids.add(RegionEnter("娱乐", R.mipmap.ic_category_t5))
        grids.add(RegionEnter("电影", R.mipmap.ic_category_t23))
        grids.add(RegionEnter("电视剧", R.mipmap.ic_category_t11))
        grids.add(RegionEnter("游戏中心", R.mipmap.ic_category_game_center))
        mView?.showGrid(grids)
    }

    override fun getRegionxs() {
        Observable.just(JsonUtils.readJson("region.json"))
                .flatMap {
                    val gson = Gson()
                    val obj = JsonParser().parse(it).getAsJsonObject()
                    val array = obj.getAsJsonArray("data")
                    val regionTypes = ArrayList<RegionTagType>()
                    for (jsonElement in array) {
                        regionTypes.add(gson.fromJson<RegionTagType>(jsonElement, RegionTagType::class.java))
                    }
                    return@flatMap homeRepository?.appService?.region
                }
                .excute(object : SampleSubscriber<HttpResponse<List<com.jiangxufa.homemodule.model.region.Region>>>(mCompositeDisposable) {
                    override fun onSuccess(t: HttpResponse<List<com.jiangxufa.homemodule.model.region.Region>>) {
                        val datas = t.data
                        val regionxs = ArrayList<RegionX>()
                        var i = 0
                        for (region in datas) {
                            regionxs.add(RegionX(region.title, images[i], bodybeans = region.body))
                            i = (i + 1) % 14
                        }
                        mView?.showRegionX(regionxs)
                    }
                }, lifecycleProvider, mView)
    }
}

class RegionSection(var regionX: RegionX) : StatelessSection<Region.BodyBean>(R.layout.layout_item_home_region_head,
        R.layout.layout_item_home_region_footer,
        R.layout.layout_item_home_region_body,regionX.bodybeans) {

    val random = Random()

    override fun onBindHeaderViewHolder(holder: ViewHolder) {
        holder.setImageResource(R.id.iv_icon, regionX.icon)
                .setText(R.id.tv_title, regionX.title)
    }

    override fun convert(holder: ViewHolder, bodyBean: Region.BodyBean, position: Int) {
        Glide.with(mContext)
                .load<Any>(bodyBean.cover)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.bili_default_image_tv)
                .dontAnimate()
                .into(holder.getView(R.id.iv_video_preview) as ImageView)
        holder.setText(R.id.tv_video_title, bodyBean.title)
        holder.setText(R.id.tv_video_play_num, NumberUtils.format(bodyBean.play.toString()))
        if (TextUtils.equals("番剧区", regionX.title)) {
            holder.setVisible(R.id.iv_video_online_region, false)
                    .setVisible(R.id.iv_video_online, true)
                    .setText(R.id.tv_video_favourite, NumberUtils.format(bodyBean.favourite.toString()))
        } else {
            holder.setVisible(R.id.iv_video_online_region, true)
                    .setVisible(R.id.iv_video_online, false)
                    .setText(R.id.tv_video_favourite, NumberUtils.format(bodyBean.danmaku.toString()))
        }
        if (position % 2 == 0) {
            setMargins(holder.itemView, AppUtils.getDimension(R.dimen.dp_10).toInt(),
                    AppUtils.getDimension(R.dimen.dp_5).toInt(),
                    AppUtils.getDimension(R.dimen.dp_5).toInt(),
                    AppUtils.getDimension(R.dimen.dp_5).toInt())
        } else {
            setMargins(holder.itemView, AppUtils.getDimension(R.dimen.dp_5).toInt(),
                    AppUtils.getDimension(R.dimen.dp_5).toInt(),
                    AppUtils.getDimension(R.dimen.dp_10).toInt(),
                    AppUtils.getDimension(R.dimen.dp_5).toInt())
        }
    }

    override fun onBindFooterViewHolder(holder: ViewHolder) {
        //尾
        holder.setText(R.id.bt_more, "更多" + regionX.title.subSequence(0, regionX.title.length - 1))
                .setText(R.id.tv_dynamic, random.nextInt(200).toString() + "条新动态，点击这里刷新")
        if (TextUtils.equals("游戏区", regionX.title)) {
            holder.setVisible(R.id.bt_more, false)
                    .setVisible(R.id.bt_more_game, true)
                    .setVisible(R.id.bt_game_center, true)
        } else {
            holder.setVisible(R.id.bt_more, true)
                    .setVisible(R.id.bt_more_game, false)
                    .setVisible(R.id.bt_game_center, false)
        }
    }
}