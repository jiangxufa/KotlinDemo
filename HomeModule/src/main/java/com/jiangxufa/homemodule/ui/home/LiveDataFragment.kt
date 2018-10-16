package com.jiangxufa.homemodule.ui.home

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.SpanUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.jiangxufa.baselibrary.common.BaseIPresenter
import com.jiangxufa.baselibrary.common.BasePresenter
import com.jiangxufa.baselibrary.common.BaseRefreshFragment
import com.jiangxufa.baselibrary.common.BaseView
import com.jiangxufa.baselibrary.ext.excute
import com.jiangxufa.baselibrary.ext.onBannerClick
import com.jiangxufa.baselibrary.rx.SampleSubscriber
import com.jiangxufa.baselibrary.utils.AppUtils
import com.jiangxufa.baselibrary.widgets.section.SectionedRVAdapter
import com.jiangxufa.baselibrary.widgets.section.StatelessSection
import com.jiangxufa.baselibrary.widgets.section.ViewHolder
import com.jiangxufa.businesslibrary.router.RouterPath
import com.jiangxufa.homemodule.R
import com.jiangxufa.homemodule.data.HomeRepository
import com.jiangxufa.homemodule.ext.handlerResult
import com.jiangxufa.homemodule.injection.component.DaggerHomeComponent
import com.jiangxufa.homemodule.injection.module.HomeModule
import com.jiangxufa.homemodule.model.live.LiveDataX
import com.jiangxufa.homemodule.model.live.LivePartition
import com.jiangxufa.homemodule.model.live.LiveRecommend
import com.jiangxufa.homemodule.model.live.support.LiveEnter
import com.jiangxufa.homemodule.utils.NumberUtils
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.home_refresh_recycler.*
import java.util.*
import javax.inject.Inject

/**
 * 创建时间：2018/10/13
 * 编写人：lenovo
 * 功能描述：
 */
interface LiveDataContract {
    interface View : BaseView {
        fun showLivePartition(livePartition: LivePartition)
        fun showLiveRecommend(liveRecommend: LiveRecommend)
    }

    interface Presenter : BaseIPresenter<View> {
        fun getLiveData()
    }
}

class LiveDataFragment : BaseRefreshFragment<LiveDataContract.View, LiveDataPresenter>()
        , LiveDataContract.View {

    companion object {
        fun instance(): Fragment = LiveDataFragment()
    }

    lateinit var mSectionAdapter: SectionedRVAdapter
    private lateinit var mLiveBannerSection: LiveBannerSection
    private lateinit var mPartitionsBeanList: List<LivePartition.PartitionsBean>

    override fun getLayoutId(): Int = R.layout.fragment_home_livedata

    override fun initInject() {
        DaggerHomeComponent.builder()
                .fragmentComponent(fragmentComponent)
                .homeModule(HomeModule())
                .build().inject(this)
        mPresenter?.attachView(this)
    }

    override fun initRecyclerView() {
        mSectionAdapter = SectionedRVAdapter()
        val gridLayout = GridLayoutManager(mContext, 2)
        gridLayout.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (mSectionAdapter.getSectionItemViewType(position)) {
                    SectionedRVAdapter.VIEW_TYPE_HEADER -> 2//1格  占的weight
                    SectionedRVAdapter.VIEW_TYPE_FOOTER -> 2//1格
                    else -> {
                        if (position == 9) {
                            2
                        } else {
                            1
                        }
                    }
                }
            }
        }
        with(recycler) {
            adapter = mSectionAdapter
            layoutManager = gridLayout
        }

        //轮播图
        mLiveBannerSection = LiveBannerSection()
        mSectionAdapter.addSection(mLiveBannerSection)
    }

    override fun lazyLoadData() {
        if (isFirstVisible) {
            recycler?.post {
                refresh!!.isRefreshing = true
                mPresenter?.getLiveData()
            }
        }
    }

    override fun refreshLoadData() {
        mPresenter?.getLiveData()
    }

    override fun initData() {
    }

    override fun showLivePartition(livePartition: LivePartition) {
        mPartitionsBeanList = livePartition.partitions
        AppUtils.runOnUI {
            mLiveBannerSection.setNewData(livePartition.banner)
        }
    }

    override fun showLiveRecommend(liveRecommend: LiveRecommend) {
        //分类
        mSectionAdapter.addSection(LiveEntranceSection())

        val mList = liveRecommend.recommend_data.lives
        val mPartitionBean = liveRecommend.recommend_data.partition
        val mBannerRecommendList = liveRecommend.recommend_data.banner_data
        //推荐主播
        if (mBannerRecommendList.size != 0) {
            mSectionAdapter.addSection(LiveRecommendSection(
                    LiveDataX(mPartitionBean.name, mPartitionBean.sub_icon.src, mPartitionBean.count.toString(), mList.subList(0, 11))))
        }
        //分区
        //绘画专区
        //生活娱乐
        //唱见舞见
        //手游直播
        //单机联机
        //网络游戏
        //电子竞技
        //御宅文化
        var count = 0
        val total = mPartitionsBeanList.size
        mPartitionsBeanList.forEach {

            count++
            mSectionAdapter.addSection(LiveRecommendPartitionSection(
                    LiveDataX(
                            it.partition.name,
                            it.partition.sub_icon.src,
                            it.partition.count.toString(),
                            lives = it.lives.subList(0, 4),
                            isLast = total == count
                    )))
        }
        mSectionAdapter.notifyDataSetChanged()

        refresh.isEnabled = false
    }
}

class LiveDataPresenter @Inject constructor() : BasePresenter<LiveDataContract.View>()
        , LiveDataContract.Presenter {

    @Inject
    @JvmField
    var homeRepository: HomeRepository? = null

    override fun getLiveData() {
        homeRepository!!.liveService.livePartition
                .handlerResult()
                .flatMap {
                    mView?.showLivePartition(it)
                    return@flatMap homeRepository!!.liveService.liveRecommend.handlerResult()
                }
                .excute(object : SampleSubscriber<LiveRecommend>(mCompositeDisposable) {
                    override fun onSuccess(t: LiveRecommend) {
                        mView?.showLiveRecommend(t)
                    }
                }, lifecycleProvider, mView)
    }

}

//轮播图
class LiveBannerSection(var mList: ArrayList<LivePartition.BannerBean> = arrayListOf()) : StatelessSection<LivePartition.BannerBean>(
        R.layout.layout_banner, R.layout.layout_empty) {

    fun setNewData(list: List<LivePartition.BannerBean>) {
        mList.clear()
        mList.addAll(list)
    }

    override fun onBindHeaderViewHolder(holder: ViewHolder) {
        val banner = holder.getView<Banner>(R.id.banner)
        val urls = mList.asSequence().map { it.img }.toList()
        banner.setIndicatorGravity(BannerConfig.RIGHT)
                .setImages(urls)
                .setImageLoader(GlideImageLoader())
                .start()
        banner.onBannerClick { position ->
            ARouter.getInstance().build(RouterPath.WebCenter.PATH_WEB)
                    .withString(RouterPath.WebCenter.EXTRA_URL, mList[position].link)
                    .withString(RouterPath.WebCenter.EXTRA_TITLE, mList[position].title)
                    .withString(RouterPath.WebCenter.EXTRA_IMAGE, mList[position].img)
                    .navigation()
        }
    }

    companion object {
        internal class GlideImageLoader : ImageLoader() {
            override fun displayImage(context: Context, path: Any, imageView: ImageView) {
                Glide.with(context)
                        .load(path as String)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .into(imageView)
            }
        }
    }
}

//分类
class LiveEntranceSection : StatelessSection<String>(
        R.layout.layout_item_home_live_entrance, R.layout.layout_empty) {

    private var mList: ArrayList<LiveEnter> = arrayListOf()

    init {
        mList.add(LiveEnter("关注", R.mipmap.live_home_follow_anchor))
        mList.add(LiveEnter("中心", R.mipmap.live_home_live_center))
        mList.add(LiveEnter("小视频", R.mipmap.live_home_clip_video))
        mList.add(LiveEnter("搜索", R.mipmap.live_home_search_room))
        mList.add(LiveEnter("分类", R.mipmap.live_home_all_category))
    }

    override fun onBindHeaderViewHolder(holder: ViewHolder) {
        val recyclerView = holder.getView<RecyclerView>(R.id.recycler)
        with(recyclerView) {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = GridLayoutManager(mContext, 5)
            adapter = LiveEntranceAdapter(mList)
        }
    }

}

//分类里面的adapter
class LiveEntranceAdapter(data: List<LiveEnter>) : BaseQuickAdapter<LiveEnter, BaseViewHolder>(R.layout.item_live_entrance, data) {

    override fun convert(helper: BaseViewHolder, item: LiveEnter) {
        helper.setText(R.id.tv_title, item.title)
                .setImageResource(R.id.iv_icon, item.img)
    }
}

//推荐主播
class LiveRecommendSection(var liveDataX: LiveDataX) : StatelessSection<LiveRecommend.RecommendDataBean.LivesBean>(
        R.layout.layout_item_home_live_head,
        R.layout.layout_item_home_live_footer,
        R.layout.layout_item_home_live_body, liveDataX.bodybeans) {

    private val mRandom = Random()

    override fun onBindHeaderViewHolder(holder: ViewHolder) {
        holder.setVisible(R.id.cl_type_root, true)
        Glide.with(mContext)
                .load(liveDataX.url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.getView(R.id.iv_icon) as ImageView)
        holder.setText(R.id.tv_title, liveDataX.title)
        val stringBuilder = SpannableStringBuilder("当前" + liveDataX.count + "个直播")
        val foregroundColorSpan = ForegroundColorSpan(
                mContext.resources.getColor(R.color.pink_text_color))
        stringBuilder.setSpan(foregroundColorSpan, 2,
                stringBuilder.length - 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.setText(R.id.tv_online, stringBuilder)
    }

    override fun convert(holder: ViewHolder, livesBean: LiveRecommend.RecommendDataBean.LivesBean, position: Int) {
        Glide.with(mContext)
                .load<Any>(livesBean.cover.src)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.bili_default_image_tv)
                .dontAnimate()
                .into(holder.getView(R.id.iv_video_preview) as ImageView)
        holder.setText(R.id.tv_video_live_up, livesBean.owner.name)//up
                .setText(R.id.tv_video_online, NumberUtils.format(livesBean.online.toString()))//在线人数;
        holder.setText(R.id.tv_video_title, SpanUtils()
                .append("#" + livesBean.area + "#")
                .setForegroundColor(AppUtils.getColor(R.color.pink_text_color))
                .append(livesBean.title).create())
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
        holder.setVisible(R.id.bt_more, true)
                .setText(R.id.tv_dynamic, "${mRandom.nextInt(200)} 条新动态，点击这里刷新")
        holder.getView<ImageView>(R.id.iv_refresh).setOnClickListener { view ->
            view.animate()
                    .rotation(360f)
                    .setInterpolator(LinearInterpolator())
                    .setDuration(1000).start()
        }
        holder.getView<ImageView>(R.id.iv_refresh).setOnClickListener { view ->
            view.animate()
                    .rotation(360f)
                    .setInterpolator(LinearInterpolator())
                    .setDuration(1000).start()
        }
    }
}

//下面的分类
class LiveRecommendPartitionSection(var liveDataX: LiveDataX) : StatelessSection<LivePartition.PartitionsBean.LivesBean>(
        R.layout.layout_item_home_live_head,
        R.layout.layout_item_home_live_footer,
        R.layout.layout_item_home_live_body, liveDataX.lives) {

    private val mRandom = Random()

    override fun onBindHeaderViewHolder(holder: ViewHolder) {
        Glide.with(mContext)
                .load(liveDataX.url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.getView(R.id.iv_icon) as ImageView)
        holder.setText(R.id.tv_title, liveDataX.title)
        holder.setText(R.id.tv_online, SpanUtils()
                .append("当前")
                .append("" + liveDataX.count)
                .setForegroundColor(AppUtils.getColor(R.color.pink_text_color))
                .append("个直播")
                .create())
    }

    override fun convert(holder: ViewHolder, livesBean: LivePartition.PartitionsBean.LivesBean, position: Int) {
        Glide.with(mContext)
                .load<Any>(livesBean.cover.src)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.bili_default_image_tv)
                .dontAnimate()
                .into(holder.getView(R.id.iv_video_preview) as ImageView)
        holder.setText(R.id.tv_video_live_up, livesBean.owner.name)//up
                .setText(R.id.tv_video_online, NumberUtils.format(livesBean.online.toString() + ""))//在线人数;
        holder.setText(R.id.tv_video_title, livesBean.title)
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
        if (liveDataX.isLast) {
            holder.setVisible(R.id.bt_more_live, true)
        } else {
            holder.setVisible(R.id.bt_more_live, false)
        }
        holder.setText(R.id.tv_dynamic, "${mRandom.nextInt(200)} 条新动态，点击这里刷新")
        holder.getView<ImageView>(R.id.iv_refresh).setOnClickListener { view ->
            view.animate()
                    .rotation(360f)
                    .setInterpolator(LinearInterpolator())
                    .setDuration(1000).start()
        }
    }

}