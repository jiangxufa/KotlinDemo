package com.jiangxufa.baselibrary.common

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class RLAdapter : BaseQuickAdapter<String, BaseViewHolder>(android.R.layout.simple_list_item_1) {
    override fun convert(helper: BaseViewHolder, item: String) {
        helper.setText(android.R.id.text1, item)
    }

}