package com.gl4tp.chatzy.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gl4tp.chatzy.database.chatzyDatabase
import com.gl4tp.chatzy.models.Robot
import com.gl4tp.chatzy.utils.Resource
import com.gl4tp.chatzy.utils.StatusResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


class RobotRepository (application: Application){

    private val robotDao = chatzyDatabase.getInstance(application).robotDao
    private val _statusLiveData = MutableLiveData<Resource<StatusResult>>()

    val statusLiveData : LiveData<Resource<StatusResult>>
        get() = _statusLiveData

    fun insertRobot(robot : Robot){
        try{
            _statusLiveData.postValue(Resource.Loading())
            CoroutineScope(Dispatchers.IO).launch {
                val result = robotDao.insertRobot(robot)
                handleResult(result.toInt(), "Inserted Robot Successfully" , StatusResult.Added)

            }

        }

        catch (e: Exception) {
            val errorResource = Resource.Error<StatusResult>(e.message ?: "Unknown error")
            _statusLiveData.postValue(errorResource)
        }

    }

    private fun handleResult(result: Int, message: String, statusResult: StatusResult) {

        if(result != -1){

            _statusLiveData.postValue(Resource.Success(statusResult, message))

        }
        else {
            val errorResource = Resource.Error<StatusResult>("Something went wrong")
            _statusLiveData.postValue(errorResource)
        }

    }


}