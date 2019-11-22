package com.example.chatappkotlin.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.chatappkotlin.R
import com.example.chatappkotlin.User
import com.example.chatappkotlin.presenter.LoginPresenter
import com.example.chatappkotlin.util.Constants
import com.example.chatappkotlin.util.Contract
import com.example.chatappkotlin.util.DialogHelper
import com.example.chatappkotlin.util.TextWatcherHelper
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.btn_signup
import kotlinx.android.synthetic.main.activity_login.et_password
import kotlinx.android.synthetic.main.activity_login.et_username
import kotlinx.android.synthetic.main.activity_login.tv_pass_error
import kotlinx.android.synthetic.main.activity_login.tv_user_error

class SignInActivity : AppCompatActivity(), View.OnClickListener, Contract.LoginView {

    var str_usernmame = ""
    var str_password = ""
    var loginPresenter: LoginPresenter? = null

    var dialogHelper : DialogHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_signup.setOnClickListener(this)
        btn_login.setOnClickListener(this)


        textWatcher()

        dialogHelper = DialogHelper()
        loginPresenter = LoginPresenter(this)


    }


    override fun onClick(v: View) {

        when (v.id) {

            R.id.btn_login -> {

                str_password = et_password.text.toString()
                str_usernmame = et_username.text.toString()

//                Toast.makeText(this, str_usernmame, Toast.LENGTH_SHORT).show()


                val editTexts = arrayOf<EditText>(et_username, et_password)
                val textViews = arrayOf<TextView>(tv_user_error, tv_pass_error)


                loginPresenter?.onHandleSigIn(editTexts, textViews, str_usernmame, str_password)

            }
            R.id.btn_signup -> {

                loginPresenter?.onIntentSignUp()
            }

        }

    }


    override fun onDismissProgress() {

        dialogHelper?.dismissProgressDialog()

    }

    override fun onShowProgDialog() {
        dialogHelper?.showProgressDialog(this, "Please wait...", false)

    }



    override fun onSuccess(user: User) {

        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra(Constants.INTENT_USER, user)
        startActivity(intent)

    }

    override fun onEmpyFields(textViews: Array<TextView>, i: Int) {
        textViews[i].visibility = View.VISIBLE

    }

    override fun onFailure() {
        tv_user_error.visibility = View.VISIBLE

    }

    override fun onConnectionTimed() {

        Toast.makeText(this, "Connection Timed out.", Toast.LENGTH_SHORT).show()


    }

    override fun onIntentSignUp() {

        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)

    }

    override fun onPasswordError() {
        tv_pass_error.visibility = View.VISIBLE

    }


    private fun textWatcher() {

        et_password.addTextChangedListener(
            TextWatcherHelper(
                et_password,
                this,
                object : TextWatcherHelper.OnTextChangedCallbacks {
                    override fun onTextChanged() {
                        tv_pass_error.visibility = View.GONE
                    }

                }
            )
        )

        et_username.addTextChangedListener(
            TextWatcherHelper(
                et_password,
                this,
                object : TextWatcherHelper.OnTextChangedCallbacks {
                    override fun onTextChanged() {

                        tv_user_error.visibility = View.GONE
                    }

                }
            )
        )

    }

}
