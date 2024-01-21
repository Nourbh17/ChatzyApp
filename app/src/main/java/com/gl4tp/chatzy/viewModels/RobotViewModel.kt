package com.gl4tp.chatzy.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.gl4tp.chatzy.models.Robot
import com.gl4tp.chatzy.repository.RobotRepository


class RobotViewModel(application: Application): AndroidViewModel(application) {

    private  val robotRepository = RobotRepository(application)

    val statusLiveData get() = robotRepository.statusLiveData
    val robotStateFlow get() = robotRepository.robotStateFlow

    fun insertRobot(robot:Robot){
        robotRepository.insertRobot(robot)
    }

    fun getRobotList(){
        robotRepository.getRobotList()
    }

    fun deleteRobotUsingId(robotId : String){
        robotRepository.deleteRobotUsingId(robotId)
    }
    fun updateRobot (robot : Robot){
        robotRepository.updateRobotUsingId(robot)
    }
    fun clearStatusLiveData (){
        robotRepository.clearStatusLiveData()
    }


}