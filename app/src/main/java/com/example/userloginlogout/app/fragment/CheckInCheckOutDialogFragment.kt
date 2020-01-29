package com.example.userloginlogout.app.fragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.userloginlogout.R
import com.example.userloginlogout.app.DatabaseClient
import com.example.userloginlogout.app.adapter.TimeLogsAdapter
import com.example.userloginlogout.app.model.UsercheckInOrCheckOutTime
import kotlinx.android.synthetic.main.checkin_checkout_all_logs_dialog_fragment.view.*
import kotlinx.coroutines.*


class CheckInCheckOutDialogFragment : DialogFragment() {

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.d("Exception", "$exception handled !")
    }
    private lateinit var articleDataModelsNew: List<UsercheckInOrCheckOutTime>
    var id: String? = null
    private val timeLogsAdapter: TimeLogsAdapter by lazy { TimeLogsAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.checkin_checkout_all_logs_dialog_fragment, container, false)
        id = if (arguments!!.containsKey("id")) {
            arguments?.getString("id")
        } else {
            ""
        }

        val llm = LinearLayoutManager(activity)
        llm.orientation = RecyclerView.VERTICAL
        view.recyclerView.layoutManager = llm
        articleDataModelsNew = ArrayList()
        view.recyclerView.adapter = timeLogsAdapter
        CoroutineScope(Dispatchers.IO + handler).launch {
            val logsData = fetchLogsData(id!!)
            MainScope().launch {
                if (logsData.isNullOrEmpty()) {
                    Toast.makeText(context, "not data", Toast.LENGTH_SHORT).show()
                } else {
                    timeLogsAdapter.setTimeLogsData(logsData)
                    timeLogsAdapter.notifyDataSetChanged()
                }
            }
        }


        return view
    }


    private suspend fun fetchLogsData(id: String): List<UsercheckInOrCheckOutTime>? {
        return DatabaseClient.getInstance(context).appDatabase.userDao().getAllCheckInCheckOutTime(id)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            //  dialog.window!!.setWindowAnimations(R.style.CollectionDialogAnimation)
            // dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent)))
        }
        // shimmer1.startShimmerAnimation()
    }


}