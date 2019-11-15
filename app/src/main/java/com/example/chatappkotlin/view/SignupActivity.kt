package com.example.chatappkotlin.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.chatappkotlin.Contract
import com.example.chatappkotlin.R
import com.example.chatappkotlin.TextWatcherHelper
import com.example.chatappkotlin.User
import com.example.chatappkotlin.presenter.SignupPresenter
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity(), Contract.SignupActivityView, View.OnClickListener {

    private var signuPresenter: SignupPresenter? = null

    private var str_username = ""
    private var str_password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        btn_signup.setOnClickListener(this)
        btn_signin.setOnClickListener(this)

        textWatcher()

        signuPresenter = SignupPresenter(this)


    }


    override fun onEmpyFields(textViews: Array<TextView>, i: Int) {

        textViews[i].visibility = View.VISIBLE
    }

    override fun onFailure() {
        tv_user_error.visibility = View.VISIBLE
    }

    override fun onConnectionTimed() {

        Toast.makeText(this@SignupActivity, "Connection Timed out.", Toast.LENGTH_SHORT).show()

    }

    override fun onIntentSignIn() {

        val intent = Intent(this@SignupActivity, SignInActivity::class.java)
        startActivity(intent)
    }

    override fun onPasswordError() {

        tv_user_error.visibility = View.VISIBLE
    }

    override fun onSuccess(user: User) {


//        val intent = Intent(this@SignupActivity, MessagesActivity::class.java)
//        intent.putExtra("user", user)
//        startActivity(intent)

        Toast.makeText(this@SignupActivity, "Success pota", Toast.LENGTH_SHORT).show()

    }

    override fun onClick(v: View) {

        when (v.id) {

            R.id.btn_signin -> {

                signuPresenter?.onHandleIntent()

            }

            R.id.btn_signup -> {
                str_password = et_password.text.toString()
                str_username = et_username.text.toString()

                val editTexts = arrayOf<EditText>(et_password, et_username)
                val textViews = arrayOf<TextView>(tv_user_error, tv_pass_error)

                val user = User(
                    str_username,
                    str_password,
                    "ID" + System.currentTimeMillis()
                )

                signuPresenter?.onHandleSignup(editTexts, textViews, str_username, user)

            }

        }
    }


    private fun textWatcher() {

        et_password.addTextChangedListener(
            TextWatcherHelper(
                et_password,
                this@SignupActivity,
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
                this@SignupActivity,
                object : TextWatcherHelper.OnTextChangedCallbacks {
                    override fun onTextChanged() {

                        tv_user_error.visibility = View.GONE
                    }

                }
            )
        )

    }


}
