package com.jiangxufa.homemodule

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.jiangxufa.baselibrary.common.BaseFragment
import com.jiangxufa.baselibrary.common.BasePresenter
import com.jiangxufa.baselibrary.common.BaseView
import com.jiangxufa.baselibrary.utils.AppUtils
import com.jiangxufa.homemodule.ui.home.*
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * 创建时间：2018/9/29
 * 编写人：lenovo
 * 功能描述：
 */
class HomeFragment : BaseFragment<BaseView, BasePresenter<BaseView>>() {
    companion object {
        fun instance(): Fragment = HomeFragment()
    }

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun initView() {
        with(vp) {
            offscreenPageLimit = 5
            adapter = HomePageAdapter(this@HomeFragment.childFragmentManager)
            tabs.setViewPager(this)
            currentItem = 1
        }
    }

    override fun initData() {
    }
}

class HomePageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var mTitle: Array<String>? = null
    private var mFragment: Array<Fragment?>

    init {
        mTitle = AppUtils.getStringArray(R.array.main_title)
        mFragment = arrayOfNulls(mTitle!!.size)
    }

    override fun getItem(position: Int): Fragment {
        if (mFragment[position] == null) {
            when (position) {
                //直播
                0 -> mFragment[position] = LiveDataFragment.instance()
                //推荐
                1 -> mFragment[position] = RecommendFragment.instance()
                //追番
                2 -> mFragment[position] = ChaseBangumiFragment.instance()
                //分区
                3 -> mFragment[position] = RegionFragment.instance()
                //动态
                4 -> mFragment[position] = DynamicFragment.instance()
                //发现
                5 -> mFragment[position] = DiscoverFragment.instance()
            }
        }
        return mFragment[position]!!
    }

    override fun getCount(): Int = mFragment.size

    override fun getPageTitle(position: Int): CharSequence? = mTitle!![position]
}