package com.example.chatappkotlin.util

import android.app.IntentService
import android.content.Intent
import android.os.Parcelable
import com.example.chatappkotlin.Profile
import com.example.chatappkotlin.ProfilePic
import com.example.chatappkotlin.User
import com.example.chatappkotlin.UserSettings
import com.example.chatappkotlin.view.activity.HomeActivity
import com.example.chatappkotlin.view.fragment.ProfileFragment
import com.google.android.gms.common.internal.service.Common
import com.google.firebase.database.*

class RealtimeService : IntentService("Realtime") {


    private var database: FirebaseDatabase? = null
    private var table_user: DatabaseReference? = null
    private var userSettings: UserSettings? = null
    private var type: Int? = null

    override fun onHandleIntent(intent: Intent?) {

        if (intent != null) {


            database = FirebaseDatabase.getInstance()
            table_user = database!!.reference.child("User Settings")
            var arrayList: ArrayList<ProfilePic>? = null

            userSettings = intent.getParcelableExtra(Constants.INTENT_USER)
            type = intent.getIntExtra(Constants.INTENT_TYPE, 0)

            table_user!!.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    if (dataSnapshot.childrenCount != 0L) {

                        for (datasnapshot1 in dataSnapshot.children) {

                            var id = datasnapshot1.child("str_id").value.toString()

                            if (userSettings!!.str_id.equals(id)) {


                                var followers = datasnapshot1.child("followers")
                                    .value.toString()
                                var posts =
                                    datasnapshot1.child("posts").value.toString()

                                var following = datasnapshot1.child("following")
                                    .value.toString()

                                var nickname = datasnapshot1.child("str_display_name")
                                    .value.toString()



                                for (datasnaphot3 in datasnapshot1.child("photos").children) {

                                    arrayList = ArrayList()


                                    for (i in 0 until datasnapshot1.child("photos").childrenCount) {

                                        var bool_profile =
                                            datasnaphot3.child("_profile")
                                                .value.toString()
                                        var orig_uri =
                                            datasnaphot3.child("orig_uri")
                                                .value.toString()
                                        var cropper_uri =
                                            datasnaphot3.child("uri")
                                                .value.toString()

                                        var pic = ProfilePic(
                                            cropper_uri,
                                            bool_profile,
                                            orig_uri
                                        )

                                        arrayList!!.add(pic)
                                    }

                                    var profile = Profile(arrayList)


                                    var userSetting = UserSettings(
                                        userSettings!!.str_email,
                                        userSettings!!.str_id,
                                        userSettings!!.str_password,
                                        userSettings!!.str_userId,
                                        profile,
                                        followers.toInt(),
                                        following.toInt(),
                                        posts.toInt(),
                                        nickname
                                    )


                                    onRequestSuccess(userSetting)

                                }
                            }

                        }
                    }

                }

            })
        }

    }

    private fun onRequestSuccess(
        userSettings: UserSettings
    ) {
        if (type == 1) {

            val broadcastIntent = Intent()
            broadcastIntent.action = HomeActivity.RealtimeReceiver.TAGGED_RECEIVER
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT)
            broadcastIntent.putExtra(Constants.INTENT_USER, userSettings)
            sendBroadcast(broadcastIntent)

        }else if (type == 0){

            val broadcastIntent = Intent()
            broadcastIntent.action = ProfileFragment.RealtimeReceiver.TAGGED_RECEIVER
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT)
            broadcastIntent.putExtra(Constants.INTENT_USER, userSettings)
            sendBroadcast(broadcastIntent)

        }

    }


}