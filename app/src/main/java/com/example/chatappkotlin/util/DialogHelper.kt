package com.example.chatappkotlin.util

import android.app.ProgressDialog
import android.content.Context
import com.example.chatappkotlin.R

class DialogHelper {


    private  var progressDialog: ProgressDialog? = null

    fun showProgressDialog(context: Context?, strMessage: String, isCancellable: Boolean) {
        if (context != null) {
            progressDialog = ProgressDialog(context, R.style.MyAlertDialogStyle)
            progressDialog?.setMessage(strMessage)
            progressDialog?.setIndeterminate(true)
            progressDialog?.setCancelable(isCancellable)
            progressDialog?.setCanceledOnTouchOutside(isCancellable)
            progressDialog?.show()
        }

    }

    fun dismissProgressDialog() {

        progressDialog?.dismiss()

    }
}