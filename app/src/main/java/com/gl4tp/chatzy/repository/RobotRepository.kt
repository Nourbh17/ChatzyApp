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
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception


class RobotRepository (application: Application){

    private val robotDao = chatzyDatabase.getInstance(application).robotDao
    private val _robotStateFlow = MutableStateFlow<Resource<Flow<List<Robot>>>>(Resource.Loading())
    val robotStateFlow : StateFlow<Resource<Flow<List<Robot>>>>
        get() = _robotStateFlow

    private val _statusLiveData = MutableLiveData<Resource<StatusResult>?>()

    val statusLiveData : LiveData<Resource<StatusResult>?>
        get() = _statusLiveData

    fun clearStatusLiveData(){
        _statusLiveData.value=null

    }
    fun getRobotList(){
        CoroutineScope(Dispatchers.IO).launch{
            try{
                _robotStateFlow.emit(Resource.Loading())
                val result = robotDao.getRobotList()
                _robotStateFlow.emit(Resource.Success(result))

            }catch (e: Exception) {
                e.printStackTrace()
                val errorResource = Resource.Error<Flow<List<Robot>>>(e.message ?: "Unknown error")
                _robotStateFlow.emit(errorResource)
                //_robotStateFlow.emit(Error(e.message.toString()))
            }

        }
    }


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

    fun deleteRobotUsingId (robotId : String){
        try{
            _statusLiveData.postValue(Resource.Loading())
            CoroutineScope(Dispatchers.IO).launch {
                async {
                    robotDao.deleteChatUsingRobotId(robotId)
                }.await()

                val result =async {
                    robotDao.deleteRobotUsingId(robotId)
                }.await()

                handleResult(result, "Deleted Robot and Chat Successfully" , StatusResult.Deleted)

            }

        }
        catch (e: Exception) {
            val errorResource = Resource.Error<StatusResult>(e.message ?: "Unknown error")
            _statusLiveData.postValue(errorResource)
        }

    }

    fun updateRobotUsingId (robot : Robot){
        try{
            _statusLiveData.postValue(Resource.Loading())
            CoroutineScope(Dispatchers.IO).launch {

                val result = robotDao.updateRobot(robot)
                handleResult(result, "Updated Robot Successfully" , StatusResult.Updated)

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