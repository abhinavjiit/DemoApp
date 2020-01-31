package com.example.userloginlogout.app.activity

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.database.getStringOrNull
import com.example.userloginlogout.R
import com.example.userloginlogout.app.DatabaseClient
import com.example.userloginlogout.app.fragment.CheckInCheckOutFragment
import com.opencsv.CSVWriter
import kotlinx.coroutines.*
import java.io.File
import java.io.FileWriter

class CheckInOrCheckOutOrLoginActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checkint_checkout_login_activity)

        val userLoginTextView = findViewById<TextView>(R.id.userLoginTextView)
        val checkInTextView = findViewById<TextView>(R.id.checkInTextView)
        val checkOutTextView = findViewById<TextView>(R.id.checkOutTextView)
        val downloadCSVFileAllUsers = findViewById<TextView>(R.id.downloadCSVFileAllUsers)
        userLoginTextView.setOnClickListener(this)
        checkInTextView.setOnClickListener(this)
        checkOutTextView.setOnClickListener(this)
        downloadCSVFileAllUsers.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.userLoginTextView -> {
                val intent = Intent(
                        this@CheckInOrCheckOutOrLoginActivity,
                        UserSignInOrSignUpActivity::class.java
                )
                startActivity(intent)
            }

            R.id.checkInTextView -> {
                val checkInCheckOutFragment =
                        CheckInCheckOutFragment.newInstance(comingFor = "checkIn")
                supportFragmentManager.beginTransaction()
                        .add(R.id.container, checkInCheckOutFragment, "CheckInFragment")
                        .addToBackStack("CheckInFragment").commit()
            }
            R.id.checkOutTextView -> {

                val checkInCheckOutFragment =
                        CheckInCheckOutFragment.newInstance(comingFor = "checkOut")
                supportFragmentManager.beginTransaction()
                        .add(R.id.container, checkInCheckOutFragment, "CheckInFragment")
                        .addToBackStack("CheckInFragment").commit()
            }

            R.id.downloadCSVFileAllUsers -> {
                try {
                    val file = File(Environment.getExternalStorageDirectory(), "AllEmployees")
                    if (!file.exists()) {
                        Toast.makeText(applicationContext, "went wrong", Toast.LENGTH_SHORT).show()
                        file.mkdir()
                    }
                    val fileFolder = File(file, "AllData + .csv")

                    fileFolder.createNewFile()
                    val csvWriter = CSVWriter(FileWriter(fileFolder))
                    CoroutineScope(Dispatchers.IO).launch {
                        val data = async { DatabaseClient.getInstance(applicationContext).appDatabase.userDao().getAllData() }
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
                                csvWriter.close()
                                dataa.close()
                            } catch (e: Exception) {
                                Log.d("CSVFileWriteException", e.message)
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.d("CSVAndNewFileException", e.message)
                    Toast.makeText(applicationContext, "data is deleted", Toast.LENGTH_SHORT).show()
                }

            }


        }


    }
}