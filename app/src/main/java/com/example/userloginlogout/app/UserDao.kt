package com.example.userloginlogout.app

import android.database.Cursor
import androidx.room.*
import com.example.userloginlogout.app.model.UserInfo
import com.example.userloginlogout.app.model.UsercheckInOrCheckOutTime

@Dao
interface UserDao {
    @Insert
    fun insert(userInfo: UserInfo)

    @Query("SELECT * FROM User_Detail WHERE employeeCode LIKE :employeeId AND password LIKE :pass ")
    suspend fun getDataAsync(employeeId: String, pass: String): List<UserInfo>

    @Delete
    suspend fun deleteUser(userInfo: UserInfo)

    @Update
    suspend fun updateUser(userInfo: UserInfo)

    @Query("UPDATE User_Detail SET checkIn = :checkInTime WHERE employeeCode=:employeeId")
    suspend fun updateCheckInTime(checkInTime: String, employeeId: String)

    @Query("UPDATE User_Detail SET checkOut = :checkOutTime WHERE employeeCode=:employeeId")
    suspend fun updateCheckOutTime(checkOutTime: String, employeeId: String)


    @Insert
    suspend fun insert(userCheckInOrCheckOutTime: UsercheckInOrCheckOutTime)

    @Query("SELECT * FROM User_CheckIn_CheckOut_Time_Log WHERE employeeCode LIKE :employeeId ")
    suspend fun getAllCheckInCheckOutTime(employeeId: String): List<UsercheckInOrCheckOutTime>

    @Query("SELECT * FROM User_CheckIn_CheckOut_Time_Log WHERE employeeCode LIKE :employeeId")
    fun getData(employeeId: String): Cursor

    @Query("SELECT * FROM User_CheckIn_CheckOut_Time_Log")
    fun getAllData(): Cursor
    @Query("SELECT * FROM User_Detail")
    fun getAllUserPersonalData(): Cursor

}

