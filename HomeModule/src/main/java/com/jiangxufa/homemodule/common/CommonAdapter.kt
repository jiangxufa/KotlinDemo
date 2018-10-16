package com.jiangxufa.homemodule.common

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.chad.library.adapter.base.BaseViewHolder

/**
 * 创建时间：2018/10/10
 * 编写人：lenovo
 * 功能描述：
 */
abstract class CommonAdapter<T>(var layout: Int, var context: Context, var layoutHelper: LayoutHelper, var count: Int = -1) : DelegateAdapter.Adapter<BaseViewHolder>() {

    protected var datas: List<T> = ArrayList()
    protected var data: T? = null

    fun setNewData(datas: List<T>) {
        this.datas = datas
        notifyDataSetChanged()
    }

    fun setNewSingleData(data: T ) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(LayoutInflater.from(context).inflate(layout, parent, false))
    }

    override fun onCreateLayoutHelper(): LayoutHelper = layoutHelper

    override fun getItemCount(): Int {
        if (datas.isEmpty()) {
            if (count == -1) {
                return 0
            } else {
            }
            return count
        } else {
            return datas.size
        }
    }

}