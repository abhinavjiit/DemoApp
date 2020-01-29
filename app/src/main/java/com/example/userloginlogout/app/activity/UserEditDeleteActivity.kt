package com.example.userloginlogout.app.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.userloginlogout.R
import com.example.userloginlogout.app.DatabaseClient
import com.example.userloginlogout.app.fragment.CheckInCheckOutDialogFragment
import com.example.userloginlogout.app.model.UserInfo
import kotlinx.android.synthetic.main.user_edit_delete_activity.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserEditDeleteActivity : AppCompatActivity() {

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.d("Exception", "$exception handled !")
    }

    private var getData: List<UserInfo>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_edit_delete_activity)
        val id = intent.getStringExtra("id")
        val pass = intent.getStringExtra("pass")

        CoroutineScope(Dispatchers.IO + handler).launch {
            getData = fetchDataAsync(id, pass)

            if (!getData.isNullOrEmpty()) {
                CoroutineScope(Dispatchers.Main + handler).launch {
                    employeeCodeEditTextView.setText(getData?.get(0)?.employeeCode)
                    nameEditTextView.setText(getData?.get(0)?.name)
                    DobEditTextView.setText(getData?.get(0)?.dob)
                    passwordEditTextView.setText(getData?.get(0)?.password)
                    phoneNumberEditTextView.setText(getData?.get(0)?.contactNumber)
                    emailEditTextView.setText(getData?.get(0)?.email)
                    checkinEditTextView.setText(getData?.get(0)?.checkIn)
                    checkoutEdittextView.setText(getData?.get(0)?.checkOut)

                }
            }
        }

        saveAndupdate.setOnClickListener {
            val userInfo = UserInfo()
            userInfo.email = emailEditTextView.text.toString()
            userInfo.contactNumber = phoneNumberEditTextView.text.toString()
            userInfo.password = passwordEditTextView.text.toString()
            userInfo.dob = DobEditTextView.text.toString()
            userInfo.name = nameEditTextView.text.toString()
            userInfo.employeeCode = employeeCodeEditTextView.text.toString()
            CoroutineScope(Dispatchers.IO + handler).launch {
                DatabaseClient.getInstance(applicationContext).appDatabase.userDao()
                        .updateUser(userInfo)
                getData = DatabaseClient.getInstance(applicationContext).appDatabase.userDao()
                        .getDataAsync(id, pass)
                CoroutineScope(Dispatchers.Main + handler).launch {
                    if (!getData.isNullOrEmpty()) {
                        employeeCodeEditTextView.setText(getData?.get(0)?.employeeCode)
                        nameEditTextView.setText(getData?.get(0)?.name)
                        DobEditTextView.setText(getData?.get(0)?.dob)
                        passwordEditTextView.setText(getData?.get(0)?.password)
                        phoneNumberEditTextView.setText(getData?.get(0)?.contactNumber)
                        emailEditTextView.setText(getData?.get(0)?.email)
                    }
                    Toast.makeText(applicationContext, "data is updated", Toast.LENGTH_SHORT).show()
                }
            }
        }

        deleteUser.setOnClickListener {
            val userInfo = UserInfo()
            userInfo.email = getData?.get(0)?.email
            userInfo.contactNumber = getData?.get(0)?.contactNumber
            userInfo.password = getData?.get(0)?.password
            userInfo.dob = getData?.get(0)?.dob
            userInfo.name = getData?.get(0)?.name
            userInfo.employeeCode = getData?.get(0)?.employeeCode
            CoroutineScope(Dispatchers.IO + handler).launch {
                DatabaseClient.getInstance(applicationContext).appDatabase.userDao()
                        .deleteUser(userInfo)
                CoroutineScope(Dispatchers.Main + handler).launch {
                    Toast.makeText(applicationContext, "data is deleted", Toast.LENGTH_SHORT).show()
                }
            }


        }


        showUserCheckInCheckOutLog.setOnClickListener {
            try {
                val bundle = Bundle()
                bundle.putString("id", id)
                val checkInCheckOutDialogFragment = CheckInCheckOutDialogFragment()
                checkInCheckOutDialogFragment.arguments = bundle
                val fm = supportFragmentManager
                checkInCheckOutDialogFragment.show(fm, "collectionAddPopUp")
            } catch (e: Exception) {
                Log.d("Exception", e.message)
            }


            /* CoroutineScope(Dispatchers.IO).launch {
                 val checkIncheckOut = DatabaseClient.getInstance(applicationContext).appDatabase.userDao()
                         .getAllCheckInCheckOutTime(employeeId = id)
                 MainScope()

             }*/


        }


    }

    private suspend fun fetchDataAsync(id: String, pass: String): List<UserInfo> {
        return DatabaseClient.getInstance(applicationContext).appDatabase.userDao()
                .getDataAsync(id, pass)
    }
}