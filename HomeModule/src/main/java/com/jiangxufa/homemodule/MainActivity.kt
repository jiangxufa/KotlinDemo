package com.jiangxufa.homemodule

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import com.blankj.utilcode.util.SnackbarUtils
import com.jiangxufa.baselibrary.ext.addFragment
import com.jiangxufa.baselibrary.utils.AppUtils
import com.jiangxufa.baselibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StatusBarUtil.setColorNoTranslucent(this, AppUtils.getColor(R.color.colorPrimary))

        if (savedInstanceState == null) {
            addFragment(HomeFragment.instance(),R.id.fl_content)
        }
    }

    //属性代理 监听属性变化
    private var mBackPressedTime by Delegates.observable(0L) { _: KProperty<*>, old: Long, new: Long ->
        if (new - old > 1000) {
            SnackbarUtils.with(fl_content).setMessage( "再按一下退出").show()
        }
        if (new - old in 1..1000) {
            finish()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mBackPressedTime =  System.currentTimeMillis()
        }
        return true
    }
}
