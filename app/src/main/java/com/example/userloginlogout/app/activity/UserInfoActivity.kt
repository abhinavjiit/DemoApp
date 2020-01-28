package com.example.userloginlogout.app.activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.userloginlogout.R
import com.example.userloginlogout.app.DatabaseClient
import com.example.userloginlogout.app.model.UserInfo
import kotlinx.android.synthetic.main.user_info_activity.*


class UserInfoActivity : AppCompatActivity() {
    private lateinit var userInfo: UserInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_info_activity)
        saveAndContinue.setOnClickListener {
            if (isValid())
                saveData()
        }
    }


    private fun saveData() {
        userInfo = UserInfo()
        userInfo.employeeCode = employeeCodeEditTextView.text.toString().trim()
        userInfo.contactNumber = phoneNumberEditTextView.text.toString().trim()
        userInfo.dob = DobEditTextView.text.toString().trim()
        userInfo.email = emailEditTextView.text.toString().trim()
        userInfo.name = nameEditTextView.text.toString().trim()
        userInfo.password = passwordEditTextView.text.toString().trim()

        val saveUserInfo = SaveUserInfo(applicationContext, userInfo)
        saveUserInfo.execute()
    }

    class SaveUserInfo internal constructor(var context: Context, var userInfo: UserInfo) :
        AsyncTask<Void, Void, Void>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: Void?): Void? {
            DatabaseClient.getInstance(context).appDatabase.userDao()
                .insert(userInfo)
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            Toast.makeText(context, "dataSaved", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, UserEditDeleteActivity::class.java)
            intent.putExtra("id", userInfo.employeeCode)
            intent.putExtra("pass", userInfo.password)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }


    private fun isValid(): Boolean {
        when {
            employeeCodeEditTextView.text.toString().trim().isBlank() -> {
                employeeCodeEditTextView.error = "field can't be empty"
                employeeCodeEditTextView.requestFocus()
                return false
            }
            nameEditTextView.text.toString().trim().isBlank() -> {
                nameEditTextView.error = "field can't be empty"
                nameEditTextView.requestFocus()
                return false
            }
            DobEditTextView.text.toString().trim().isBlank() -> {
                DobEditTextView.error = "field can't be empty"
                DobEditTextView.requestFocus()
                return false
            }
            passwordEditTextView.text.toString().trim().isBlank() -> {
                passwordEditTextView.error = "field can't be empty"
                passwordEditTextView.requestFocus()
                return false
            }
            phoneNumberEditTextView.text.toString().trim().isBlank() -> {
                phoneNumberEditTextView.error = "field can't be empty"
                phoneNumberEditTextView.requestFocus()
                return false
            }
            emailEditTextView.text.toString().trim().isBlank() -> {
                emailEditTextView.error = "field can't be empty"
                emailEditTextView.requestFocus()
                return false
            }
            else -> return true
        }
    }
}
