package com.example.chatappkotlin.model

import com.example.chatappkotlin.util.FirebaseMethods
import com.example.chatappkotlin.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginInteractor {

    private var firebaseMethod: FirebaseMethods? = null
    private var database: FirebaseDatabase? = null
    private var table_user: DatabaseReference? = null
    private var firebaseAuth: FirebaseAuth? = null

    constructor() {
        firebaseAuth = FirebaseAuth.getInstance()

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


        fun onShowProgDialog()

        fun onDismissProgress()
    }

    fun login(username: String, password: String, loginInterface: LoginInterface) {


        loginInterface.onShowProgDialog()
        firebaseMethod?.firebaseLogin(
            table_user!!,
            username,
            password,
            object : FirebaseMethods.LoginCallback {
                override fun onSuccess(user: User?) {

                    loginInterface.onDismissProgress()
                    loginInterface.onSuccess(user!!)
                }

                override fun onFailure() {
                    loginInterface.onDismissProgress()

                    loginInterface.onFailure()

                }

                override fun onConnectionTimeOut() {

                    loginInterface.onDismissProgress()

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