package com.example.chatappkotlin.util

import android.net.Uri
import android.os.Handler
import com.example.chatappkotlin.Profile
import com.example.chatappkotlin.ProfilePic
import com.example.chatappkotlin.User
import com.example.chatappkotlin.UserSettings
import com.google.firebase.database.*

class FirebaseMethods {

    var valueEventListener1: ValueEventListener? = null
    var valueEventListener2: ValueEventListener? = null
    var valueEventListener3: ValueEventListener? = null
    var valueEventListener4: ValueEventListener? = null

    internal val gotResult = BooleanArray(1)


    private var isnotEqual = false
    private var isEqual = false
    private var isEquals = false

    private var user: User? = null
    private var userSettings: UserSettings? = null
    private var arrayList: ArrayList<ProfilePic>? = null

    fun firebaseRegistration(
        databaseReference: DatabaseReference,
        str_username: String,
        registrationCallback: RegistrationCallback
    ) {


        valueEventListener1 = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                gotResult[0] = true
                if (dataSnapshot.childrenCount != 0L) {

                    isnotEqual = false

                    for (dataSnapshot1 in dataSnapshot.children) {

                        val user = dataSnapshot1.child("str_username").value.toString()

                        if (user.equals(str_username, ignoreCase = true)) {


                            isnotEqual = true

                            registrationCallback.onUserExists()
                            valueEventListener1?.let { databaseReference.removeEventListener(it) }
                            break

                        }
                    }

                    if (!isnotEqual) {

                        registrationCallback.onSuccess()
                        valueEventListener1?.let { databaseReference.removeEventListener(it) }
                    }

                } else {
                    registrationCallback.onSuccess()
                    valueEventListener1?.let { databaseReference.removeEventListener(it) }
                }


            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }


        databaseReference.addListenerForSingleValueEvent(valueEventListener1 as ValueEventListener)
        checkConnectionTimeout(object : ConnectionTimeoutCallback {
            override fun onConnectionTimeOut() {
                databaseReference.removeEventListener(valueEventListener1 as ValueEventListener)
                registrationCallback.onConnectionTimeOut()
            }
        })
    }

    private fun checkConnectionTimeout(connectionTimeoutCallback: ConnectionTimeoutCallback) {
        val progressRunnable = {

            if (!gotResult[0]) { //  Timeout

                connectionTimeoutCallback.onConnectionTimeOut()

            }
        }
        val pdCanceller = Handler()
        pdCanceller.postDelayed(progressRunnable, 120000)
    }


    interface ConnectionTimeoutCallback {


        fun onConnectionTimeOut()
    }

    interface RegistrationCallback {

        fun onSuccess()

        fun onUserExists()

        fun onConnectionTimeOut()
    }


    fun insertProfileImages(
        databaseReference: DatabaseReference,
        users: UserSettings,
        user: User,
        uriArrayList1: ArrayList<Uri>,
        loginCallback: LoginCallback
    ) {

        valueEventListener3 = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                gotResult[0] = true

                for (dataSnapshot1 in dataSnapshot.children) {

                    val user_email = dataSnapshot1.child("str_email").value.toString()

                    if (user_email.equals(user.str_email)) {

                        var last_image = 0
                        var tot = last_image + 1


                        var profilePic = ProfilePic(
                            uriArrayList1[0].toString(), "true", uriArrayList1[1].toString()

                        )

                        databaseReference.child(dataSnapshot1.key.toString()).child("photos")
                            .child("0$tot")
                            .setValue(profilePic).addOnSuccessListener {

                                loginCallback.onSuccess(users)
                                databaseReference.removeEventListener(valueEventListener3 as ValueEventListener)
                            }

                    }
                }

            }

        }

        databaseReference.addValueEventListener(valueEventListener3 as ValueEventListener)
        checkConnectionTimeout(object : ConnectionTimeoutCallback {
            override fun onConnectionTimeOut() {
                loginCallback.onConnectionTimeOut()
                databaseReference.removeEventListener(valueEventListener3 as ValueEventListener)
            }
        })
    }

    fun firebaseLogin(
        databaseReference: DatabaseReference,
        str_username: String,
        str_password: String,
        loginCallback: LoginCallback
    ) {
        valueEventListener2 = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                gotResult[0] = true
                if (dataSnapshot.childrenCount != 0L) {

                    isEqual = false
                    for (dataSnapshot1 in dataSnapshot.child("User").children) {

                        val username = dataSnapshot1.child("str_email").value.toString()

                        if (username.equals(str_username, ignoreCase = true)) {

                            isEqual = true


                            var email = dataSnapshot1.child("str_email").value.toString()
                            var userid = dataSnapshot1.child("str_userId").value.toString()
                            var name = dataSnapshot1.child("str_username").value.toString()
                            var password = dataSnapshot1.child("str_password").value.toString()
                            var id = dataSnapshot1.child("id").value.toString()

                            user = User(email, name, password, userid, id)


                            break


                        }

                    }

                    if (isEqual) {


                        valueEventListener2?.let {
                            databaseReference.child("User")
                                .removeEventListener(it)
                        }

                        if (user?.str_password.equals(str_password)) {


                            valueEventListener4 = object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }

                                override fun onDataChange(dataSnapshot: DataSnapshot) {


                                    if (dataSnapshot.childrenCount != 0L) {

                                        isEquals = false

                                        for (datasnaphot2 in dataSnapshot.child("User Settings").children) {

                                            var id =
                                                datasnaphot2.child("str_id").value.toString()

                                            if (user!!.id.equals(id)) {


                                                isEquals = true


                                                var followers = datasnaphot2.child("followers")
                                                    .value.toString()
                                                var posts =
                                                    datasnaphot2.child("posts").value.toString()
                                                var following = datasnaphot2.child("following")
                                                    .value.toString()
                                                var nickname =
                                                    datasnaphot2.child("str_display_name")
                                                        .value.toString()

                                                for (datasnaphot3 in datasnaphot2.child("photos").children) {


                                                    arrayList = ArrayList()

                                                    var users: User? = user


                                                    for (i in 0 until datasnaphot2.child("photos").childrenCount) {

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
                                                            bool_profile,
                                                            orig_uri,
                                                            cropper_uri
                                                        )

                                                        arrayList!!.add(pic)
                                                    }

                                                    var profile = Profile(arrayList)


                                                    userSettings = UserSettings(
                                                        user!!.str_email,
                                                        user!!.id,
                                                        user!!.str_password,
                                                        user!!.str_userId,
                                                        profile,
                                                        followers.toInt(),
                                                        following.toInt(),
                                                        posts.toInt(),
                                                        nickname
                                                    )

                                                    break

                                                }


                                            }
                                        }


                                        if (isEquals) {


                                            valueEventListener4?.let {
                                                databaseReference.child("User Settings")
                                                    .removeEventListener(it)
                                            }

                                            loginCallback.onSuccess(userSettings!!)


                                        }
                                    }


                                }

                            }

//                            databaseReference.child("User Settings")
//                                .addValueEventListener(object : ValueEventListener {
//                                    override fun onCancelled(p0: DatabaseError) {
//                                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                                    }
//
//                                    override fun onDataChange(dataSnaphot: DataSnapshot) {
//
//
//                                    }
//
//
//                                })

                        } else {

                            loginCallback.onErrorPassword()
                            valueEventListener2?.let {
                                databaseReference.child("User").removeEventListener(it)
                            }
                        }
                    } else {
                        loginCallback.onFailure()
                        valueEventListener2?.let {
                            databaseReference.child("User").removeEventListener(it)
                        }
                    }


                } else {

                    loginCallback.onFailure()
                    valueEventListener2?.let {
                        databaseReference.child("User").removeEventListener(it)
                    }

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        databaseReference.addValueEventListener(valueEventListener2 as ValueEventListener)
        checkConnectionTimeout(object : ConnectionTimeoutCallback {
            override fun onConnectionTimeOut() {
                loginCallback.onConnectionTimeOut()
                databaseReference.removeEventListener(valueEventListener2 as ValueEventListener)
            }
        })
    }


    interface LoginCallback {
        fun onSuccess(userSettings: UserSettings)

        fun onFailure()

        fun onConnectionTimeOut()

        fun onErrorPassword()

    }
}