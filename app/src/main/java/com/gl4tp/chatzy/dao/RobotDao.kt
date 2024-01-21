package com.gl4tp.chatzy.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gl4tp.chatzy.models.Robot
import kotlinx.coroutines.flow.Flow


@Dao
interface RobotDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRobot(robot : Robot): Long

    @Query("SELECT * FROM Robot ")
    fun getRobotList (): Flow<List<Robot>>

    @Update()
    suspend fun updateRobot(robot : Robot): Int

    @Query("DELETE FROM Robot WHERE robotId == :robotId")
    suspend fun deleteRobotUsingId (robotId: String): Int

    @Query("DELETE FROM Chat WHERE robotId == :robotId")
    suspend fun deleteChatUsingRobotId (robotId: String)





}