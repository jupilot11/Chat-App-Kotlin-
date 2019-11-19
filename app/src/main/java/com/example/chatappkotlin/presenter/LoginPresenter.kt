package com.example.chatappkotlin.presenter

import android.widget.EditText
import android.widget.TextView
import com.example.chatappkotlin.util.Contract
import com.example.chatappkotlin.User
import com.example.chatappkotlin.model.LoginInteractor

class LoginPresenter : Contract.LoginContract, LoginInteractor.LoginInterface {



    private var loginView: Contract.LoginView? = null
    private var loginInteractor: LoginInteractor? = null
    private var isEmpty = false

    constructor(loginView: Contract.LoginView?) {
        this.loginView = loginView
        loginInteractor = LoginInteractor()
    }


    override fun onHandleSigIn(
        editTexts: Array<EditText>,
        textViews: Array<TextView>,
        strusername: String,
        strpassword: String
    ) {


        var ctr = 0

        for (i in editTexts.indices) {

            val strings = editTexts[i]

            if (strings.text.length < 6) {

                isEmpty = true

                loginView?.onEmpyFields(textViews, i)

                ctr++

            }else{

                if (ctr == 0){

                    isEmpty = false
                }
            }

        }
        if (!isEmpty) {

            loginInteractor?.login(strusername, strpassword, this)
        }

    }

    override fun onHandleIntent() {


        loginInteractor?.onSignupIntent(this)
    }

    override fun onSuccess(user: User) {

        loginView?.onSuccess(user)
    }

    override fun onFailure() {

        loginView?.onFailure()
    }

    override fun onConnectionTimed() {

        loginView?.onConnectionTimed()

    }

    override fun onIntentSignUp() {

        loginView?.onIntentSignUp()

    }

    override fun onPasswordError() {

        loginView?.onPasswordError()

    }

    override fun onShowProgDialog() {

        loginView?.onShowProgDialog()
    }

    override fun onDismissProgress() {

        loginView?.onDismissProgress()
    }
}