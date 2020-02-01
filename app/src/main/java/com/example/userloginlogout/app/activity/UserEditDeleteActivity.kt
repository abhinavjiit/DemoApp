package com.example.userloginlogout.app.activity

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.database.getStringOrNull
import com.example.userloginlogout.R
import com.example.userloginlogout.app.DatabaseClient
import com.example.userloginlogout.app.fragment.CheckInCheckOutDialogFragment
import com.example.userloginlogout.app.model.UserInfo
import com.opencsv.CSVWriter
import kotlinx.android.synthetic.main.user_edit_delete_activity.*
import kotlinx.coroutines.*
import java.io.File
import java.io.FileWriter

class UserEditDeleteActivity : AppCompatActivity() {
    lateinit var id: String
    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.d("CoroutineException", "$exception handled !")
    }

    private var getData: List<UserInfo>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_edit_delete_activity)
        id = intent.getStringExtra("id")
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
            userInfo.checkOut = checkoutEdittextView.text.toString()
            userInfo.checkIn = checkinEditTextView.text.toString()
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
                        checkinEditTextView.setText(getData?.get(0)?.checkIn)
                        checkoutEdittextView.setText(getData?.get(0)?.checkOut)
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
                Log.d("DialogFragmentException", e.message)
            }


            /* CoroutineScope(Dispatchers.IO).launch {
                 val checkIncheckOut = DatabaseClient.getInstance(applicationContext).appDatabase.userDao()
                         .getAllCheckInCheckOutTime(employeeId = id)
                 MainScope()

             }*/


        }
        downloadCSVFile.setOnClickListener {
            try {
                val file = File(Environment.getExternalStorageDirectory(), "EmployeeCode/$id")
                if (!file.exists()) {
                    Toast.makeText(applicationContext, "went wrong", Toast.LENGTH_SHORT).show()
                    file.mkdir()
                }
                val fileFolder = File(file, "$id.csv")
                fileFolder.createNewFile()
                val csvWriter = CSVWriter(FileWriter(fileFolder))
                CoroutineScope(Dispatchers.IO).launch {
                    val data = async { DatabaseClient.getInstance(applicationContext).appDatabase.userDao().getData(id) }
                    val dataa = data.await()
                    MainScope().launch {
                        try {
                            csvWriter.writeNext(dataa.columnNames)
                            while (dataa.moveToNext()) {
                                val empty = Array<String?>(dataa.columnCount) {
                                    null
                                }
                                for (i in 0 until dataa.columnCount) {
                                    empty[i] = dataa.getStringOrNull(i)
                                }
                                csvWriter.writeNext(empty)
                            }
                            Toast.makeText(applicationContext, "data downloading.....", Toast.LENGTH_SHORT).show()
                            csvWriter.close()
                            dataa.close()
                        } catch (e: Exception) {
                            Log.d("CSVFileWriteException", e.message)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("CSVAndNewFileException", e.message)
                Toast.makeText(applicationContext, "went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun fetchDataAsync(id: String, pass: String): List<UserInfo> {
        return DatabaseClient.getInstance(applicationContext).appDatabase.userDao()
                .getDataAsync(id, pass)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, CheckInOrCheckOutOrLoginActivity::class.java)
        startActivity(intent)
        finish()

    }
}