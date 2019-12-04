package com.example.chatappkotlin.view.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.chatappkotlin.Posts

import com.example.chatappkotlin.R
import com.example.chatappkotlin.UserSettings
import com.example.chatappkotlin.util.DialogHelper
import com.example.chatappkotlin.view.activity.ImagePickerActivity
import com.example.chatappkotlin.view.activity.UploadActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.fragment_upload_stepone.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"
private const val ARG_PARAM4 = "param4"

class UploadSteponeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var userSettings: UserSettings? = null

    var dialogHelper: DialogHelper? = null


    private var REQUEST_IMAGE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            userSettings = it.getParcelable(ARG_PARAM3)
            posts = it.getParcelable(ARG_PARAM4)

        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dialogHelper = DialogHelper()



        if (posts != null) {

            if (posts!!.str_uri != null) {

                imageCam!!.visibility = View.GONE
                imagepost!!.setImageURI(posts!!.str_uri)


            } else {

                imageCam!!.visibility = View.VISIBLE


            }

        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_upload_stepone, container, false)

        imagepost = view.findViewById(R.id.imageView)
        imageCam = view.findViewById(R.id.imageView_cam)
        btn_select = view.findViewById(R.id.btn_choose)

        uriArrayList = ArrayList()
        if (uriArrayList?.size == 0) {

            imageCam!!.visibility = View.VISIBLE

        } else {

            imageCam!!.visibility = View.GONE

        }

        btn_select!!.setOnClickListener {

            dialogHelper!!.showUploadDialog(activity, object : DialogHelper.DialogUploadCallback {
                override fun onSelectCam() {

                    dialogHelper!!.dismissUploadDialog()

                    Dexter.withActivity(activity)
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

                    Dexter.withActivity(activity)
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

        return view

    }


    companion object {

        var imagepost: ImageView? = null
        var imageCam: ImageView? = null
        var uri: Uri? = null
        var uri1: Uri? = null
        var uriArrayList: ArrayList<Uri>? = null
        var btn_select: TextView? = null
        var posts: Posts? = null


        @JvmStatic
        fun newInstance(posts: Posts, user: UserSettings) =
            UploadSteponeFragment().apply {
                arguments = Bundle().apply {

                    putParcelable(ARG_PARAM4, posts)
                    putParcelable(ARG_PARAM3, user)

                    UploadActivity.position = 1

                }
            }

        @JvmStatic
        fun newInstance(user: UserSettings, param2: String) =
            UploadSteponeFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM3, user)
                    putString(ARG_PARAM2, param2)

                    UploadActivity.position = 1

                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE) {

            if (resultCode == Activity.RESULT_OK) {

                uri = data?.getParcelableExtra<Uri>("path")
                uri1 = data?.getParcelableExtra<Uri>("display")

                uriArrayList = ArrayList()
                uriArrayList?.add(uri!!)
                uriArrayList?.add(uri1!!)

                if (uriArrayList?.size == 0) {

                    imageCam!!.visibility = View.VISIBLE



                } else {

                    imageCam!!.visibility = View.GONE
                    imagepost!!.setImageURI(uri1)


                    posts = Posts(userSettings!!.str_id, uri1)

                }

            }

        }
    }


    private fun launchCameraIntent() {
        val intent = Intent(activity, ImagePickerActivity::class.java)
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
        val intent = Intent(activity, ImagePickerActivity::class.java)
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


    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(activity!!.applicationContext)
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
        val uri = Uri.fromParts("package", activity!!.getPackageName(), null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

}
