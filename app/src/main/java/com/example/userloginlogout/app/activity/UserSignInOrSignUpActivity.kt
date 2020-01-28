package com.example.userloginlogout.app.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.userloginlogout.R
import com.example.userloginlogout.app.DatabaseClient
import com.example.userloginlogout.app.model.UserInfo
import kotlinx.android.synthetic.main.user_sign_in_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class UserSignInOrSignUpActivity : AppCompatActivity() {
    private var getData: List<UserInfo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_sign_in_activity)
        login.setOnClickListener {
            val employeeCode = employeeCodeEditTextView.text.toString()
            val pass = passwordEditTextView.text.toString()
            CoroutineScope(Dispatchers.IO)
                .launch {
                    getData = DatabaseClient.getInstance(applicationContext).appDatabase.userDao()
                        .getDataAsync(employeeCode, pass)
                    delay(1000)
                    CoroutineScope(Dispatchers.Main).launch {
                        if (!getData.isNullOrEmpty()) {
                            val intent = Intent(
                                this@UserSignInOrSignUpActivity,
                                UserEditDeleteActivity::class.java
                            )
                            intent.putExtra("id", employeeCode)
                            intent.putExtra("pass", pass)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this@UserSignInOrSignUpActivity,
                                "not a user",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }

                }
        }
        signUpTextView.setOnClickListener {
            val intent = Intent(this, UserInfoActivity::class.java)
            startActivity(intent)
        }
    }
}
