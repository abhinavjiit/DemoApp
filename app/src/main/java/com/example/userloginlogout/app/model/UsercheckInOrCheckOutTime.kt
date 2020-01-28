package com.example.userloginlogout.app.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User_CheckIn_CheckOut_Time_Log")
class UsercheckInOrCheckOutTime {
    @NonNull
    @PrimaryKey
    var employeeCode: String? = null
    var checkIn: String? = null
    var checkOut: String? = null
    var date: String? = null
}


