package com.example.chatappkotlin.view.activity

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.chatappkotlin.*
import com.example.chatappkotlin.util.Constants.Companion.INTENT_USER
import com.example.chatappkotlin.util.DialogHelper
import com.example.chatappkotlin.util.FirebaseMethods
import com.example.chatappkotlin.view.fragment.UploadSteponeFragment
import com.example.chatappkotlin.view.fragment.UploadSteptwoFragment
import com.google.android.gms.common.internal.service.Common
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_upload.*
import java.util.*

class UploadActivity : AppCompatActivity() {

    var uploadSteponeFragment: Fragment? = null
    var uploadSteptwoFragment: Fragment? = null

    var userSettings: UserSettings? = null
    var userPosts: Posts? = null

    var str_caption: String? = null

    private var database: FirebaseDatabase? = null
    private var table_user: DatabaseReference? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var firebaseMethods: FirebaseMethods? = null
    private var dialogHelper: DialogHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        table_user = database!!.reference
        firebaseMethods = FirebaseMethods()
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
        dialogHelper = DialogHelper()
        if (intent != null) {

            userSettings = intent.getParcelableExtra(INTENT_USER)

        }

        position = 1
        uploadSteponeFragment = UploadSteponeFragment.newInstance(userSettings!!, "")
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.framelayout_upload, uploadSteponeFragment as UploadSteponeFragment, "")
        ft.commit()



        image_proceed.setOnClickListener {

            if (position == 1) {

                if (UploadSteponeFragment.posts != null) {
                    position = 2
                    uploadSteptwoFragment =
                        UploadSteptwoFragment.newInstance(UploadSteponeFragment.posts!!, "")
                    val ft = supportFragmentManager.beginTransaction()
//                ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right)
                    ft.replace(
                        R.id.framelayout_upload,
                        uploadSteptwoFragment as UploadSteptwoFragment,
                        ""
                    )
                    ft.commit()

                    img_view_close.setImageDrawable(getDrawable(R.drawable.ic_arrow_back_black_36dp))
                } else {

                    Toast.makeText(this, "Please select a photo to proceed", Toast.LENGTH_SHORT)
                        .show()

                }


            } else {

                str_caption = UploadSteptwoFragment.editText_caption!!.text.toString()

                userPosts = Posts(
                    UploadSteptwoFragment.posts!!.str_userid,
                    str_caption,
                    UploadSteptwoFragment.posts!!.str_uri
                )

                dialogHelper!!.showProgressDialog(this, "Please wait", false)


                val ref = storageReference?.child("images/" + UUID.randomUUID().toString())

                ref?.putFile(UploadSteptwoFragment.posts!!.str_uri!!)?.addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener { uri ->


                        dialogHelper!!.dismissProgressDialog()
                        userPosts = Posts(
                            UploadSteptwoFragment.posts!!.str_userid,
                            uri.toString(),
                            str_caption, userSettings!!.profile!!.arrayList?.get(0)?.uri,
                            userSettings!!.str_nickname
                        )
                        firebaseMethods!!.addnewPost(
                            table_user!!,
                            userPosts!!,
                            object : FirebaseMethods.AddPostCallback {
                                override fun onSuccess() {
                                    dialogHelper!!.dismissProgressDialog()

                                    finish()
                                }

                                override fun onFailure() {
                                    dialogHelper!!.dismissProgressDialog()

                                    Toast.makeText(this@UploadActivity, "You failed", Toast.LENGTH_SHORT).show()

                                }

                                override fun onConnectionTimeOut() {

//                                        dialogHelper!!.dismissProgressDialog()
                                    Toast.makeText(
                                        this@UploadActivity,
                                        "Connection Timed-out",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }

                            })
                    }

                }


            }

        }

        img_view_close.setOnClickListener {

            if (position == 1) {


                onBackPressed()

            } else {
                position = 1
                if (UploadSteponeFragment.posts != null) {
                    uploadSteponeFragment = UploadSteponeFragment.newInstance(
                        UploadSteponeFragment.posts!!,
                        userSettings!!
                    )
                    val ft = supportFragmentManager.beginTransaction()
                    ft.replace(
                        R.id.framelayout_upload,
                        uploadSteponeFragment as UploadSteponeFragment,
                        ""
                    )
                    ft.commit()
                } else {

                    uploadSteponeFragment = UploadSteponeFragment.newInstance(userSettings!!, "")
                    val ft = supportFragmentManager.beginTransaction()
                    ft.replace(
                        R.id.framelayout_upload, uploadSteponeFragment as UploadSteponeFragment, ""
                    )
                    ft.commit()
                }

                img_view_close.setImageDrawable(getDrawable(R.drawable.ic_close_black_36dp))


            }
        }
    }

    companion object {

        var position = 0

    }

    override fun onBackPressed() {

        if (position == 1) {

            super.onBackPressed()

        } else {
            position = 1

            if (UploadSteponeFragment.posts != null) {
                uploadSteponeFragment = UploadSteponeFragment.newInstance(
                    UploadSteponeFragment.posts!!,
                    userSettings!!
                )
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(
                    R.id.framelayout_upload,
                    uploadSteponeFragment as UploadSteponeFragment,
                    ""
                )
                ft.commit()
            } else {

                uploadSteponeFragment = UploadSteponeFragment.newInstance(userSettings!!, "")
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(
                    R.id.framelayout_upload, uploadSteponeFragment as UploadSteponeFragment, ""
                )
                ft.commit()
            }

            img_view_close.setImageDrawable(getDrawable(R.drawable.ic_close_black_36dp))


        }
    }


}
