package com.gl4tp.chatzy.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.gl4tp.chatzy.R
import com.gl4tp.chatzy.adapters.RobotAdapter
import com.gl4tp.chatzy.models.Robot
import com.gl4tp.chatzy.utils.Status
import com.gl4tp.chatzy.utils.StatusResult
import com.gl4tp.chatzy.utils.gone
import com.gl4tp.chatzy.utils.longToastShow
import com.gl4tp.chatzy.utils.robotImageList
import com.gl4tp.chatzy.viewModels.RobotViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * A simple [Fragment] subclass.
 * Use the [RobotListScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RobotListScreenFragment : Fragment() {


    private val robotViewModel : RobotViewModel by lazy {
        ViewModelProvider(this) [RobotViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_robot_list_screen, container, false)

       val toolbarview = view.findViewById<View>(R.id.toolbarLayout)
        val robotsImageLL= toolbarview.findViewById<View>(R.id.robotImageLL)
        robotsImageLL.gone()
        val closeimage =toolbarview.findViewById<ImageView>(R.id.backImg)

        closeimage.setOnClickListener {
            findNavController().navigateUp()
        }
        val titleTxt= toolbarview.findViewById<TextView>(R.id.titleTxt)
        titleTxt.text= "Chatzy App"
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       val addRobotFabBtn = view.findViewById<ExtendedFloatingActionButton>(R.id.addRobotFabBtn).apply{
            startAnimation(
                AnimationUtils.loadAnimation(view.context,R.anim.zoom_in_cut)
            )
        }
        addRobotFabBtn.setOnClickListener{
            addRobotDialog(it)
        }
        //val action =RobotListScreenFragmentDirections.actionRobotListScreenFragmentToChatScreenFragment()
        //findNavController().navigate(action)
        val robotAdapter = RobotAdapter{
            type, position, robot ->
            when(type){
                "delete"->{
                    robotViewModel.deleteRobotUsingId(robot.robotId)
                }
                "update"->{
                    updateRobotDialog(view,robot)
                }
                else ->{
                    val action = RobotListScreenFragmentDirections.actionRobotListScreenFragmentToChatScreenFragment(
                        robot.robotId,
                        robot.robotImg,
                        robot.robotName
                        )
                    findNavController().navigate(action)

                }
            }
        }
        val robotRV = view.findViewById<RecyclerView>(R.id.robotRV)
        robotRV.adapter = robotAdapter
        robotAdapter.registerAdapterDataObserver(
            object : RecyclerView.AdapterDataObserver(){
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    super.onItemRangeInserted(positionStart, itemCount)
                    robotRV.smoothScrollToPosition(positionStart)
                }
            }
        )
        callGetRobotList(robotAdapter,view)
        robotViewModel.getRobotList()
        statusCallback(view)

    }

    private fun callGetRobotList(robotAdapter: RobotAdapter, view: View) {
        CoroutineScope(Dispatchers.Main).launch {
            robotViewModel
                .robotStateFlow
                .collectLatest {
                    when (it.status){
                        Status.LOADING ->{}
                        Status.SUCCESS ->{

                            it.data?.collect(){
                                robotList -> robotAdapter.submitList(robotList)
                            }

                        }
                        Status.ERROR -> {it.message?.let { it1->view.context.longToastShow(it1) }}

                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        robotViewModel.clearStatusLiveData()
    }
    private fun statusCallback(view : View){
        robotViewModel
            .statusLiveData
            .observe(viewLifecycleOwner)
            {

                if (it != null) {
                    when (it.status) {
                        Status.LOADING -> {

                        }

                        Status.SUCCESS -> {
                            when (it.data as StatusResult) {
                                StatusResult.Added -> {
                                    Log.d("StatusResult", "Added")
                                }

                                StatusResult.Updated -> {
                                    Log.d("StatusResult", "Updated")
                                }

                                StatusResult.Deleted -> {
                                    Log.d("StatusResult", "Deleted")
                                }
                            }
                            it.message?.let { it1 -> view.context.longToastShow(it1) }
                        }

                        Status.ERROR -> {

                            it.message?.let { it1 -> view.context.longToastShow(it1) }
                        }
                    }
                }
            }
    }
    private fun addRobotDialog(view: View){

        val edRobotName = TextInputEditText(view.context)
        edRobotName.hint = "Enter Robot Name"

        edRobotName.maxLines = 3

        val textInputLayout = TextInputLayout(view.context)
        val container = FrameLayout(view.context)
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT

        )

        params.setMargins(50,30,50,30)
        textInputLayout.addView(edRobotName)
        container.addView(textInputLayout)

        MaterialAlertDialogBuilder(view.context)
            .setTitle("Add a new Robot")
            .setView(container)
            .setCancelable(false)
            .setPositiveButton("Add"){ dialog, which ->
                val robotName = edRobotName.text.toString().trim()
                if(robotName.isNotEmpty()){
                    robotViewModel.insertRobot(
                        Robot (
                            UUID.randomUUID().toString(),
                            robotName,
                            (robotImageList.indices).random()
                        )
                    )
                }

                else {

                    view.context.longToastShow("Required")
                }

            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }
    private fun updateRobotDialog(view: View,robot:Robot){

        val edRobotName = TextInputEditText(view.context)
        edRobotName.hint = "Enter Robot Name"

        edRobotName.maxLines = 3

        val textInputLayout = TextInputLayout(view.context)
        val container = FrameLayout(view.context)
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT

        )

        params.setMargins(50,30,50,30)
        textInputLayout.addView(edRobotName)
        container.addView(textInputLayout)

        MaterialAlertDialogBuilder(view.context)
            .setTitle("Update Robot")
            .setView(container)
            .setCancelable(false)
            .setPositiveButton("Update"){ dialog, which ->
                val robotName = edRobotName.text.toString().trim()
                if(robotName.isNotEmpty()){
                    robotViewModel.updateRobot(
                        Robot (
                            robot.robotId,
                            robotName,
                            robot.robotImg
                        )
                    )
                }

                else {

                    view.context.longToastShow("Required")
                }

            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
        edRobotName.setText(robot.robotName)
    }



}