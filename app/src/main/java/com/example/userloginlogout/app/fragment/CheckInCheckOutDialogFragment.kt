package com.example.userloginlogout.app.fragment

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.userloginlogout.R

class CheckInCheckOutDialogFragment:DialogFragment() {




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