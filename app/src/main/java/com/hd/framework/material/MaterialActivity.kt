package com.hd.framework.material

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.ListAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.textview.MaterialTextView
import com.google.android.material.transition.MaterialFadeThrough
import com.hd.framework.R
import com.hd.framework.databinding.FMyBinding
import com.xhh.framework.vm.helper.AppHelper
import com.xhh.ui.ModuleHostActivity
import com.xhh.ui.databinding.DialogMaterialBinding
import com.xhh.ui.fragment.CoreFragment
import com.xhh.ui.fragment.DataBindingFragment
import com.xhh.ui.fragment.DialogListener
import com.xhh.ui.utils.dp
import kotlinx.android.synthetic.main.activity_material.*


/**
 *   @Author:小灰灰
 *   @Time:2020/10/31
 *   @Desc:
 */
class MaterialActivity : ModuleHostActivity() {
    override fun onCreateHostFragment(intent: Intent): Fragment {
        return AFrgment.getInstance()
    }

    override fun onCreateFragments(tag: String): Fragment {
        return when (tag) {
            "1" -> AFrgment.getInstance()
            "2" -> BFragment.getInstance()
            else -> throw Exception("==")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_material)


    }
}

class AFrgment : CoreFragment() {
    companion object {
        fun getInstance(): AFrgment = AFrgment()
    }

    override fun onViewRealCreated() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStartPageClick(v: View?) {
        navigate("2")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f_first, container, false).apply {
            setOnClickListener(this@AFrgment)

        }
    }
}

class BFragment : CoreFragment() {
    companion object {
        fun getInstance(): BFragment = BFragment()
    }

    override fun onViewRealCreated() {
        var dialog:AlertDialog?=null
        val dialogMaterialBinding = DataBindingUtil.inflate<DialogMaterialBinding>(layoutInflater,R.layout.dialog_material,null,false)
        dialogMaterialBinding.listener = object : DialogListener{
            override fun dismiss() {
                dialog?.dismiss()

            }

            override fun positive() {

            }

        }

        dialog = MaterialAlertDialogBuilder(requireContext())
            .setBackground(
                MaterialShapeDrawable.createWithElevationOverlay(requireContext()).apply {
                    this.setCornerSize(8.dp)
                })
            .setCustomTitle(MaterialTextView(requireContext()).apply {
                text = "斌提"
                textSize = 18f
                gravity = Gravity.CENTER
                setTextColor(Color.BLACK)
                setPadding(0, 16.dp.toInt(), 0, 0)
            })
            .setCancelable(false)

            .setView(dialogMaterialBinding.root)
            .create()
        dialog.show()


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStartPageClick(v: View?) {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return MaterialTextView(requireContext()).apply {
            text = "B"
            setOnClickListener {
                //requireActivity().supportFragmentManager.popBackStack()
            }
            setBackgroundResource(android.R.color.holo_orange_light)
        }
    }


}
