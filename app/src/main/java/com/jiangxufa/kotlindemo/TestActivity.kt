package com.jiangxufa.kotlindemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.act_test.*
import java.util.concurrent.Callable

/**
 * 创建时间：2018/9/10
 * 编写人：lenovo
 * 功能描述：
 */
class TestActivity : AppCompatActivity() {

   lateinit var topAdapter:TopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_test)

        val leftAdapter = LeftAdapter()
        with(rcLeft) {
            layoutManager = LinearLayoutManager(this@TestActivity.baseContext)
            adapter = leftAdapter
            (adapter as LeftAdapter).setOnItemClickListener(View.OnClickListener { view ->
                var position = view.tag as Int
                (rcRight.adapter as RightAdapter).showIndex(position)
            })
        }

        val rightAdapter = RightAdapter()
        with(rcRight) {
            layoutManager = LinearLayoutManager(this@TestActivity.baseContext)
            adapter = rightAdapter
            (adapter as RightAdapter).setData1((rcLeft.adapter as LeftAdapter).data)
        }


        topAdapter = TopAdapter()
        with(rcTop) {
            //            layoutManager = ChipsLayoutManager.newBuilder(context!!)
//                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
//                    .build()
            layoutManager = object : LinearLayoutManager(this@TestActivity.baseContext) {
//                override fun onMeasure(recycler: RecyclerView.Recycler?, state: RecyclerView.State?, widthSpec: Int, heightSpec: Int) {
//                    d("LinearLayoutManager", childCount.toString())
//                    if (topAdapter.data.size > 3 && childCount > 0) {
//                        val firstChildView = recycler!!.getViewForPosition(0)
//                        measureChild(firstChildView, widthSpec, heightSpec)
//                        setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), firstChildView.measuredHeight * 3)
//                    } else {
//                        super.onMeasure(recycler, state, widthSpec, heightSpec)
//                    }
//                }
            }
            adapter = topAdapter
        }

        topAdapter.setOnItemClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                topAdapter.data.removeAt(topAdapter.data.size - 1)
                refreshTop()
                topAdapter.notifyDataSetChanged()
//                leftAdapter.notifyDataSetChanged()
//                rightAdapter.notifyDataSetChanged()
            }

        })

//        var ft = FutureTask<String>(CallableTest())
//        var thread = Thread(ft)
//
//        thread.suspend()
//        thread.start()
    }

    override fun onResume() {
        super.onResume()
        refreshTop()
    }

    private fun refreshTop() {
        if (topAdapter.data.size > 2) {
            val layoutParams = flContainer.layoutParams
            layoutParams.height = 150
            flContainer.layoutParams = layoutParams
        } else {
            val layoutParams = flContainer.layoutParams
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            flContainer.layoutParams = layoutParams
        }
    }

    class CallableTest : Callable<String> {
        override fun call(): String {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
}