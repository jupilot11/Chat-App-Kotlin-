package com.example.chatappkotlin.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.chatappkotlin.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_login.setOnClickListener(this)
        btn_signup.setOnClickListener(this)
    }

    override fun onClick(v: View) {

        when(v.id){

            R.id.btn_login ->{

                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)

            }
            R.id.btn_signup ->{

                val intent = Intent(this, SignupActivity::class.java)
                startActivity(intent)
            }
        }

    }

}
