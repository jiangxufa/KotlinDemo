package com.jiangxufa.homemodule.ui.adapter

import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.jiangxufa.homemodule.R
import com.jiangxufa.homemodule.model.bandumi.BangumiDetail

/**
 * @author zzq  作者 E-mail:   soleilyoyiyi@gmail.com
 * @date 创建时间：2017/6/12 17:31
 * 描述:
 */

class BangumiDetailEpisodeAdapter(data: List<BangumiDetail.EpisodesBean>?) : BaseQuickAdapter<BangumiDetail.EpisodesBean, BaseViewHolder>(R.layout.item_bangumi_detail_episodes, data) {

    private var mOldPos: Int = 0
    private var mNewPos: Int = 0

    override fun convert(holder: BaseViewHolder, episodesBean: BangumiDetail.EpisodesBean) {
        holder.setText(R.id.tv_index, "第" + episodesBean.index + "话")
        holder.setText(R.id.tv_index_title, episodesBean.index_title)
        holder.itemView.setOnClickListener { view ->
            mNewPos = holder.adapterPosition//新位置
            mOldPos = mNewPos
            notifyDataSetChanged()
        }
        if (holder.adapterPosition == mNewPos) {
            holder.getView<TextView>(R.id.tv_index_title).isEnabled = true
            holder.getView<TextView>(R.id.tv_index).isEnabled = true
            holder.getView<LinearLayout>(R.id.ll_root).isEnabled = true
        } else {
            holder.getView<TextView>(R.id.tv_index_title).isEnabled = false
            holder.getView<TextView>(R.id.tv_index).isEnabled = false
            holder.getView<LinearLayout>(R.id.ll_root).isEnabled = false
        }
        if (mNewPos != mOldPos) {
            holder.getView<TextView>(R.id.tv_index_title).isEnabled = false
            holder.getView<TextView>(R.id.tv_index).isEnabled = false
            holder.getView<LinearLayout>(R.id.ll_root).isEnabled = false
        }
        if (holder.adapterPosition == itemCount - 1) {
            holder.setVisible(R.id.space, true)
        } else {
            holder.setVisible(R.id.space, false)
        }
        //跳转到播放界面
//        holder.itemView.setOnClickListener { view -> mContext.startActivity(Intent(mContext, VideoPlayerActivity::class.java)) }
    }
}
