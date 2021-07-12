package com.engineer.imitate.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.engineer.imitate.R
import com.engineer.imitate.ui.widget.DecorationGod
import com.engineer.imitate.ui.widget.DecorationOne
import com.engineer.imitate.util.dp2px
import com.list.rados.fast_list.FastListAdapter
import com.list.rados.fast_list.bind
import com.skydoves.transformationlayout.TransformationLayout
import com.skydoves.transformationlayout.onTransformationEndContainer
import kotlinx.android.synthetic.main.activity_horizontal_list.*
import kotlinx.android.synthetic.main.view_item_h.view.*

class HorizontalListActivity : AppCompatActivity() {

    private lateinit var adapter: FastListAdapter<String>

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        intent.getParcelableExtra<TransformationLayout.Params>("TransformationParams")?.let {
            onTransformationEndContainer(it)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horizontal_list)
        val datas = initData()

        adapter = list00.bind(datas, R.layout.view_item_h_card) { value: String, _: Int ->
            setOnClickListener {
                val pos = datas.indexOf(value)
                Toast.makeText(context, "pos $pos", Toast.LENGTH_SHORT).show()
                list.smoothScrollToPosition(pos + 1)
            }
            desc.text = value
            path.text = "pos " + datas.indexOf(value)

        }.layoutManager(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false))

        adapter = list0.bind(datas, R.layout.view_item_h_card) { value: String, _: Int ->
            setOnClickListener {
                val pos = datas.indexOf(value)
                Toast.makeText(context, "pos $pos", Toast.LENGTH_SHORT).show()
                list.smoothScrollToPosition(pos + 1)
            }
            desc.text = value
            path.text = "pos " + datas.indexOf(value)

        }.layoutManager(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false))



        adapter = list.bind(datas, R.layout.view_item_h_square) { value: String, _: Int ->
            setOnClickListener {
                val pos = datas.indexOf(value)
                Toast.makeText(context, "pos $pos", Toast.LENGTH_SHORT).show()
                list.smoothScrollToPosition(pos + 1)
            }
            desc.text = value
            path.text = "pos " + datas.indexOf(value)

        }.layoutManager(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false))
        list.addItemDecoration(DividerItemDecoration(this, RecyclerView.HORIZONTAL))


        list2.bind(datas, R.layout.view_item_h) { value: String, _: Int ->
            setOnClickListener {
                val pos = datas.indexOf(value)
                Toast.makeText(context, "pos $pos", Toast.LENGTH_SHORT).show()
                val xy = IntArray(2)
                getLocationOnScreen(xy)
                val dest = xy[0] - dp2px(24f)
                Log.e(TAG, "dest==: $dest")
                if (dest > 0) {
                    list2.smoothScrollBy(dest.toInt(), 0)
                }
            }
            desc.text = value
            path.text = "pos " + datas.indexOf(value)

        }.layoutManager(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false))
        list2.addItemDecoration(DecorationOne(this))
        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(list2)


        list3.bind(datas, R.layout.view_item_h_image) { value: String, _: Int ->
            desc.text = value

        }.layoutManager(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false))
        list3.addItemDecoration(DecorationGod(this))
    }

    override fun onResume() {
        super.onResume()
        val result = true
    }

    private fun initData(): ArrayList<String> {
        val datas = ArrayList<String>()
        for (i in 0 until 6) {
            datas.add("item $i")
        }
        return datas
    }
}
