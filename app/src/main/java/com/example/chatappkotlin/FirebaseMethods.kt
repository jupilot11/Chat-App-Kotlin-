package com.example.chatappkotlin

import android.os.Handler
import com.google.firebase.database.*

class FirebaseMethods {

    var valueEventListener1 : ValueEventListener? = null
    var valueEventListener2 : ValueEventListener? = null

    internal val gotResult = BooleanArray(1)


    private var isnotEqual = false
    private var isEqual = false
    private var user: User? = null


    fun  firebaseRegistration(databaseReference: DatabaseReference, str_username : String, registrationCallback: RegistrationCallback ){


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
        pdCanceller.postDelayed(progressRunnable, 10000)
    }


    interface ConnectionTimeoutCallback {


        fun onConnectionTimeOut()
    }

    interface RegistrationCallback {

        fun onSuccess()

        fun onUserExists()

        fun onConnectionTimeOut()
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

                        val username = dataSnapshot1.child("str_username").value.toString()

                        if (username.equals(str_username, ignoreCase = true)) {

                            isEqual = true

                            user = dataSnapshot1.getValue(User::class.java)

                            break
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