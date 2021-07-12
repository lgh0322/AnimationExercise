package com.engineer.imitate.ui.activity.fragmentmanager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.engineer.imitate.R
import com.engineer.imitate.util.dp
import kotlinx.android.synthetic.main.fragment_blink.*

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlinkFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlinkFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var toggle = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blink, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text.text = param1?.plus(param2)
        image.setOnClickListener {
            Log.e("BlinkFragment", "onViewCreated: 1 " + nestedScrollview.canScrollVertically(1))
            Log.e("BlinkFragment", "onViewCreated: -1" + nestedScrollview.canScrollVertically(-1))
            if (nestedScrollview.canScrollVertically(-1)) {
                nestedScrollview.scrollTo(0, 0)
            } else {
                nestedScrollview.scrollTo(0, 10000)
            }
        }
        nestedScrollview.setOnClickListener {
            val p = nestedScrollview.layoutParams
            if (toggle) {
                p.height = ViewGroup.LayoutParams.MATCH_PARENT
            } else {
                p.height = 100.dp
            }
            nestedScrollview.layoutParams = p
            toggle = !toggle
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlinkFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BlinkFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}