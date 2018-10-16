package com.jiangxufa.kotlindemo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_chip.view.*

/**
 * 创建时间：2018/9/10
 * 编写人：lenovo
 * 功能描述：
 */
class TopAdapter: RecyclerView.Adapter<TopAdapter.ViewHold>() {

    var  data:ArrayList<SpuItem> = Data.createSpuItemData(1)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chip, parent, false)
        return ViewHold(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.itemView.tvName.text = data.get(position).name
        holder.itemView.tag = position
        if (listener != null) {
            holder.itemView.setOnClickListener(listener)
        }
    }


    public class ViewHold(view: View): RecyclerView.ViewHolder(view)

    private var listener:View.OnClickListener ?= null

    fun setOnItemClickListener(listener:View.OnClickListener) {
        this.listener = listener
    }

}