package com.gl4tp.chatzy.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import androidx.room.TypeConverters
import com.gl4tp.chatzy.conveters.TypeConverter
import com.gl4tp.chatzy.dao.ChatDao
import com.gl4tp.chatzy.models.Chat
import com.gl4tp.chatzy.network.ApiInterface
import com.gl4tp.chatzy.utils.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Database(
    entities = [Chat::class],
    version= 1,
    exportSchema = false
)
@TypeConverters(TypeConverter::class)
abstract class chatzyDatabase : RoomDatabase() {
    abstract val chatdao: ChatDao

    companion object {
        @Volatile

        private var INSTANCE: chatzyDatabase? = null





        fun getInstance (context: Context): chatzyDatabase {


            synchronized(this) {
                return INSTANCE?: Room.databaseBuilder(
                    context.applicationContext,
                    chatzyDatabase::class.java,
                    "chatzy_db"
                )
                    .build().also { INSTANCE =it }


            }
    }
}}





