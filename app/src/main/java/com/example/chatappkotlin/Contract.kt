package com.example.chatappkotlin

import android.widget.EditText
import android.widget.TextView
import com.example.chatappkotlin.User

interface Contract {


    interface SignupActivityView {

        fun onSuccess(user: User)

        fun onEmpyFields(textViews: Array<TextView>, i: Int)

        fun onFailure()

        fun onConnectionTimed()

        fun onIntentSignIn()

        fun onPasswordError()

    }

    interface SignUpActivityPresenter {

        fun onHandleSignup(
            editTexts: Array<EditText>,
            textViews: Array<TextView>,
            strusername: String,
            user: User
        )

        fun onHandleIntent()
    }


}