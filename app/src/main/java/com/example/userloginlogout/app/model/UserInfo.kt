package com.example.userloginlogout.app.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User_Detail")
class UserInfo {
    @NonNull
    @PrimaryKey
    var employeeCode: String? = null
    var name: String? = null
    var phoneNumber: String? = null
    var dob: String? = null
    @NonNull
    var password: String? = null
    var contactNumber: String? = null
    var email: String? = null
    var checkIn: String? = null
    var checkOut: String? = null
}

