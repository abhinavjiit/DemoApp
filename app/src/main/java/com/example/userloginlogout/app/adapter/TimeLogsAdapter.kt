package com.example.userloginlogout.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.userloginlogout.R
import com.example.userloginlogout.app.model.UsercheckInOrCheckOutTime
import kotlinx.android.synthetic.main.time_logs_adapter_layout.view.*

class TimeLogsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var dataList: List<UsercheckInOrCheckOutTime>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.time_logs_adapter_layout, parent, false)
        return TimeLogsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList?.size!!

    }

    fun setTimeLogsData(data: List<UsercheckInOrCheckOutTime>?) {
        this.dataList = data
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is TimeLogsViewHolder) {
            holder.employeeCodeTextView.text = dataList?.get(position)?.employeeCode
            if (!dataList?.get(position)?.checkIn.isNullOrBlank()) {
                holder.checkInTimeTextView.text = dataList?.get(position)?.checkIn + " In"
            } else {
                holder.checkInTimeTextView.visibility = View.GONE
            }
            holder.dateTextView.text = dataList?.get(position)?.date

            if (!dataList?.get(position)?.checkOut.isNullOrBlank()) {
                holder.checkOutTimeTextView.text = dataList?.get(position)?.checkOut + " Out"
            } else {
                holder.checkOutTimeTextView.visibility = View.GONE
            }
        }
    }


    class TimeLogsViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val employeeCodeTextView: TextView = mView.employeeCodeTextView
        val dateTextView: TextView = mView.dateTextView
        val checkOutTimeTextView: TextView = mView.checkOutTimeTextView
        val checkInTimeTextView: TextView = mView.checkInTimeTextView
    }
}