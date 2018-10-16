package com.jiangxufa.homemodule

import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.ViewSwitcher
import com.jiangxufa.baselibrary.common.BaseFragment
import com.jiangxufa.baselibrary.common.BasePresenter
import com.jiangxufa.baselibrary.common.BaseView
import kotlinx.android.synthetic.main.fragment_switch.*

/**
 * 创建时间：2018/10/8
 * 编写人：lenovo
 * 功能描述：
 */
class SwitchFragment() : BaseFragment<BaseView, BasePresenter<BaseView>>() {

    companion object {
        fun instance(): Fragment = SwitchFragment()
    }

    private var mFactory = object : ViewSwitcher.ViewFactory {
        override fun makeView(): View {
            var t = TextView(mContext)
            t.gravity = Gravity.TOP
            t.gravity = Gravity.CENTER_HORIZONTAL
            t.setTextAppearance(mContext, android.R.style.TextAppearance_Large)
            return t
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_switch

    override fun initView() {
        switcher.setFactory(mFactory)
        var inA = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in)
        var outA = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out)
        with(switcher) {
            inAnimation = inA
            outAnimation = outA
        }

        button.setOnClickListener {
            mCounter++
            switcher.setText("$mCounter 个");
        }
    }

    var mCounter  = 0
    override fun initData() {
    }


}