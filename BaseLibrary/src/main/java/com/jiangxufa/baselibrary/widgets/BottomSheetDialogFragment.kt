package com.jiangxufa.baselibrary.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jiangxufa.baselibrary.R


/**
 * 创建时间：2018/9/10
 * 编写人：lenovo
 * 功能描述：
 * 调用：
 *   with(container){
        onClick {
            val fragment = BottomSheetDialogFragment.newInstance()
            fragment.show(supportFragmentManager, fragment.getTag())
        }
     }
 */
class BottomSheetDialogFragment : android.support.design.widget.BottomSheetDialogFragment() {

    companion object {
        fun newInstance(): BottomSheetDialogFragment {
            val args = Bundle()
            val fragment = BottomSheetDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_sheet_modal, container, false)
        return view
    }
}