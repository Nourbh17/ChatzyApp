package com.gl4tp.chatzy.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gl4tp.chatzy.response.Message
import java.util.Date

@Entity()

data class Robot(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name ="robotId")

    val robotId : String,

    val robotName : String,

    val robotImg: Int
)