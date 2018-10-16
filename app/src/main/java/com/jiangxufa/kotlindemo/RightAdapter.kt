package com.jiangxufa.kotlindemo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_spu.view.*

/**
 * 创建时间：2018/9/10
 * 编写人：lenovo
 * 功能描述：
 */
class RightAdapter : RecyclerView.Adapter<RightAdapter.ViewHold>() {

    lateinit var data: List<Spu>
    var currentPosition: Int = 0

    fun setData1(list: List<Spu>) {
        data = list
    }

    fun showIndex(position: Int) {
        currentPosition = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_spu, parent, false)
        return ViewHold(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.itemView.tvSpu.text = data.get(currentPosition).spuItems.get(position).name
        if (listener != null) {
            holder.itemView.setOnClickListener(listener)
        }
    }


    public class ViewHold(view: View) : RecyclerView.ViewHolder(view)

    private var listener: View.OnClickListener ?= null


    fun setOnItemClickListener(listener: View.OnClickListener) {
        this.listener = listener
    }

}