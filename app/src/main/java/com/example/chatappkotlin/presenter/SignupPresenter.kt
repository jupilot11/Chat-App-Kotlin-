package com.example.chatappkotlin.presenter

import android.net.Uri
import android.widget.EditText
import android.widget.TextView
import com.example.chatappkotlin.User
import com.example.chatappkotlin.UserSettings
import com.example.chatappkotlin.model.SignupInteractor
import com.example.chatappkotlin.util.Contract

class SignupPresenter(signupActivityViews: Contract.SignupActivityView?) : Contract.SignUpActivityPresenter, SignupInteractor.SignupInterface {

    private var signupActivityView: Contract.SignupActivityView? = signupActivityViews
    private var signupInteractor: SignupInteractor? = null
    private var isEmpty = false

    init {
        signupInteractor = SignupInteractor()
    }

    override fun onHandleSignup(
        editTexts: Array<EditText>,
        textViews: Array<TextView>,
        strusername: String,
        strpass : String,
        uriArrayList: ArrayList<Uri>,
        user: User
    ) {
        var ctr = 0

        for (i in editTexts.indices) {
            val strings = editTexts[i]

            if (strings.text.length < 6) {

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

            signupInteractor?.signUp(user,uriArrayList,strpass, strusername ,this)
        }
    }

    override fun onHandleIntent() {

        signupInteractor?.onHandleLoginIntent(this)
    }

    override fun onSuccess(user: UserSettings) {

        signupActivityView?.onSuccess(user)
    }

    override fun onFailure(type : Int, strmessage : String) {

        signupActivityView?.onFailure(type, strmessage)
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