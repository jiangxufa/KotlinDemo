package com.jiangxufa.homemodule.ui.adapter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.SpanUtils
import com.blankj.utilcode.util.TimeUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.jiangxufa.baselibrary.utils.AppUtils
import com.jiangxufa.baselibrary.widgets.CircleImageView
import com.jiangxufa.baselibrary.widgets.flowlayout.FlowLayout
import com.jiangxufa.baselibrary.widgets.flowlayout.TagAdapter
import com.jiangxufa.baselibrary.widgets.flowlayout.TagFlowLayout
import com.jiangxufa.homemodule.R
import com.jiangxufa.homemodule.model.bandumi.BangumiDetail
import com.jiangxufa.homemodule.model.bandumi.MulBangumiDetail
import com.jiangxufa.homemodule.utils.NumberUtils
import jp.wasabeef.glide.transformations.BlurTransformation

/**
 * 创建时间：2018/10/16
 * 编写人：lenovo
 * 功能描述：
 */
class BangumiDetailAdapter(data:List<MulBangumiDetail>):BaseMultiItemQuickAdapter<MulBangumiDetail,BaseViewHolder>(data){

    init {
        addItemType(MulBangumiDetail.TYPE_HEAD, R.layout.layout_item_bangumi_detail_info)//头部信息
        addItemType(MulBangumiDetail.TYPE_SEASON, R.layout.layout_item_bangumi_detail_recycler)//分季
        addItemType(MulBangumiDetail.TYPE_EPISODE_HEAD, R.layout.layout_item_bangumi_detail_head)//分集头部
        addItemType(MulBangumiDetail.TYPE_EPISODE_ITEM, R.layout.layout_item_bangumi_detail_recycler)//分集
        addItemType(MulBangumiDetail.TYPE_CONTRACTED, R.layout.layout_bangumi_detail_contracted)//承包
        addItemType(MulBangumiDetail.TYPE_DES, R.layout.layout_item_bangumi_detail_des)//简介
        addItemType(MulBangumiDetail.TYPE_RECOMMEND_HEAD, R.layout.layout_item_bangumi_detail_head)//推荐头部
        addItemType(MulBangumiDetail.TYPE_RECOMMEND_ITEM, R.layout.layout_item_bangumi_detail_recommend)//推荐item
        addItemType(MulBangumiDetail.TYPE_COMMENT_HEAD, R.layout.layout_item_bangumi_detail_head)//评论头部
        addItemType(MulBangumiDetail.TYPE_COMMENT_HOT_ITEM, R.layout.layout_item_bangumi_detail_comment)//热门评论
        addItemType(MulBangumiDetail.TYPE_COMMENT_MORE, R.layout.layout_item_bangumi_detail_more)//更多推荐
        addItemType(MulBangumiDetail.TYPE_COMMENT_NOMAL_ITEM, R.layout.layout_item_bangumi_detail_comment)//评论
    }

    override fun convert(holder: BaseViewHolder, mulBangumiDetail: MulBangumiDetail) {
        when (mulBangumiDetail.itemType) {
            MulBangumiDetail.TYPE_HEAD ->{
                if (!mulBangumiDetail.isPrepare) {
                    holder.setText(R.id.tv_play, "播放:" + NumberUtils.format(mulBangumiDetail.playCount + ""))
                            .setText(R.id.tv_follow, "追番" + NumberUtils.format(mulBangumiDetail.favorites + ""))
                            .setText(R.id.tv_state, if (TextUtils.equals(mulBangumiDetail.isFinish, "0")) "连载中" else "已完结")
                    Glide.with(mContext)
                            .load<Any>(mulBangumiDetail.cover)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .dontAnimate()
                            .into(holder.getView<View>(R.id.iv_pic) as ImageView)
                    Glide.with(mContext)
                            .load<Any>(mulBangumiDetail.cover)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.bili_default_image_tv)
                            .bitmapTransform(BlurTransformation(mContext, 26))
                            .dontAnimate()
                            .into(holder.getView<View>(R.id.iv_pic_big) as ImageView)
                }
            }
            MulBangumiDetail.TYPE_SEASON ->{
                with(holder.getView<RecyclerView>(R.id.recycler)){
                    setHasFixedSize(true)
                    isNestedScrollingEnabled = false
                    layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false)
                    adapter = BangumiDetailSeasonAdapter(mulBangumiDetail.seasonsBeanList, mulBangumiDetail.seasonsTitle)
                }
            }
            MulBangumiDetail.TYPE_EPISODE_HEAD ->{
                holder.setText(R.id.tv_title, "选集")
                if (TextUtils.equals(mulBangumiDetail.isFinish, "1")) {
                    holder.setText(R.id.tv_online, "一共 " + mulBangumiDetail.totalCount + " 话")
                } else {
                    holder.setText(R.id.tv_online, "更新至第 " + mulBangumiDetail.totalCount + " 话")
                }
            }
            MulBangumiDetail.TYPE_EPISODE_ITEM ->{
                with(holder.getView<RecyclerView>(R.id.recycler)){
                    setHasFixedSize(true)
                    isNestedScrollingEnabled = false
                    layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false)
                    adapter = BangumiDetailEpisodeAdapter(mulBangumiDetail.episodesBeans)
                }
            }
            MulBangumiDetail.TYPE_CONTRACTED ->{
                holder.setText(R.id.tv_pay_count, "已有" + mulBangumiDetail.totalBpCount + "人承包了这部番")
                        .setText(R.id.tv_week_count, "等" + mulBangumiDetail.weekBpCount + "人七日内承包了这部番")
                val ids = intArrayOf(R.id.iv_avatar1, R.id.iv_avatar2, R.id.iv_avatar3, R.id.iv_avatar1, R.id.iv_avatar4)
                val beanList = mulBangumiDetail.listBeanList
            }
            MulBangumiDetail.TYPE_DES ->{
                holder.setText(R.id.tv_des, mulBangumiDetail.evaluate)
                        .setText(R.id.tv_title, "简介")
                        .setText(R.id.tv_online, "更多")
                val tagsLayout = holder.getView<TagFlowLayout>(R.id.tags_layout)
                tagsLayout.adapter = object : TagAdapter<BangumiDetail.TagsBean>(mulBangumiDetail.tagsBeanList) {
                    override fun getView(flowLayout: FlowLayout, i: Int, listBean: BangumiDetail.TagsBean): View {
                        val mTags = LayoutInflater.from(mContext)
                                .inflate(R.layout.layout_hot_tags_item, flowLayout, false) as TextView
                        mTags.text = listBean.tag_name
                        return mTags
                    }
                }
            }
            MulBangumiDetail.TYPE_RECOMMEND_HEAD ->{
                holder.setText(R.id.tv_title, "更多推荐")
                        .setText(R.id.tv_online, "换一换")
                        .setVisible(R.id.iv_trans, true)
                        .setVisible(R.id.iv_arrow, false)
            }
            MulBangumiDetail.TYPE_RECOMMEND_ITEM ->{

            }
            MulBangumiDetail.TYPE_COMMENT_HEAD ->{
                holder.setText(R.id.tv_title, SpanUtils()
                        .append("评论  ")
                        .append("第")
                        .append(mulBangumiDetail.num.toString() + "")
                        .append("话")
                        .append("(" + mulBangumiDetail.account + ")").setForegroundColor(AppUtils.getColor(R.color.black_alpha_30))
                        .create())
                        .setText(R.id.tv_online, "选集")
            }
            MulBangumiDetail.TYPE_COMMENT_HOT_ITEM ->{}
            MulBangumiDetail.TYPE_COMMENT_MORE ->{}
            MulBangumiDetail.TYPE_COMMENT_NOMAL_ITEM ->{
                holder.setVisible(R.id.tv_rcount, false)
                        .setText(R.id.tv_like, mulBangumiDetail.repliesBean.like.toString() + "")
                        .setText(R.id.tv_uname, SpanUtils()
                                .append(mulBangumiDetail.repliesBean.member.uname + " ")
                                .setForegroundColor(AppUtils.getColor(R.color.gray_20))
                                .appendSpace(10)
                                .appendImage(getIdRes(mulBangumiDetail.repliesBean.member.level_info.current_level), SpanUtils.ALIGN_CENTER)
                                .create())
                        .setText(R.id.tv_floor, "#" + mulBangumiDetail.repliesBean.floor)
                        .setText(R.id.tv_time, TimeUtils.millis2String((mulBangumiDetail.repliesBean.ctime * Math.pow(10.0, 3.0)).toLong()))
                        .setText(R.id.tv_message, mulBangumiDetail.repliesBean.content.message)
                Glide.with(mContext)
                        .load<Any>(mulBangumiDetail.repliesBean.member.avatar)
                        .centerCrop()
                        .placeholder(R.mipmap.bili_default_avatar)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .into(holder.getView<CircleImageView>(R.id.iv_avatar))
            }
        }
    }

    private fun getIdRes(lv: Int): Int {
        return when (lv) {
            1 -> R.mipmap.ic_lv1
            2 -> R.mipmap.ic_lv2
            3 -> R.mipmap.ic_lv3
            4 -> R.mipmap.ic_lv4
            5 -> R.mipmap.ic_lv5
            6 -> R.mipmap.ic_lv6
            7 -> R.mipmap.ic_lv7
            8 -> R.mipmap.ic_lv8
            9 -> R.mipmap.ic_lv9
            else -> R.mipmap.ic_lv0
        }
    }

}