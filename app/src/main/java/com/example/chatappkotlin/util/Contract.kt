package com.example.chatappkotlin.util

import android.net.Uri
import android.widget.EditText
import android.widget.TextView
import com.example.chatappkotlin.User
import com.example.chatappkotlin.UserSettings

interface Contract {

    interface LoginView {
        fun onSuccess(user: UserSettings)

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

        fun onSuccess(user: UserSettings)

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
            uriArrayList: ArrayList<Uri>,
            user: User
        )

        fun onHandleIntent()
    }


}