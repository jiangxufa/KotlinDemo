package com.jiangxufa.homemodule.ui.home

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.alibaba.android.vlayout.layout.SingleLayoutHelper
import com.blankj.utilcode.util.LogUtils.d
import com.blankj.utilcode.util.SpanUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
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
import com.jiangxufa.homemodule.R
import com.jiangxufa.homemodule.common.CommonAdapter
import com.jiangxufa.homemodule.injection.component.DaggerHomeComponent
import com.jiangxufa.homemodule.injection.module.HomeModule
import com.jiangxufa.homemodule.model.chase.ChaseBangumi
import com.jiangxufa.homemodule.model.chase.RecommendBangumi
import com.jiangxufa.homemodule.ui.bangumi.BangumiDetailActivity
import com.jiangxufa.homemodule.utils.JsonUtils
import com.jiangxufa.homemodule.utils.NumberUtils
import io.reactivex.Observable
import kotlinx.android.synthetic.main.home_refresh_recycler.*
import javax.inject.Inject

/**
 * 创建时间：2018/10/9
 * 编写人：lenovo
 * 功能描述：
 */

interface ChaseBangumiContract {
    interface View : BaseView {
        fun showChaseBangumi(chaseBangumi: ChaseBangumi)
        fun showRecommendBangumi(recommendBangumi: RecommendBangumi)
    }

    interface Presenter : BaseIPresenter<View> {
        fun getChaseBangumiData()
    }
}

class ChaseBangumiFragment : BaseRefreshFragment<ChaseBangumiContract.View, ChaseBangumiPresenter>(),
        ChaseBangumiContract.View {

    companion object {
        fun instance(): Fragment = ChaseBangumiFragment()
    }

    override fun getLayoutId(): Int = R.layout.fragment_chase_bangumi

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
        initAdapter()
        val viewPool = RecyclerView.RecycledViewPool()
        viewPool.setMaxRecycledViews(0, 10)
        with(recycler) {
            layoutManager = virtualLayoutManager
            adapter = delegateAdapter
            recycledViewPool = viewPool
        }
    }

    private lateinit var virtualLayoutManager: VirtualLayoutManager
    private lateinit var delegateAdapter: DelegateAdapter

    private lateinit var mimeAdapter: CommonAdapter<ChaseBangumi.FollowsBean>
    private lateinit var jpAdapter: CommonAdapter<RecommendBangumi.RecommendJpBean.RecommendBeanX>
    private lateinit var jpFooter: CommonAdapter<RecommendBangumi.RecommendJpBean.FootBeanX>
    private lateinit var cnAdapter: CommonAdapter<RecommendBangumi.RecommendCnBean.RecommendBean>
    private lateinit var cnFooter: CommonAdapter<RecommendBangumi.RecommendCnBean.FootBean>

    private val adapters = ArrayList<DelegateAdapter.Adapter<*>>()

    private fun initAdapter() {
        virtualLayoutManager = VirtualLayoutManager(mContext)
        //第二个参数是是否复用不同adapter中的相同的view
        delegateAdapter = DelegateAdapter(virtualLayoutManager, false)

        //番剧 国番
        adapters.add(object : CommonAdapter<RecommendBangumi.RecommendJpBean>(R.layout.chase_item_title, mContext, SingleLayoutHelper(), 1) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                d("position $position")
            }
        })

        //我的追番标题
        adapters.add(object : CommonAdapter<RecommendBangumi.RecommendJpBean>(R.layout.chase_item_title_sub, mContext, SingleLayoutHelper(), 1) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                d("position $position")
                holder.setText(R.id.tv_title, "我的追番")
                holder.setImageResource(R.id.iv_icon, R.mipmap.bangumi_follow_home_ic_mine)
            }
        })

        mimeAdapter = object : CommonAdapter<ChaseBangumi.FollowsBean>(R.layout.item_home_chase_body, mContext, GridLayoutHelper(3), 3) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                d("position $position")
                if (datas.isEmpty()) return
                val followsBean = datas[position]
                Glide.with(mContext)
                        .load<Any>(followsBean.cover)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.bili_default_image_tv)
                        .dontAnimate()
                        .into(holder.getView<View>(R.id.iv_video_preview) as ImageView)
                holder.setText(R.id.tv_video_title, followsBean.title)//
                holder.setText(R.id.tv_video_update, SpanUtils()
                        .append("更新至第 " + followsBean.new_ep.index + " 话")
                        .setForegroundColor(AppUtils.getColor(R.color.pink_text_color)).create())
                holder.setText(R.id.tv_video_state, "尚未观看")
                holder.itemView.onClick {
                    startActivity(Intent(mContext,BangumiDetailActivity::class.java))
                }
            }
        }
        //我的追番内容
        adapters.add(mimeAdapter)

        //番剧推荐标题
        adapters.add(object : CommonAdapter<RecommendBangumi.RecommendJpBean>(R.layout.chase_item_title_sub, mContext, SingleLayoutHelper(), 1) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                d("position $position")
                holder.setText(R.id.tv_title, "番剧推荐")
                holder.setImageResource(R.id.iv_icon, R.mipmap.bangumi_follow_home_ic_bangumi)
            }
        })

        //番剧推荐内容
        jpAdapter = object : CommonAdapter<RecommendBangumi.RecommendJpBean.RecommendBeanX>(R.layout.item_home_chase_body, mContext, GridLayoutHelper(3), 3) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                d("position $position")
                if (datas.isEmpty()) return
                val recommendBean = datas[position]
                Glide.with(mContext)
                        .load<Any>(recommendBean.cover)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.bili_default_image_tv)
                        .dontAnimate()
                        .into(holder.getView<View>(R.id.iv_video_preview) as ImageView)
                holder.setText(R.id.tv_video_follow, NumberUtils.format(recommendBean.favourites) + "人追番")
                        .setText(R.id.tv_video_title, recommendBean.title)
                        .setText(R.id.tv_video_update, "更新至第" + recommendBean.newest_ep_index + "话")
                        .setVisible(R.id.tv_video_state, false)
                holder.itemView.onClick {
                    startActivity(Intent(mContext,BangumiDetailActivity::class.java))
                }
            }
        }
        adapters.add(jpAdapter)

        jpFooter = object : CommonAdapter<RecommendBangumi.RecommendJpBean.FootBeanX>(R.layout.layout_item_home_chase_footer, mContext, SingleLayoutHelper(), 1) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                d("position $position")
                if (data == null) return
                Glide.with(mContext)
                        .load<Any>(data!!.cover)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.bili_default_image_tv)
                        .dontAnimate()
                        .into(holder.getView(R.id.iv_video_preview) as ImageView)
                holder.setText(R.id.tv_title, data!!.title)
                        .setText(R.id.tv_des, data!!.desc)
                if (data!!.is_new == 1) {
                    holder.setVisible(R.id.tv_new_tag, true)
                } else {
                    holder.setVisible(R.id.tv_new_tag, false)
                }
            }
        }
        //番剧推荐尾部
        adapters.add(jpFooter)

        //国产动画推荐标题
        adapters.add(object : CommonAdapter<RecommendBangumi.RecommendJpBean>(R.layout.chase_item_title_sub, mContext, SingleLayoutHelper(), 1) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                d("position $position")
                holder.setText(R.id.tv_title, "国产动画推荐")
                holder.setImageResource(R.id.iv_icon, R.mipmap.bangumi_follow_home_ic_domestic)
            }
        })

        //国产动画推荐内容
        cnAdapter = object : CommonAdapter<RecommendBangumi.RecommendCnBean.RecommendBean>(
                R.layout.item_home_chase_body, mContext, GridLayoutHelper(3), 3) {

            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                d("position $position")
                if (datas.isEmpty()) return
                val recommendBean = datas[position]
                Glide.with(mContext)
                        .load<Any>(recommendBean.cover)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.bili_default_image_tv)
                        .dontAnimate()
                        .into(holder.getView<View>(R.id.iv_video_preview) as ImageView)
                holder.setText(R.id.tv_video_follow, NumberUtils.format(recommendBean.favourites) + "人追番")
                        .setText(R.id.tv_video_title, recommendBean.title)
                        .setText(R.id.tv_video_update, "更新至第" + recommendBean.newest_ep_index + "话")
                        .setVisible(R.id.tv_video_state, false)
                holder.itemView.onClick {
                    startActivity(Intent(mContext,BangumiDetailActivity::class.java))
                }
            }
        }
        adapters.add(cnAdapter)
        //国产动画推荐尾部
        cnFooter = object : CommonAdapter<RecommendBangumi.RecommendCnBean.FootBean>(R.layout.layout_item_home_chase_footer, mContext, SingleLayoutHelper(), 1) {
            override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
                d("position $position")
                if (data == null) return
                Glide.with(mContext)
                        .load<Any>(data!!.cover)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.bili_default_image_tv)
                        .dontAnimate()
                        .into(holder.getView(R.id.iv_video_preview) as ImageView)
                holder.setText(R.id.tv_title, data!!.title)
                        .setText(R.id.tv_des, data!!.desc)
                if (data!!.is_new == 1) {
                    holder.setVisible(R.id.tv_new_tag, true)
                } else {
                    holder.setVisible(R.id.tv_new_tag, false)
                }
            }
        }
        adapters.add(cnFooter)
    }

    override fun showChaseBangumi(chaseBangumi: ChaseBangumi) {
        AppUtils.runOnUI {
            mimeAdapter.setNewData(chaseBangumi.follows)
        }
    }


    override fun showRecommendBangumi(recommendBangumi: RecommendBangumi) {
        jpAdapter.setNewData(recommendBangumi.recommend_jp.recommend)
        jpFooter.setNewSingleData(recommendBangumi.recommend_jp.foot[0])
        cnAdapter.setNewData(recommendBangumi.recommend_cn.recommend)
        cnFooter.setNewSingleData(recommendBangumi.recommend_cn.foot[0])
    }

    override fun refreshLoadData() {
        mPresenter?.getChaseBangumiData()
    }

    override fun lazyLoadData() {
        if (isFirstVisible) {
            recycler?.post {
                delegateAdapter.setAdapters(adapters)
                refresh!!.isRefreshing = true
                mPresenter?.getChaseBangumiData()
            }
        }
    }

    override fun initData() {
    }
}

class ChaseBangumiPresenter @Inject constructor() : BasePresenter<ChaseBangumiContract.View>(), ChaseBangumiContract.Presenter {

    override fun getChaseBangumiData() {
        Observable.just(JsonUtils.readJson("user_chase.json"))
                .flatMap {
                    val gson = Gson()
                    val obj = JsonParser().parse(it).getAsJsonObject()
                    val result = obj.getAsJsonObject("result")
                    val chaseBangumi = gson.fromJson<ChaseBangumi>(result, ChaseBangumi::class.java)
                    mView?.showChaseBangumi(chaseBangumi)
                    return@flatMap Observable.just(JsonUtils.readJson("recommend_chase.json"))
                }
                .map {
                    val gson = Gson()
                    val obj = JsonParser().parse(it).getAsJsonObject()
                    val result = obj.getAsJsonObject("result")
                    return@map gson.fromJson(result, RecommendBangumi::class.java)
                }
                .excute(object : SampleSubscriber<RecommendBangumi>(mCompositeDisposable) {
                    override fun onSuccess(t: RecommendBangumi) {
                        mView?.showRecommendBangumi(t)
                    }
                }, lifecycleProvider, mView)
    }

}
