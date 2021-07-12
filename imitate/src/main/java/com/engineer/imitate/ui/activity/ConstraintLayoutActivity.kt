package com.engineer.imitate.ui.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.engineer.imitate.R
import com.engineer.imitate.ui.list.adapter.LargeImageAdapter
import com.engineer.imitate.ui.widget.more.DZStickyNavLayouts
import com.engineer.imitate.util.hide
import com.engineer.imitate.util.show
import kotlinx.android.synthetic.main.activity_constraint_layout.*

class ConstraintLayoutActivity : AppCompatActivity() {
    val tag = "ConstraintLayout"

    private lateinit var layoutParams: FrameLayout.LayoutParams
    private val globalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            root_content?.let {
                val displayRect = Rect()
                it.getWindowVisibleDisplayFrame(displayRect)
                Log.e(tag, "${displayRect.top}")
                Log.e(tag, "${displayRect.right}")
                Log.e(tag, "${displayRect.bottom}")
                Log.e(tag, "${displayRect.left}")

//                scrollView.scrollBy(0, -displayRect.bottom)
//                layoutParams.height = displayRect.bottom
//                layoutParams.bottomMargin = 1000
                it.layoutParams = layoutParams

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_layout)

        recyclerView_2.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView_2.adapter = LargeImageAdapter(getList())
        head_home_layout.setOnStartActivity(object : DZStickyNavLayouts.OnStartActivityListener {
            override fun onStart() {
                Toast.makeText(this@ConstraintLayoutActivity, "bingo", Toast.LENGTH_SHORT).show()
            }
        })

        var toggle = false
        anim.setOnClickListener {
            if (toggle) {
                image_alpha.alpha = 0.0f
            } else {
                image_alpha.alpha = 1.0f
            }
            toggle = !toggle
        }
        rotate_anim.setOnClickListener {
            rotate_anim.rotation = 0f
            rotateAnim(rotate_anim)
        }
        button_2.setOnClickListener {
            if (button_1.visibility == View.VISIBLE) {
                button_1.hide()
            } else {
                button_1.show()
            }
        }
    }

    private var rotation: ViewPropertyAnimator? = null
    private fun rotateAnim(view: View) {
        rotation = view.animate().rotation(90f).setStartDelay(400).setDuration(600)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    rotation = view.animate().rotation(0f).setStartDelay(400).setDuration(600)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                super.onAnimationEnd(animation)
                                rotation =
                                    view.animate().rotation(90f).setStartDelay(400).setDuration(600)
                                rotation?.start()
                            }
                        })
                    rotation?.start()
                }
            })
        rotation?.start()

    }

    // <editor-fold defaultstate="collapsed" desc="prepare datas">
    private fun getList(): MutableList<String> {
        val datas = ArrayList<String>()
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555581149653&di=5912dd2fe4db77ce303569b3e8f34d7b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201406%2F08%2F20140608161225_VYVEV.jpeg")
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555581149653&di=5912dd2fe4db77ce303569b3e8f34d7b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201406%2F08%2F20140608161225_VYVEV.jpeg")
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555581149653&di=5912dd2fe4db77ce303569b3e8f34d7b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201406%2F08%2F20140608161225_VYVEV.jpeg")
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555581149653&di=5912dd2fe4db77ce303569b3e8f34d7b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201406%2F08%2F20140608161225_VYVEV.jpeg")
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555581149653&di=5912dd2fe4db77ce303569b3e8f34d7b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201406%2F08%2F20140608161225_VYVEV.jpeg")
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555581149653&di=5912dd2fe4db77ce303569b3e8f34d7b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201406%2F08%2F20140608161225_VYVEV.jpeg")
        return datas
    }
    // </editor-fold>

    override fun onResume() {
        super.onResume()
        layoutParams = root_content.layoutParams as FrameLayout.LayoutParams
        root_content.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }

    override fun onPause() {
        super.onPause()
        root_content.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
    }
}
