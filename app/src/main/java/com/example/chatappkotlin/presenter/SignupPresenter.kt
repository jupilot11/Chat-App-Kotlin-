package com.example.chatappkotlin.presenter

import android.widget.EditText
import android.widget.TextView
import com.example.chatappkotlin.User
import com.example.chatappkotlin.model.SignupInteractor
import com.example.chatappkotlin.util.Contract

class SignupPresenter : Contract.SignUpActivityPresenter, SignupInteractor.SignupInterface {


    private var signupActivityView: Contract.SignupActivityView? = null
    private var signupInteractor: SignupInteractor? = null
    private var isEmpty = false

    constructor(signupActivityViews: Contract.SignupActivityView?) {

        this.signupActivityView = signupActivityViews
        signupInteractor = SignupInteractor()
    }

    override fun onHandleSignup(
        editTexts: Array<EditText>,
        textViews: Array<TextView>,
        strusername: String,
        user: User
    ) {
        var ctr = 0

        for (i in editTexts.indices) {
            val strings = editTexts[i]

            if (strings.text.length < 8) {

                isEmpty = true

                signupActivityView?.onEmpyFields(textViews, i)
                ctr++

            } else {
                if (ctr == 0) {
                    isEmpty = false
                }
            }
        }
        if (!isEmpty) {

            signupInteractor?.signUp(user, strusername, this)
        }
    }

    override fun onHandleIntent() {

        signupInteractor?.onHandleLoginIntent(this)
    }

    override fun onSuccess(user: User) {

        signupActivityView?.onSuccess(user)
    }

    override fun onFailure() {

        signupActivityView?.onFailure()
    }

    override fun onConnectionTimed() {
        signupActivityView?.onConnectionTimed()
    }

    override fun onIntentSignin() {

        signupActivityView?.onIntentSignIn()
    }

    override fun onPasswordError() {

        signupActivityView?.onPasswordError()
    }


    override fun onShowProgDialog() {

        signupActivityView?.onShowProgDialog()
    }

    override fun onDismissProgress() {
        signupActivityView?.onDismissProgress()
    }

}