package com.example.chatappkotlin.util

import android.app.IntentService
import android.content.Intent
import com.example.chatappkotlin.UserSettings
import com.google.android.gms.common.internal.service.Common
import com.google.firebase.database.*

class RealtimeService : IntentService {


    private var database: FirebaseDatabase? = null
    private var table_user: DatabaseReference? = null
    private var userSettings : UserSettings? = null

    constructor(name: String?) : super(name) {

        database = FirebaseDatabase.getInstance()
        table_user = database!!.reference.child("User Settings")

    }


    override fun onHandleIntent(intent: Intent?) {

        userSettings = intent!!.getParcelableExtra(Constants.INTENT_USER)

        table_user!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.childrenCount != 0L) {

                    for (datasnapshot1 in dataSnapshot.children) {

                        var id =
                            datasnapshot1.child("str_id").value.toString()


                        if (userSettings!!.str_id.equals(id)) {


                            var followers = datasnapshot1.child("followers")
                                .value.toString()
                            var posts =
                                datasnapshot1.child("posts").value.toString()

                            var following = datasnapshot1.child("following")
                                .value.toString()

                            var nickname = datasnapshot1.child("str_display_name")
                                .value.toString()


//                            tv_followers!!.text = followers
//                            tv_post!!.text = posts
//                            tv_following!!.text = following
//                            tv_nickname!!.text = nickname
                        }
                    }


                }

            }

        })
    }
}