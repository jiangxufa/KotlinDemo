package com.jiangxufa.homemodule.ui.adapter

import android.text.TextUtils
import android.widget.LinearLayout
import android.widget.TextView

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.jiangxufa.homemodule.R
import com.jiangxufa.homemodule.model.bandumi.BangumiDetail


class BangumiDetailSeasonAdapter(data: List<BangumiDetail.SeasonsBean>?, private val mSeasonTitle: String) : BaseQuickAdapter<BangumiDetail.SeasonsBean, BaseViewHolder>(R.layout.item_bangumi_detail_seasons, data) {

    private var mOldPos: Int = 0
    private var mNewPos = -1

    private var mFlag = true

    override fun convert(holder: BaseViewHolder, seasonsBean: BangumiDetail.SeasonsBean) {
        holder.setText(R.id.tv_index, seasonsBean.title)
        if (mFlag) {
            if (TextUtils.equals(seasonsBean.title, mSeasonTitle)) {
                mNewPos = holder.adapterPosition
                mOldPos = mNewPos
                mFlag = false
            }
        }
        holder.itemView.setOnClickListener { view ->
            mNewPos = holder.adapterPosition//新位置
            mOldPos = mNewPos
            notifyDataSetChanged()
        }
        if (holder.adapterPosition == mNewPos) {
            holder.getView<TextView>(R.id.tv_index).isEnabled = true
            holder.getView<LinearLayout>(R.id.ll_root).isEnabled = true
        } else {
            holder.getView<TextView>(R.id.tv_index).isEnabled = false
            holder.getView<LinearLayout>(R.id.ll_root).isEnabled = false
        }
        if (mNewPos != mOldPos) {
            holder.getView<TextView>(R.id.tv_index).isEnabled = false
            holder.getView<LinearLayout>(R.id.ll_root).isEnabled = false
        }
        if (holder.adapterPosition == itemCount - 1) {
            holder.setVisible(R.id.space, true)
        } else {
            holder.setVisible(R.id.space, false)
        }
    }


}
