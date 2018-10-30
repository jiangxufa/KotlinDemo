package com.jiangxufa.homemodule.ui.bangumi

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jiangxufa.baselibrary.common.BaseIPresenter
import com.jiangxufa.baselibrary.common.BasePresenter
import com.jiangxufa.baselibrary.common.BaseRefreshActivity
import com.jiangxufa.baselibrary.common.BaseView
import com.jiangxufa.baselibrary.ext.excute
import com.jiangxufa.baselibrary.ext.onClick
import com.jiangxufa.baselibrary.rx.SampleSubscriber
import com.jiangxufa.homemodule.R
import com.jiangxufa.homemodule.data.HomeRepository
import com.jiangxufa.homemodule.ext.handlerResult
import com.jiangxufa.homemodule.injection.component.DaggerHomeComponent
import com.jiangxufa.homemodule.injection.module.HomeModule
import com.jiangxufa.homemodule.model.bandumi.MulBangumiDetail
import com.jiangxufa.homemodule.ui.adapter.BangumiDetailAdapter
import kotlinx.android.synthetic.main.activity_bangumi_detail.*
import kotlinx.android.synthetic.main.common_layout_error.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * 创建时间：2018/10/16
 * 编写人：lenovo
 * 功能描述：
 */
interface BangumiDetailContract {
    interface View : BaseView {
        fun showMulBangumiDetail(mulBangumiDetails: List<MulBangumiDetail>, title: String)
    }

    interface Presenter : BaseIPresenter<View> {
        fun getBangumiDatail()
    }
}

class BangumiDetailActivity : BaseRefreshActivity<BangumiPresenter>(), BangumiDetailContract.View {

    override fun getLayoutId(): Int = R.layout.activity_bangumi_detail

    private val bangumiDetailAdapter by lazy {
        BangumiDetailAdapter(arrayListOf())
    }


    override fun initToolBar() {
        with(toolbar) {
            title = ""
            setNavigationIcon(R.mipmap.ic_clip_back_white)
            setSupportActionBar(this)
            onClick { finish() }
        }
    }

    override fun initInject() {
        DaggerHomeComponent.builder()
                .activityComponent(activityComponent)
                .homeModule(HomeModule())
                .build().inject(this)
        mPresenter?.attachView(this)
    }

    override fun initRecyclerView() {
        with(recycler) {
            layoutManager = LinearLayoutManager(this@BangumiDetailActivity)
            adapter = bangumiDetailAdapter
        }
    }

    override fun loadData() {
        cl_error.visibility = View.VISIBLE
        mPresenter?.getBangumiDatail()
    }

    override fun showMulBangumiDetail(mulBangumiDetails: List<MulBangumiDetail>, title: String) {
        toolbar.title = title
        bangumiDetailAdapter.setNewData(mulBangumiDetails)
    }

    override fun complete() {
        super.complete()
        cl_error.visibility = View.GONE
    }
}

class BangumiPresenter @Inject constructor(): BasePresenter<BangumiDetailContract.View>(), BangumiDetailContract.Presenter {

    @Inject
    @JvmField
    var homeRepository: HomeRepository? = null

    override fun getBangumiDatail() {
        val mulBangumiDetails = ArrayList<MulBangumiDetail>()
        val title = StringBuilder()
        homeRepository!!.bangumiService.bangumiDetail
                .handlerResult()
                .flatMap {
                    title.append(it.title)
                    val episodes = it.episodes
                    episodes.reverse()
                    mulBangumiDetails.addAll(Arrays.asList(
                            MulBangumiDetail()
                                    .setItemType(MulBangumiDetail.TYPE_HEAD)//头部
                                    .setPlayCount(it.play_count)
                                    .setCover(it.cover)
                                    .setFavorites(it.favorites)
                                    .setIsFinish(it.is_finish),
                            MulBangumiDetail()
                                    .setItemType(MulBangumiDetail.TYPE_SEASON)//分季节
                                    .setSeasonsTitle(it.season_title)
                                    .setSeasonsBeanList(it.seasons),
                            MulBangumiDetail()
                                    .setItemType(MulBangumiDetail.TYPE_EPISODE_HEAD)
                                    .setTotalCount(it.total_count)
                                    .setIsFinish(it.is_finish), //分集头部
                            MulBangumiDetail()
                                    .setItemType(MulBangumiDetail.TYPE_EPISODE_ITEM)//分集
                                    .setEpisodesBeans(episodes),
                            MulBangumiDetail()
                                    .setItemType(MulBangumiDetail.TYPE_CONTRACTED)
                                    .setlistBeanList(it.rank.list)
                                    .setTotalBpCount(it.rank.total_bp_count)
                                    .setWeekBpCount(it.rank.week_bp_count), //承包
                            MulBangumiDetail()
                                    .setItemType(MulBangumiDetail.TYPE_DES)
                                    .setEvaluate(it.evaluate)
                                    .setTagsBeanList(it.tags)//简介
                    ))
                    return@flatMap homeRepository!!.bangumiService.bangumiDetailRecommend.handlerResult()
                }
                .flatMap {
                    mulBangumiDetails.addAll(Arrays.asList(MulBangumiDetail()
                            .setItemType(MulBangumiDetail.TYPE_RECOMMEND_HEAD), //推荐头部
                            MulBangumiDetail()
                                    .setItemType(MulBangumiDetail.TYPE_RECOMMEND_ITEM)
                                    .setBangumiRecommendList(it.list)//推荐
                    ))
                    return@flatMap homeRepository!!.apiService.bangumiDetailComment
                }
                .map { it ->
                    mulBangumiDetails.add(MulBangumiDetail()
                            .setItemType(MulBangumiDetail.TYPE_COMMENT_HEAD)
                            .setNum(it.data.page.num)
                            .setAccount(it.data.page.acount))
                    it.data.hots.forEach {
                        mulBangumiDetails.add(MulBangumiDetail()
                                .setItemType(MulBangumiDetail.TYPE_COMMENT_HOT_ITEM)
                                .setHotsBean(it))//热门评论
                    }
                    mulBangumiDetails.add(MulBangumiDetail().setItemType(MulBangumiDetail.TYPE_COMMENT_MORE))

                    it.data.replies.forEach {
                        mulBangumiDetails.add(MulBangumiDetail()//普通评论
                                .setItemType(MulBangumiDetail.TYPE_COMMENT_NOMAL_ITEM)
                                .setRepliesBean(it))
                    }
                    return@map mulBangumiDetails
                }
                .excute(object : SampleSubscriber<ArrayList<MulBangumiDetail>>(mCompositeDisposable){
                    override fun onSuccess(t: ArrayList<MulBangumiDetail>) {
                        mView?.showMulBangumiDetail(mulBangumiDetails, title.toString())
                    }
                },lifecycleProvider, mView)
    }

}