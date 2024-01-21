package com.gl4tp.chatzy.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gl4tp.chatzy.response.Message
import java.util.Date

@Entity()
data class Chat(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name ="chatId")
    val chatId : String,
    @Embedded
    val message : Message,

    val date: Date
)
