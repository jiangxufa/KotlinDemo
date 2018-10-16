package com.jiangxufa.kotlindemo

import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.act_swipe.*

/**
 * 创建时间：2018/9/26
 * 编写人：lenovo
 * 功能描述：
 */
class SwipeRefreshActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_swipe)
        //改变加载显示的颜色
//        srlRefresh.setColorSchemeColors(
//                context.getResources().getColor(R.color.green_one),
//                context.getResources().getColor(R.color.green_two),
//                context.getResources().getColor(R.color.green_three))
        //设置背景颜色
        srlRefresh.setBackgroundColor(Color.YELLOW)
        //设置初始时的大小
        srlRefresh.setSize(SwipeRefreshLayout.LARGE)
        //设置监听
//        srlRefresh.setOnRefreshListener(listener)
        //设置向下拉多少出现刷新
        srlRefresh.setDistanceToTriggerSync(100)
        //设置刷新出现的位置
        srlRefresh.setProgressViewEndTarget(true, 200)


        srlRefresh.isRefreshing = true
//        srlRefresh.post { srlRefresh.isRefreshing = true }
    }

}