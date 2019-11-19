package com.example.chatappkotlin.util

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.TextView
import com.example.chatappkotlin.R

class DialogHelper {


    private var progressDialog: ProgressDialog? = null
    private lateinit var dialog: Dialog
    private var tv_camera: TextView? = null
    private var tv_gallery: TextView? = null

    fun showProgressDialog(context: Context?, strMessage: String, isCancellable: Boolean) {
        if (context != null) {
            progressDialog = ProgressDialog(context, R.style.MyAlertDialogStyle)
            progressDialog?.setMessage(strMessage)
            progressDialog?.isIndeterminate = true
            progressDialog?.setCancelable(isCancellable)
            progressDialog?.setCanceledOnTouchOutside(isCancellable)
            progressDialog?.show()
        }

    }

    fun dismissProgressDialog() {

        progressDialog?.dismiss()

    }

    fun showUploadDialog(context: Context?, dialogUploadCallback: DialogUploadCallback) {

        if (context != null) {

            dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.alert_dialog)
            dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.getWindow()!!.attributes.windowAnimations = R.style.DialogAnimation
            dialog.setCancelable(true)

            tv_camera = dialog!!.findViewById(R.id.tv_camera)
            tv_gallery = dialog!!.findViewById(R.id.tv_gallery)

            tv_camera?.setOnClickListener {


                dialogUploadCallback.onSelectCam()
            }

            tv_gallery?.setOnClickListener {

                dialogUploadCallback.onSelectGallery()
            }


            dialog.show()
        }
    }

    fun dismissUploadDialog(){


        dialog.dismiss()
    }


    interface DialogUploadCallback {

        fun onSelectCam()

        fun onSelectGallery()

    }

}