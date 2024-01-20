package com.gl4tp.chatzy.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.gl4tp.chatzy.R
import com.gl4tp.chatzy.utils.gone

/**
 * A simple [Fragment] subclass.
 * Use the [SelectTextFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelectTextFragment : Fragment() {
    private val selectArgs: SelectTextFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_select_text_screen, container, false)
        val toolbarview = view.findViewById<View>(R.id.toolbarLayout)
        val robotsImageLL= toolbarview.findViewById<View>(R.id.robotImageLL)
        robotsImageLL.gone()
        val closeimage =toolbarview.findViewById<ImageView>(R.id.backImg)

        closeimage.setImageResource(R.drawable.ic_close)
        closeimage.setOnClickListener {
            findNavController().navigateUp()
        }
        val titleTxt= toolbarview.findViewById<TextView>(R.id.titleTxt)
        titleTxt.text= "select text"

        val selectTxt= view.findViewById<TextView>(R.id.selectTxt)
        selectTxt.text= selectArgs.selectMessage

       /* val moveChatBtn = view.findViewById<Button>(R.id.moveChat).apply{
            startAnimation(
                AnimationUtils.loadAnimation(view.context,R.anim.zoom_in_cut)
            )
        }
        moveChatBtn.setOnClickListener{
            val action =RobotListScreenFragmentDirections.actionRobotListScreenFragmentToChatScreenFragment()
            findNavController().navigate(action)
        }*/
        return view
    }



}