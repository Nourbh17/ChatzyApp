package com.gl4tp.chatzy.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.gl4tp.chatzy.models.Robot


@Dao
interface RobotDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRobot(robot : Robot): Long



}