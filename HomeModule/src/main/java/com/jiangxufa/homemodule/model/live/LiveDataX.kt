package com.jiangxufa.homemodule.model.live

/**
 * 创建时间：2018/10/15
 * 编写人：lenovo
 * 功能描述：
 */
data class LiveDataX(var title: String,
                     var url: String,
                     var count:String,
                     var bodybeans: List<LiveRecommend.RecommendDataBean.LivesBean> = listOf(),
                     var lives: List<LivePartition.PartitionsBean.LivesBean> = listOf(),
                     var isLast:Boolean = false)