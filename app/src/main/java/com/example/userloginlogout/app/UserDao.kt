package com.example.userloginlogout.app

import androidx.room.*
import com.example.userloginlogout.app.model.UserInfo
import com.example.userloginlogout.app.model.UsercheckInOrCheckOutTime
import retrofit2.http.GET

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


}

