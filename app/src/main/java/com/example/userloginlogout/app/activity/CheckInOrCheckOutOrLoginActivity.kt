package com.example.userloginlogout.app.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.userloginlogout.R
import com.example.userloginlogout.app.fragment.CheckInCheckOutFragment

class CheckInOrCheckOutOrLoginActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checkint_checkout_login_activity)

        val userLoginTextView = findViewById<TextView>(R.id.userLoginTextView)
        val checkInTextView = findViewById<TextView>(R.id.checkInTextView)
        val checkOutTextView = findViewById<TextView>(R.id.checkOutTextView)
        userLoginTextView.setOnClickListener(this)
        checkInTextView.setOnClickListener(this)
        checkOutTextView.setOnClickListener(this)

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


        }


    }
}