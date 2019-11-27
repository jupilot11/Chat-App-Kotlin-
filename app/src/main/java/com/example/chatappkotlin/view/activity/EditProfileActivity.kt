package com.example.chatappkotlin.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.chatappkotlin.R
import com.example.chatappkotlin.User
import com.example.chatappkotlin.UserSettings
import com.example.chatappkotlin.util.Constants
import com.example.chatappkotlin.util.DialogHelper
import com.example.chatappkotlin.view.fragment.ProfileFragment
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity(), View.OnClickListener {

    var userSetting: UserSettings? = null
    var dialogHelper: DialogHelper? = null
    var comparedUser: UserSettings? = null

    private var str_email: String? = null
    private var str_display_name: String? = null
    private var str_biography: String? = null
    private var str_nickname: String? = null
    private var str_phone: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)


        dialogHelper = DialogHelper()

        if (intent != null) {

            userSetting = intent.getParcelableExtra(Constants.INTENT_USER)


        }


        Glide.with(this)
            .load(userSetting!!.profile!!.arrayList!![0].uri)
            .error(R.drawable.ic_person_black_48dp)
            .into(circleImageview)

        et_email.setText(userSetting!!.str_email)
        et_fullname.setText(userSetting!!.str_display_name)
        et_nickname.setText(userSetting!!.str_nickname)
        et_phone.setText(userSetting!!.str_phone)


        img_view_close.setOnClickListener(this)
    }

    override fun onClick(v: View) {

        when (v.id) {

            R.id.img_view_close -> {

                str_email = et_email.text.toString()
                str_display_name = et_fullname.text.toString()
                str_biography = et_bio.text.toString()
                str_nickname = et_nickname.text.toString()
                str_phone = et_phone.text.toString()


                comparedUser = UserSettings(
                    str_email,
                    str_display_name,
                    str_biography,
                    str_nickname,
                    str_phone
                )

                userSetting = UserSettings(
                    userSetting!!.str_email,
                    userSetting!!.str_display_name,
                    checkNull(userSetting!!.str_biography),
                    userSetting!!.str_nickname,
                    userSetting!!.str_phone
                )


                dialogHelper!!.showUnsavedDialog(
                    this@EditProfileActivity,
                    object : DialogHelper.DialogUnsavedCallback {
                        override fun onYes() {

                            Toast.makeText(
                                this@EditProfileActivity,
                                "OK KEYOU",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            dialogHelper!!.dismissUnsavedDialog()

                        }

                        override fun onNo() {

                            Toast.makeText(
                                this@EditProfileActivity,
                                "DILI KEYOU",
                                Toast.LENGTH_SHORT
                            ).show()

                            dialogHelper!!.dismissUnsavedDialog()

                        }

                    })

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

        }
    }

    private fun checkNull(bio: String?): String {

        if (bio != null) {

            return bio

        } else {

            return ""
        }
    }
}
