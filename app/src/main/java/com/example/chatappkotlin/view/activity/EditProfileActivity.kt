package com.example.chatappkotlin.view.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.chatappkotlin.*
import com.example.chatappkotlin.util.Constants
import com.example.chatappkotlin.util.DialogHelper
import com.example.chatappkotlin.util.FirebaseMethods
import com.example.chatappkotlin.view.fragment.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.circleImageview
import kotlinx.android.synthetic.main.activity_edit_profile.et_email
import kotlinx.android.synthetic.main.activity_edit_profile.et_nickname
import kotlinx.android.synthetic.main.activity_edit_profile.et_phone
import kotlinx.android.synthetic.main.activity_signup.*
import java.net.URI
import java.util.*
import kotlin.collections.ArrayList

class EditProfileActivity : AppCompatActivity(), View.OnClickListener {

    var userSetting: UserSettings? = null
    var dialogHelper: DialogHelper? = null
    var comparedUser: UserSettings? = null
    var user: User? = null

    private var uri: Uri? = null
    private var uri1: Uri? = null
    private var uriArrayList: ArrayList<Uri>? = null


    private var str_email: String? = null
    private var str_display_name: String? = null
    private var str_biography: String? = null
    private var str_nickname: String? = null
    private var str_phone: String? = null
    private var profile: Profile? = null
    private var prfilePicArrayList: ArrayList<ProfilePic>? = null
    private var firebaseMethods: FirebaseMethods? = null
    private var uriArrayList1: ArrayList<Uri>? = null


    private var database: FirebaseDatabase? = null
    private var table_user: DatabaseReference? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private var REQUEST_IMAGE = 0
    private var profilePic: ProfilePic? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)


        dialogHelper = DialogHelper()
        firebaseMethods = FirebaseMethods()

        if (intent != null) {

            userSetting = intent.getParcelableExtra(Constants.INTENT_USER)


            profilePic = ProfilePic(

                userSetting!!.profile!!.arrayList?.get(0)?.uri,
                "true",
                userSetting!!.profile!!.arrayList?.get(0)?.orig_uri
            )


        }

        uriArrayList = ArrayList()

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        table_user = database!!.reference
        firebaseMethods = FirebaseMethods()
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        Glide.with(this)
            .load(userSetting!!.profile!!.arrayList!![0].uri)
            .error(R.drawable.ic_person_black_48dp)
            .into(circleImageview)

        et_email.setText(userSetting!!.str_email)
        et_fullname.setText(userSetting!!.str_display_name)
        et_nickname.setText(userSetting!!.str_nickname)
        et_phone.setText(userSetting!!.str_phone)

        if (userSetting!!.str_biography.equals("") || userSetting!!.str_biography.equals("null")) {

            et_bio.setText("")
        } else {
            et_bio.setText(userSetting!!.str_biography)

        }
        img_view_close.setOnClickListener(this)
        image_proceed.setOnClickListener(this)
        edit_pic.setOnClickListener(this)
    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.edit_pic -> {

                dialogHelper!!.showUploadDialog(this, object : DialogHelper.DialogUploadCallback {
                    override fun onSelectCam() {

                        dialogHelper!!.dismissUploadDialog()

                        Dexter.withActivity(this@EditProfileActivity)
                            .withPermissions(
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                            .withListener(object : MultiplePermissionsListener {
                                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                                    if (report.areAllPermissionsGranted()) {

                                        launchCameraIntent()
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


                        dialogHelper!!.dismissUploadDialog()

                        Dexter.withActivity(this@EditProfileActivity)
                            .withPermissions(
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                            .withListener(object : MultiplePermissionsListener {
                                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                                    if (report.areAllPermissionsGranted()) {

                                        launchGalleryIntent()
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
                })
            }
            R.id.img_view_close -> {


                onBackPressed()

//                str_email = et_email.text.toString()
//                str_display_name = et_fullname.text.toString()
//                str_biography = et_bio.text.toString()
//                str_nickname = et_nickname.text.toString()
//                str_phone = et_phone.text.toString()
//
//
//                comparedUser = UserSettings(
//                    str_email,
//                    str_display_name,
//                    str_biography,
//                    str_nickname,
//                    str_phone
//                )
//
//                userSetting = UserSettings(
//                    userSetting!!.str_email,
//                    userSetting!!.str_display_name,
//                    checkNull(userSetting!!.str_biography),
//                    userSetting!!.str_nickname,
//                    userSetting!!.str_phone
//                )
//
//
//                dialogHelper!!.showUnsavedDialog(
//                    this@EditProfileActivity,
//                    object : DialogHelper.DialogUnsavedCallback {
//                        override fun onYes() {
//
//                            Toast.makeText(
//                                this@EditProfileActivity,
//                                "OK KEYOU",
//                                Toast.LENGTH_SHORT
//                            )
//                                .show()
//                            dialogHelper!!.dismissUnsavedDialog()
//
//                        }
//
//                        override fun onNo() {
//
//                            Toast.makeText(
//                                this@EditProfileActivity,
//                                "DILI KEYOU",
//                                Toast.LENGTH_SHORT
//                            ).show()
//
//                            dialogHelper!!.dismissUnsavedDialog()
//
//                        }
//
//                    })
//                if (comparedUser!! == userSetting) {
//
//
//
//                    Toast.makeText(
//                        this@EditProfileActivity,
//                        "NOTHING KEYOU",
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()
//                } else {
//
//
//                }


            }


            R.id.image_proceed -> {


                str_email = et_email.text.toString()
                str_display_name = et_fullname.text.toString()
                str_biography = et_bio.text.toString()
                str_nickname = et_nickname.text.toString()
                str_phone = et_phone.text.toString()



                if (str_email.equals("") || str_display_name.equals("") || str_nickname.equals("")) {

                    Toast.makeText(this@EditProfileActivity, "Please fill required fields.", Toast.LENGTH_SHORT).show()

                } else {

                    comparedUser = UserSettings(
                        str_email,
                        userSetting!!.str_id,
                        userSetting!!.str_password,
                        userSetting!!.str_userId,
                        userSetting!!.followers,
                        userSetting!!.following,
                        userSetting!!.posts,
                        str_display_name,
                        str_nickname,
                        str_phone,
                        str_biography
                    )

                    user = User(
                        str_email,
                        str_display_name,
                        userSetting!!.str_password,
                        userSetting!!.str_userId,
                        userSetting!!.str_id,
                        str_nickname,
                        str_phone
                    )

                    dialogHelper!!.showProgressDialog(this, "Please wait...", false)

                    firebaseMethods!!.updateprofile(
                        table_user!!,
                        comparedUser!!,
                        user!!,
                        object : FirebaseMethods.UpdateCallback {
                            override fun onSuccess(userSettings: UserSettings) {


                                uploadImage(uriArrayList!!, comparedUser!!, user!!, profilePic!!)


                            }

                            override fun onFailure() {

                                dialogHelper!!.dismissProgressDialog()
                                Toast.makeText(
                                    this@EditProfileActivity,
                                    "You failed",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                            override fun onConnectionTimeOut() {

                                dialogHelper!!.dismissProgressDialog()
                                Toast.makeText(
                                    this@EditProfileActivity,
                                    "Connection Timed out",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                            override fun onErrorPassword() {
                            }

                        })

                }

            }

        }
    }

    private fun checkNull(bio: String?): String {

        if (bio != null) {

            return bio

        } else {

            return ""
        }
    }


    private fun launchCameraIntent() {
        val intent = Intent(this, ImagePickerActivity::class.java)
        intent.putExtra(

            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_IMAGE_CAPTURE
        )

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000)

        startActivityForResult(intent, REQUEST_IMAGE)
    }

    private fun launchGalleryIntent() {
        val intent = Intent(this, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_GALLERY_IMAGE
        )

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)
        startActivityForResult(intent, REQUEST_IMAGE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == REQUEST_IMAGE) {

            if (resultCode == Activity.RESULT_OK) {

                uri = data?.getParcelableExtra<Uri>("path")
                uri1 = data?.getParcelableExtra<Uri>("display")

                uriArrayList = ArrayList()
                uriArrayList?.add(this.uri!!)
                uriArrayList?.add(this.uri1!!)


                circleImageview.setImageURI(uri1)

                Toast.makeText(this@EditProfileActivity, "NISULOD", Toast.LENGTH_SHORT)
                    .show()

            }

        }
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


    private fun uploadImage(
        uriArrayList: ArrayList<Uri>,
        userSettings: UserSettings,
        user: User,
        profilePic: ProfilePic
    ) {


        if (uriArrayList.size == 0) {

            table_user!!.child("User Settings")
                .child(userSettings.str_id!!)
                .setValue(userSettings)
                .addOnCompleteListener {

                    if (it.isSuccessful) {

                        firebaseMethods?.insertProfileImages(
                            table_user!!.child("User Settings"),
                            userSettings,
                            user,
                            uriArrayList, profilePic,
                            object :
                                FirebaseMethods.LoginCallback {
                                override fun onSuccess(
                                    userSettings: UserSettings
                                ) {


                                    dialogHelper!!.dismissProgressDialog()

                                    var intent = Intent()

                                    intent.putExtra(Constants.INTENT_USER, userSettings)
                                    setResult(Activity.RESULT_OK, intent)
                                    finish()
                                }


                                override fun onFailure() {
                                    dialogHelper!!.dismissProgressDialog()
                                    Toast.makeText(
                                        this@EditProfileActivity,
                                        "YOU FAILED",
                                        Toast.LENGTH_SHORT
                                    ).show()


                                }

                                override fun onConnectionTimeOut() {
                                    dialogHelper!!.dismissProgressDialog()
                                    Toast.makeText(
                                        this@EditProfileActivity,
                                        "Connection Timed out.",
                                        Toast.LENGTH_SHORT
                                    ).show()


                                }

                                override fun onErrorPassword() {
                                }
                            })

                    }
                }

        } else {

            uriArrayList1 = ArrayList()
            var ctr = 0

            for (i in 0 until uriArrayList.size) {

                val ref =
                    storageReference?.child("images/" + UUID.randomUUID().toString())

                ref?.putFile(uriArrayList[i])?.addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener { uri ->

                        uriArrayList1?.add(uri)

                        ctr++

                        if (ctr == 2) {


                            table_user!!.child("User Settings")
                                .child(userSettings.str_id!!)
                                .setValue(userSettings)
                                .addOnCompleteListener {

                                    if (it.isSuccessful) {

                                        firebaseMethods?.insertProfileImages(
                                            table_user!!.child("User Settings"),
                                            userSettings,
                                            user,
                                            uriArrayList1!!, profilePic!!,
                                            object :
                                                FirebaseMethods.LoginCallback {
                                                override fun onSuccess(
                                                    userSettings: UserSettings
                                                ) {


                                                    dialogHelper!!.dismissProgressDialog()
                                                    Toast.makeText(
                                                        this@EditProfileActivity,
                                                        "SUCCESS",
                                                        Toast.LENGTH_SHORT
                                                    ).show()


                                                }

                                                override fun onFailure() {
                                                    dialogHelper!!.dismissProgressDialog()
                                                    Toast.makeText(
                                                        this@EditProfileActivity,
                                                        "YOU FAILED",
                                                        Toast.LENGTH_SHORT
                                                    ).show()


                                                }

                                                override fun onConnectionTimeOut() {
                                                    dialogHelper!!.dismissProgressDialog()
                                                    Toast.makeText(
                                                        this@EditProfileActivity,
                                                        "Connection Timed out.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()


                                                }

                                                override fun onErrorPassword() {
                                                }
                                            })

                                    }
                                }


                        }
                    }

                }

            }
        }

    }
}
