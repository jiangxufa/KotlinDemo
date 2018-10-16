package com.jiangxufa.kotlindemo

import android.graphics.Outline
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log.d
import android.view.View
import android.view.ViewOutlineProvider
import com.jiangxufa.baselibrary.ext.onClick
import com.jiangxufa.baselibrary.widgets.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.act_main.*

/**
 * 创建时间：2018/9/10
 * 编写人：lenovo
 * 功能描述：
 */
class MainActivity:AppCompatActivity(){

    object App{
        class Person(var name:String)
        fun instance() = Person("啦啦啦")
    }

    val TAG = "QQQQQQ"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_main)
        d(TAG,App.toString())
        d(TAG,App.toString())
        d(TAG,App.instance().toString())
        d(TAG,App.instance().toString())


        var d = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                //修改outLine的形状，这里是设置分别设置左上右下，以及Radius
                outline.setRoundRect(0,20,view.width,view.height,10f)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tv.outlineProvider = d
        }

//        supportFragmentManager.beginTransaction()
//                .add(R.id.container, BottomSheetDialogFragment.newInstance()).commit()

        "asa".apply {  }

        with(container){
            onClick {
                val fragment = BottomSheetDialogFragment.newInstance()
                fragment.show(supportFragmentManager, fragment.getTag())
            }
        }
    }



}