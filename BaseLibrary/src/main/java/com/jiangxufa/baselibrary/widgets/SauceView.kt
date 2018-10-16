package com.jiangxufa.baselibrary.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.jiangxufa.baselibrary.R
import kotlinx.android.synthetic.main.layout_sauce_view.view.*

/**
 * 创建时间：2018/9/26
 * 编写人：lenovo
 * 功能描述：
 */
class SauceView  @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    private var isShowArrow: Boolean
    private var isShowImag: Boolean
    private var leftText: String?
    private var rightText: String?
    private var imageResourse: Int

    init {

        //获取自定义属性
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SauceView)

        isShowArrow = typedArray.getBoolean(R.styleable.SauceView_isShowArrow, true)
        isShowImag = typedArray.getBoolean(R.styleable.SauceView_isShowImage, true)
        imageResourse = typedArray.getResourceId(R.styleable.SauceView_image, -1)

        leftText = typedArray.getString(R.styleable.SauceView_leftContent)
        rightText = typedArray.getString(R.styleable.SauceView_rightContent)

        typedArray.recycle()

        inflate(getContext(), R.layout.layout_sauce_view, this)
        initView()
    }

    private fun initView() {
        with(ivImage) {
            if (imageResourse == -1)
                visibility = View.GONE
            else {
                visibility = View.VISIBLE
                setImageResource(imageResourse)
            }
        }

        tvLeft.text = leftText?:leftText
        tvRight.text = rightText?:rightText

        ivArrow.visibility = if (isShowArrow) View.VISIBLE else View.GONE

    }

}