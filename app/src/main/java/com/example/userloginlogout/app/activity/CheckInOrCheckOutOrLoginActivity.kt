package com.example.userloginlogout.app.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.database.getStringOrNull
import com.example.userloginlogout.R
import com.example.userloginlogout.app.DatabaseClient
import com.example.userloginlogout.app.fragment.CheckInCheckOutFragment
import com.opencsv.CSVWriter
import kotlinx.coroutines.*
import java.io.File
import java.io.FileWriter
import java.io.Writer

class CheckInOrCheckOutOrLoginActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checkint_checkout_login_activity)

        val userLoginTextView = findViewById<TextView>(R.id.userLoginTextView)
        val checkInTextView = findViewById<TextView>(R.id.checkInTextView)
        val checkOutTextView = findViewById<TextView>(R.id.checkOutTextView)
        val downloadCSVFileAllUsers = findViewById<TextView>(R.id.downloadCSVFileAllUsers)
        val downloadCSVFileAllUsersPersonalData = findViewById<TextView>(R.id.downloadCSVFileAllUsersPersonalData)
        setupPermissions()
        userLoginTextView.setOnClickListener(this)
        checkInTextView.setOnClickListener(this)
        checkOutTextView.setOnClickListener(this)
        downloadCSVFileAllUsers.setOnClickListener(this)
        downloadCSVFileAllUsersPersonalData.setOnClickListener(this)
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("TAG", "Permission to write denied")
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Permission to download csv file.")
                        .setTitle("Permission required")
                builder.setPositiveButton("OK") { _, _ ->
                    Log.i("TAG", "Clicked")
                    makeRequest()
                }
                val dialog = builder.create()
                dialog.show()
            } else {
                makeRequest()
            }
        } else {

        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 111)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == 111) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@CheckInOrCheckOutOrLoginActivity, "Permission already Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@CheckInOrCheckOutOrLoginActivity, "Permission Denied", Toast.LENGTH_SHORT).show()

            }
        }
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
                    val file = File(Environment.getExternalStorageDirectory(), "AllEmployeesTimeData")
                    if (!file.exists()) {
                        Toast.makeText(applicationContext, "went wrong", Toast.LENGTH_SHORT).show()
                        file.mkdir()
                    }
                    val fileFolder = File(file, "AllTimeData.csv")

                    fileFolder.createNewFile()
                    val csvWriter = CSVWriter(FileWriter(fileFolder) as Writer?)
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
                    Toast.makeText(applicationContext, "data is deleted", Toast.LENGTH_SHORT).show()
                }

            }
            R.id.downloadCSVFileAllUsersPersonalData -> {
                try {
                    val file = File(Environment.getExternalStorageDirectory(), "AllEmployeesPersonalData")
                    if (!file.exists()) {
                      //  Toast.makeText(applicationContext, "went wrong", Toast.LENGTH_SHORT).show()
                        file.mkdir()
                    }
                    val fileFolder = File(file, "AllPersonalData.csv")

                    fileFolder.createNewFile()
                    val csvWriter = CSVWriter(FileWriter(fileFolder) as Writer?)
                    CoroutineScope(Dispatchers.IO).launch {
                        val data = async { DatabaseClient.getInstance(applicationContext).appDatabase.userDao().getAllUserPersonalData() }
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
                    Toast.makeText(applicationContext, "data is deleted", Toast.LENGTH_SHORT).show()
                }

            }


        }


    }
}