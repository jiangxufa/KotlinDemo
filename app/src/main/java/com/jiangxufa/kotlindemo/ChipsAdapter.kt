package com.jiangxufa.kotlindemo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

public class ChipsAdapter : RecyclerView.Adapter<ChipsAdapter.ViewHolder> {

    private var chipsEntities: List<SpuItem>? = null
    private var onRemoveListener: OnRemoveListener? = null
    private var isShowingPosition: Boolean = false

    constructor(chipsEntities: List<SpuItem>, onRemoveListener: OnRemoveListener) {
        this.chipsEntities = chipsEntities
        this.onRemoveListener = onRemoveListener
    }

    constructor(chipsEntities: List<SpuItem>, onRemoveListener: OnRemoveListener, isShowingPosition: Boolean) {
        this.chipsEntities = chipsEntities
        this.onRemoveListener = onRemoveListener
        this.isShowingPosition = isShowingPosition
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_chip, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(chipsEntities!![position])
    }

    override fun getItemCount(): Int {
        return chipsEntities!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvDescription: TextView
        private val ivPhoto: ImageView
        private val tvName: TextView
        private val ibClose: ImageButton
        private val tvPosition: TextView

        init {
            tvDescription = itemView.findViewById(R.id.tvDescription)
            ivPhoto = itemView.findViewById(R.id.ivPhoto)
            tvName = itemView.findViewById(R.id.tvName)
            ibClose = itemView.findViewById(R.id.ibClose)
            tvPosition = itemView.findViewById(R.id.tvPosition)
        }

        fun bindItem(entity: SpuItem) {
            itemView.tag = entity.name


            tvName.text = entity.name

            if (isShowingPosition) {
                tvPosition.text = adapterPosition.toString()
            }

            ibClose.setOnClickListener {
                if (onRemoveListener != null && adapterPosition != -1) {
                    onRemoveListener!!.onItemRemoved(adapterPosition)
                }
            }
        }
    }

}
