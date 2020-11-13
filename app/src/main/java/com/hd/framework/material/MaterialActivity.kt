package com.hd.framework.material

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.ListAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.textview.MaterialTextView
import com.google.android.material.transition.MaterialFadeThrough
import com.hd.framework.R
import com.hd.framework.databinding.FMyBinding
import com.xhh.framework.vm.helper.AppHelper
import com.xhh.ui.fragment.DataBindingFragment
import com.xhh.ui.utils.dp
import kotlinx.android.synthetic.main.activity_material.*


/**
 *   @Author:小灰灰
 *   @Time:2020/10/31
 *   @Desc:
 */
class MaterialActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material)
        AppHelper.write("11" to "222")
        Log.e("==",AppHelper.read("11",""))
//        MaterialAlertDialogBuilder(this)
//            .setBackground(MaterialShapeDrawable.createWithElevationOverlay(this).apply {
//                this.setCornerSize(8.dp)
//            })
//            .setCustomTitle(MaterialTextView(this).apply {
//                text = "斌提"
//                textSize = 18f
//                gravity = Gravity.CENTER
//                setTextColor(Color.BLACK)
//                setPadding(0, 16.dp.toInt(),0, 16.dp.toInt())
//            })
//            .setCancelable(false)
//            .setItems(arrayOf("1","2","3")
//            ) { dialog, which ->
//
//            }
//
//            .setView(R.layout.dialog_material)
//            .show()

        supportFragmentManager.commit {
            replace(container.id,AFrgment.getInstance())
        }

    }
}
class AFrgment:Fragment(){
    companion object{
        fun getInstance():AFrgment = AFrgment()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f_first,container,false)
    }
}

class BFragment:Fragment(){
    companion object{
        fun getInstance():BFragment = BFragment()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return MaterialTextView(requireContext()).apply {
            text  = "B"
            setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
            setBackgroundResource(android.R.color.holo_orange_light)
        }
    }


}
class MyFragment: DataBindingFragment<FMyBinding>() {
    companion object{
        fun getInstance() = MyFragment()
    }

    override val layoutId: Int
        get() = R.layout.f_my


    override fun onDataBindingCreated(dataBinding: ViewDataBinding) {

    }


}