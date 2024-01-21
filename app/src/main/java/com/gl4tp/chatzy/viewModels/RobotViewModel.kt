package com.gl4tp.chatzy.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.gl4tp.chatzy.models.Robot
import com.gl4tp.chatzy.repository.RobotRepository


class RobotViewModel(application: Application): AndroidViewModel(application) {

    private  val robotRepository = RobotRepository(application)

    val statusLiveData get() = robotRepository.statusLiveData
    fun insertRobot(robot:Robot){

        robotRepository.insertRobot(robot)
    }


}