package com.example.userloginlogout.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.userloginlogout.R
import com.example.userloginlogout.app.DatabaseClient
import com.example.userloginlogout.app.model.UsercheckInOrCheckOutTime
import kotlinx.android.synthetic.main.checkin_checkout_fragment.view.*
import kotlinx.android.synthetic.main.checkint_checkout_login_activity.view.employeeCodeEditTextView
import kotlinx.android.synthetic.main.checkint_checkout_login_activity.view.mainHeader
import kotlinx.android.synthetic.main.checkint_checkout_login_activity.view.passwordEditTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class CheckInCheckOutFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(comingFor: String) = CheckInCheckOutFragment().apply {
            arguments = Bundle().apply {
                this.putString("comingFor", comingFor)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.checkin_checkout_fragment, container, false)
        val comingFor = arguments?.getString("comingFor")
        view.mainHeader.text = comingFor
        view.checkInOut.text = comingFor
        context?.let { context ->
            view.checkInOut.setOnClickListener {
                when (comingFor) {
                    "checkIn" -> {
                        val employeeCode = view.employeeCodeEditTextView.text.toString()
                        val password = view.passwordEditTextView.text.toString()
                        val date = Calendar.getInstance().time
                        val formatter = SimpleDateFormat.getDateTimeInstance()
                        val formatedDate = formatter.format(date)
                        val c = Calendar.getInstance()
                        val sec = c.get(Calendar.SECOND)
                        val min = c.get(Calendar.MINUTE)
                        val hour = c.get(Calendar.HOUR)
                        val time = "$sec:$min:$hour"
                        val day = c.get(Calendar.DAY_OF_MONTH)
                        val month = c.get(Calendar.MONTH)
                        val year = c.get(Calendar.YEAR)
                        val datee = "$day/$month/$year"
                        CoroutineScope(Dispatchers.IO).launch {
                            val isUser = checkUserIsValidOrNot(employeeCode, password)
                            if (isUser) {
                                val usercheckInOrCheckOutTime = UsercheckInOrCheckOutTime()
                                usercheckInOrCheckOutTime.checkIn = time
                                usercheckInOrCheckOutTime.date = datee
                                usercheckInOrCheckOutTime.employeeCode = employeeCode
                                DatabaseClient.getInstance(context).appDatabase.userDao().updateCheckInTime(checkInTime = formatedDate, employeeId = employeeCode)
                                DatabaseClient.getInstance(context).appDatabase.userDao().insert(usercheckInOrCheckOutTime)

                                MainScope().launch {
                                    Toast.makeText(
                                            context,
                                            "you successfully checkedIn",
                                            Toast.LENGTH_SHORT
                                    )
                                            .show()
                                }


                            } else {
                                MainScope().launch {
                                    Toast.makeText(
                                            context,
                                            "not a user",
                                            Toast.LENGTH_SHORT
                                    )
                                            .show()
                                }
                            }


                        }
                    }

                    "checkOut" -> {
                        val employeeCode = view.employeeCodeEditTextView.text.toString()
                        val password = view.passwordEditTextView.text.toString()
                        val date = Calendar.getInstance().time
                        val formatter = SimpleDateFormat.getDateTimeInstance()
                        val formatedDate = formatter.format(date)
                        val c = Calendar.getInstance()
                        val sec = c.get(Calendar.SECOND)
                        val min = c.get(Calendar.MINUTE)
                        val hour = c.get(Calendar.HOUR)
                        val time = "$sec:$min:$hour"
                        val day = c.get(Calendar.DAY_OF_MONTH)
                        val month = c.get(Calendar.MONTH)
                        val year = c.get(Calendar.YEAR)
                        val datee = "$day/$month/$year"
                        CoroutineScope(Dispatchers.IO).launch {
                            val usercheckInOrCheckOutTime = UsercheckInOrCheckOutTime()
                            usercheckInOrCheckOutTime.checkOut = time
                            usercheckInOrCheckOutTime.date = datee
                            usercheckInOrCheckOutTime.employeeCode = employeeCode
                            val isUser = checkUserIsValidOrNot(employeeCode, password)
                            if (isUser) {
                                DatabaseClient.getInstance(context).appDatabase.userDao()
                                        .updateCheckOutTime(
                                                checkOutTime = formatedDate,
                                                employeeId = employeeCode
                                        )
                                DatabaseClient.getInstance(context).appDatabase.userDao().insert(usercheckInOrCheckOutTime)
                                MainScope().launch {
                                    Toast.makeText(
                                            context,
                                            "you successfully checkedOut",
                                            Toast.LENGTH_SHORT
                                    ).show()
                                }

                            } else {
                                MainScope().launch {
                                    Toast.makeText(
                                            context,
                                            "not a user",
                                            Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }


                        }


                    }

                }
            }
        }

        return view
    }

    private suspend fun checkUserIsValidOrNot(employeeCode: String, password: String): Boolean {
        if (DatabaseClient.getInstance(context).appDatabase.userDao()
                        .getDataAsync(employeeId = employeeCode, pass = password).isNullOrEmpty()
        ) {
            return false
        }

        return true
    }

}