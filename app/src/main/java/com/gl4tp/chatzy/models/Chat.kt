package com.gl4tp.chatzy.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.gl4tp.chatzy.response.Message
import java.util.Date

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Robot::class,
            parentColumns = arrayOf("robotId"),
            childColumns = arrayOf("robotId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Chat(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name ="chatId")
    val chatId : String,
    @Embedded
    val message : Message,
    val robotId : String,
    val date: Date
)
