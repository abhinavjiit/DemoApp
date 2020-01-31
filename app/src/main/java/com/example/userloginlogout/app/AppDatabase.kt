package com.example.userloginlogout.app

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.userloginlogout.app.model.UserInfo
import com.example.userloginlogout.app.model.UsercheckInOrCheckOutTime


@Database(entities = [UserInfo::class, UsercheckInOrCheckOutTime::class], version = 7, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}