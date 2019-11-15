package com.example.chatappkotlin

import android.os.Handler
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class FirebaseMethods {

    var valueEventListener1 : ValueEventListener? = null
    val valueEventListener : ValueEventListener? = null

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

            if (gotResult[0] == false) { //  Timeout

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
}