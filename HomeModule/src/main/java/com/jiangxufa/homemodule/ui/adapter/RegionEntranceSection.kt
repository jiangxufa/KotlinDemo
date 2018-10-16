package com.jiangxufa.homemodule.ui.adapter

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.jiangxufa.baselibrary.widgets.section.StatelessSection
import com.jiangxufa.baselibrary.widgets.section.ViewHolder
import com.jiangxufa.homemodule.R
import com.jiangxufa.homemodule.model.region.RegionEnter

/**
 * 创建时间：2018/10/11
 * 编写人：lenovo
 * 功能描述：
 */
class RegionEntranceSection(var grids: List<RegionEnter>) : StatelessSection<RegionEnter>(R.layout.layout_item_home_region_entrance1,
        R.layout.layout_empty, grids) {

    override fun onBindHeaderViewHolder(holder: ViewHolder) {
        val recyclerView = holder.getView<RecyclerView>(R.id.recycler)
        with(recyclerView) {
            setHasFixedSize(false)
            isNestedScrollingEnabled = false
            val mLayoutManager = GridLayoutManager(mContext, 4)
            layoutManager = mLayoutManager
            adapter = RegionEntranceAdapter(grids)
        }
    }

}

class RegionEntranceAdapter(grids: List<RegionEnter>) : BaseQuickAdapter<RegionEnter, BaseViewHolder>(
        R.layout.layout_item_home_region_entrance, grids) {

    override fun convert(helper: BaseViewHolder, item: RegionEnter) {
        helper.setText(R.id.tv_title, item.title)
                .setImageResource(R.id.iv_icon, item.img)
    }

}