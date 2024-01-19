package com.gl4tp.chatzy.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gl4tp.chatzy.R

/**
 * A simple [Fragment] subclass.
 * Use the [RobotListScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RobotListScreenFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_robot_list_screen, container, false)

        val moveChatBtn = view.findViewById<Button>(R.id.moveChat).apply{
            startAnimation(
                AnimationUtils.loadAnimation(view.context,R.anim.zoom_in_cut)
            )
        }
        moveChatBtn.setOnClickListener{
            val action =RobotListScreenFragmentDirections.actionRobotListScreenFragmentToChatScreenFragment()
            findNavController().navigate(action)
        }
        return view
    }



}