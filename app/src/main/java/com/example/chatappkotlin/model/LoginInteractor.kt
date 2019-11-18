package com.example.chatappkotlin.model

import com.example.chatappkotlin.FirebaseMethods
import com.example.chatappkotlin.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginInteractor {

    private var firebaseMethod: FirebaseMethods? = null
    private var database: FirebaseDatabase? = null
    private var table_user: DatabaseReference? = null


    constructor() {

        database = FirebaseDatabase.getInstance()
        table_user = database!!.reference.child("User")
        firebaseMethod = FirebaseMethods()
    }


    interface LoginInterface {

        fun onSuccess(user: User)

        fun onFailure()

        fun onConnectionTimed()

        fun onIntentSignUp()

        fun onPasswordError()

    }

    fun login(username: String, password: String, loginInterface: LoginInterface) {

        firebaseMethod?.firebaseLogin(
            table_user!!,
            username,
            password,
            object : FirebaseMethods.LoginCallback {
                override fun onSuccess(user: User?) {

                    loginInterface.onSuccess(user!!)
                }

                override fun onFailure() {

                    loginInterface.onFailure()

                }

                override fun onConnectionTimeOut() {


                    loginInterface.onConnectionTimed()
                }

                override fun onErrorPassword() {

                    loginInterface.onPasswordError()
                }

            })

    }

    fun onSignupIntent(loginInterface: LoginInterface) {

        loginInterface.onIntentSignUp()
    }

}