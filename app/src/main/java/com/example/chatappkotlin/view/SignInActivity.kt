package com.example.chatappkotlin.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.chatappkotlin.R
import kotlinx.android.synthetic.main.activity_login.*

class SignInActivity : AppCompatActivity(), View.OnClickListener {

    var str_usernmame = ""
    var str_password  = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_signup.setOnClickListener(this)
        btn_login.setOnClickListener(this)


    }



    override fun onClick(v: View) {

        when(v.id){

            R.id.btn_login ->{

                str_password = et_password.text.toString()
                str_usernmame = et_username.text.toString()

                Toast.makeText(this, str_usernmame, Toast.LENGTH_SHORT).show()


            }
            R.id.btn_signup ->{


                val intent = Intent(this, SignupActivity::class.java)
                startActivity(intent)
            }

        }

    }
}
