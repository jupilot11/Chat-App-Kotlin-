package com.example.chatappkotlin.util

import android.net.Uri
import android.os.Handler
import com.example.chatappkotlin.ProfilePic
import com.example.chatappkotlin.User
import com.google.firebase.database.*

class FirebaseMethods {

    var valueEventListener1: ValueEventListener? = null
    var valueEventListener2: ValueEventListener? = null
    var valueEventListener3: ValueEventListener? = null
    var valueEventListener4: ValueEventListener? = null

    internal val gotResult = BooleanArray(1)


    private var isnotEqual = false
    private var isEqual = false
    private var user: User? = null


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

    fun insertProfileImage(
        databaseReference: DatabaseReference,
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

                                loginCallback.onSuccess(user)
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
                    for (dataSnapshot1 in dataSnapshot.children) {

                        val username = dataSnapshot1.child("str_email").value.toString()

                        if (username.equals(str_username, ignoreCase = true)) {

                            isEqual = true



                            for (datasnaphot2 in dataSnapshot1.child("photos").children) {

                                var email = dataSnapshot1.child("str_email").value.toString()
                                var userid = dataSnapshot1.child("str_userId").value.toString()
                                var name = dataSnapshot1.child("str_username").value.toString()
                                var password = dataSnapshot1.child("str_password").value.toString()

    

                                user = User(email, name, password, userid)

                                break

                            }


                        }

                    }

                    if (isEqual) {

                        if (user?.str_password.equals(str_password)) {
                            loginCallback.onSuccess(user)
                            valueEventListener2?.let { databaseReference.removeEventListener(it) }
                        } else {

                            loginCallback.onErrorPassword()
                            valueEventListener2?.let { databaseReference.removeEventListener(it) }
                        }
                    } else {
                        loginCallback.onFailure()
                        valueEventListener2?.let { databaseReference.removeEventListener(it) }
                    }


                } else {

                    loginCallback.onFailure()
                    valueEventListener2?.let { databaseReference.removeEventListener(it) }

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
        fun onSuccess(user: User?)

        fun onFailure()

        fun onConnectionTimeOut()

        fun onErrorPassword()

    }
}