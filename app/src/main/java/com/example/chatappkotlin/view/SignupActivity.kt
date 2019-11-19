package com.example.chatappkotlin.view

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NavUtils
import com.example.chatappkotlin.R
import com.example.chatappkotlin.util.TextWatcherHelper
import com.example.chatappkotlin.User
import com.example.chatappkotlin.presenter.SignupPresenter
import com.example.chatappkotlin.util.Contract
import com.example.chatappkotlin.util.DialogHelper
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_signup.*
import android.os.Environment.getExternalStorageDirectory
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import java.io.File


class SignupActivity : AppCompatActivity(), Contract.SignupActivityView, View.OnClickListener {


    private var file: File? = null
    private var uri: Uri? = null
    private var signuPresenter: SignupPresenter? = null

    private var str_username = ""
    private var str_password = ""
    private var str_email = ""
    private var dialogHelper: DialogHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        btn_signup.setOnClickListener(this)
        btn_signin.setOnClickListener(this)
        img_profile_change.setOnClickListener(this)

        textWatcher()

        signuPresenter = SignupPresenter(this)
        dialogHelper = DialogHelper()

    }


    override fun onShowProgDialog() {

        dialogHelper?.showProgressDialog(this, "Please wait", false)
    }

    override fun onDismissProgress() {

        dialogHelper?.dismissProgressDialog()
    }

    override fun onEmpyFields(textViews: Array<TextView>, i: Int) {

        textViews[i].visibility = View.VISIBLE
    }

    override fun onFailure(type: Int, message: String) {

        if (type == 0) {
            tv_email.visibility = View.VISIBLE
            tv_email.text = message
        } else {
            tv_user_error.visibility = View.VISIBLE

        }

    }

    override fun onConnectionTimed() {

        Toast.makeText(this@SignupActivity, "Connection Timed out.", Toast.LENGTH_SHORT).show()

    }

    override fun onIntentSignIn() {
        NavUtils.navigateUpFromSameTask(this)
        finish()
    }

    override fun onPasswordError() {

        tv_user_error.visibility = View.VISIBLE
    }

    override fun onSuccess(user: User) {


//        val intent = Intent(this@SignupActivity, MessagesActivity::class.java)
//        intent.putExtra("user", user)
//        startActivity(intent)

        Toast.makeText(this@SignupActivity, "Success pota", Toast.LENGTH_SHORT).show()

    }

    override fun onClick(v: View) {

        when (v.id) {

            R.id.btn_signin -> {
                signuPresenter?.onHandleIntent()
            }

            R.id.btn_signup -> {

                str_email = et_email.text.toString()
                str_password = et_password.text.toString()
                str_username = et_username.text.toString()

                val editTexts = arrayOf<EditText>(et_email, et_username, et_password)
                val textViews = arrayOf<TextView>(tv_email, tv_user_error, tv_pass_error)

                val user = User(
                    str_email,
                    str_username,
                    str_password,
                    "ID" + System.currentTimeMillis()
                )

                signuPresenter?.onHandleSignup(editTexts, textViews, str_email, str_password, user)

            }

            R.id.img_profile_change -> {


                dialogHelper!!.showUploadDialog(
                    this@SignupActivity,
                    object : DialogHelper.DialogUploadCallback {
                        override fun onSelectCam() {


                            dialogHelper!!.dismissUploadDialog()


                            Dexter.withActivity(this@SignupActivity)
                                .withPermissions(
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                )
                                .withListener(object : MultiplePermissionsListener {
                                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                                        if (report.areAllPermissionsGranted()) {

                                            ClickImageFromCamera()
                                        }

                                        if (report.isAnyPermissionPermanentlyDenied) {

                                            showSettingsDialog()
                                        }
                                    }

                                    override fun onPermissionRationaleShouldBeShown(
                                        permissions: List<PermissionRequest>,
                                        token: PermissionToken
                                    ) {
                                        token.continuePermissionRequest()
                                    }
                                }).check()


                        }

                        override fun onSelectGallery() {

                            Toast.makeText(this@SignupActivity, "Gallery", Toast.LENGTH_SHORT)
                                .show()

                            dialogHelper!!.dismissUploadDialog()

                        }

                    })
            }

        }
    }


    private fun textWatcher() {

        et_password.addTextChangedListener(
            TextWatcherHelper(
                et_password,
                this@SignupActivity,
                object : TextWatcherHelper.OnTextChangedCallbacks {
                    override fun onTextChanged() {
                        tv_pass_error.visibility = View.GONE
                    }

                }
            )
        )

        et_username.addTextChangedListener(
            TextWatcherHelper(
                et_password,
                this@SignupActivity,
                object : TextWatcherHelper.OnTextChangedCallbacks {
                    override fun onTextChanged() {

                        tv_user_error.visibility = View.GONE
                    }

                }
            )
        )
        et_email.addTextChangedListener(
            TextWatcherHelper(
                et_email,
                this@SignupActivity,
                object : TextWatcherHelper.OnTextChangedCallbacks {
                    override fun onTextChanged() {

                        tv_email.visibility = View.GONE
                    }

                }
            )
        )

    }

    override fun onBackPressed() {
        NavUtils.navigateUpFromSameTask(this)
        finish()
    }


    fun ClickImageFromCamera() {

        var intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)

        file = File(
            getExternalStorageDirectory(),
            "file" + System.currentTimeMillis().toString() + ".jpg"
        )
        uri = Uri.fromFile(file)

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri)

        intent.putExtra("return-data", true)

        startActivityForResult(intent, 0)

    }


    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Grant Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton(
            "GO TO SETTINGS"
        ) { dialog, _ ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(
            getString(android.R.string.cancel)
        ) { dialog, _ -> dialog.cancel() }
        builder.show()

    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", this.getPackageName(), null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }
}
