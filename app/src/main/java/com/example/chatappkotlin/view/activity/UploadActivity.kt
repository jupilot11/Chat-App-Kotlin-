package com.example.chatappkotlin.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.chatappkotlin.R
import com.example.chatappkotlin.UserSettings
import com.example.chatappkotlin.util.Constants.Companion.INTENT_USER
import com.example.chatappkotlin.view.fragment.UploadSteponeFragment
import com.example.chatappkotlin.view.fragment.UploadSteptwoFragment
import com.google.android.gms.common.internal.service.Common
import kotlinx.android.synthetic.main.activity_upload.*

class UploadActivity : AppCompatActivity() {

    var uploadSteponeFragment: Fragment? = null
    var uploadSteptwoFragment: Fragment? = null

    var userSettings: UserSettings? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)


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
                    uploadSteptwoFragment = UploadSteptwoFragment.newInstance("", "")
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

                    Toast.makeText(this, "Please select a photo to proceed", Toast.LENGTH_SHORT).show()

                }


            } else {

                Toast.makeText(this, "Mulapas naka woie", Toast.LENGTH_SHORT).show()

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
