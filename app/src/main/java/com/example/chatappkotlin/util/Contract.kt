package com.example.chatappkotlin.util

import android.widget.EditText
import android.widget.TextView
import com.example.chatappkotlin.User

interface Contract {

    interface LoginView {
        fun onSuccess(user: User)

        fun onEmpyFields(textViews: Array<TextView>, i: Int)


        fun onFailure()

        fun onConnectionTimed()

        fun onIntentSignUp()

        fun onPasswordError()

        fun onShowProgDialog()

        fun onDismissProgress()
    }

    interface LoginContract {

        fun onHandleSigIn(
            editTexts: Array<EditText>,
            textViews: Array<TextView>,
            strusername: String,
            strpassword : String
        )

        fun onHandleIntent()
    }

    interface SignupActivityView {

        fun onSuccess(user: User)

        fun onEmpyFields(textViews: Array<TextView>, i: Int)

        fun onFailure(type : Int, message : String)

        fun onConnectionTimed()

        fun onIntentSignIn()

        fun onPasswordError()

        fun onShowProgDialog()

        fun onDismissProgress()
    }

    interface SignUpActivityPresenter {

        fun onHandleSignup(
            editTexts: Array<EditText>,
            textViews: Array<TextView>,
            strusername: String,
            strpassword: String,
            user: User
        )

        fun onHandleIntent()
    }


}