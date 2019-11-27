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
    private lateinit var unsave_dialog: Dialog
    private var tv_camera: TextView? = null
    private var tv_gallery: TextView? = null
    private var tv_no: TextView? = null
    private var tv_yes: TextView? = null

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

    fun showUnsavedDialog(context: Context?, dialogUnsavedCallback: DialogUnsavedCallback) {

        if (context != null) {

            unsave_dialog = Dialog(context)
            unsave_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            unsave_dialog.setContentView(R.layout.unsaved_layout)
            unsave_dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            unsave_dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            unsave_dialog.setCancelable(true)

            tv_yes = unsave_dialog.findViewById(R.id.tv_yes)
            tv_no = unsave_dialog.findViewById(R.id.tv_no)

            tv_yes?.setOnClickListener {


                dialogUnsavedCallback.onYes()
            }

            tv_no?.setOnClickListener {

                dialogUnsavedCallback.onNo()
            }


            unsave_dialog.show()
        }
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

    fun dismissUploadDialog() {


        dialog.dismiss()
    }


    interface DialogUploadCallback {

        fun onSelectCam()

        fun onSelectGallery()

    }

    interface DialogUnsavedCallback {

        fun onYes()

        fun onNo()

    }

    fun dismissUnsavedDialog() {

        unsave_dialog.dismiss()
    }

}