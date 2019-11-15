package com.example.chatappkotlin.model

import com.example.chatappkotlin.FirebaseMethods
import com.example.chatappkotlin.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupInteractor {

    private var firebaseMethods: FirebaseMethods? = null
    private var database: FirebaseDatabase? = null
    private var table_user: DatabaseReference? = null



    constructor(){

        database = FirebaseDatabase.getInstance()
        table_user = database!!.reference.child("User")
        firebaseMethods = FirebaseMethods()
    }


    interface SignupInterface {

        fun onSuccess(user: User)

        fun onFailure()

        fun onConnectionTimed()

        fun onIntentSignin()

        fun onPasswordError()

    }

    fun signUp(user: User, str_username: String, signupInterface: SignupInterface) {

        firebaseMethods?.firebaseRegistration(table_user!!, str_username, object : FirebaseMethods.RegistrationCallback{
            override fun onSuccess() {

                table_user!!.push().setValue(user).addOnCompleteListener { task ->

                    if (task.isSuccessful) {


                        signupInterface.onSuccess(user)
                    }
                }
            }

            override fun onUserExists() {

                signupInterface.onFailure()
            }


            override fun onConnectionTimeOut() {

                signupInterface.onConnectionTimed()
            }
        })

    }

    fun onHandleLoginIntent(signupInterface: SignupInterface) {

        signupInterface.onIntentSignin()
    }
}