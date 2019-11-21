package com.example.chatappkotlin.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.chatappkotlin.R
import com.example.chatappkotlin.User
import com.example.chatappkotlin.util.Constants.Companion.INTENT_USER
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {


    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        if (intent != null) {

            user = intent.getParcelableExtra(INTENT_USER)

        }

        tablayout.addOnTabSelectedListener(this)

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        when (tab?.position) {

            0 -> {

                Toast.makeText(this@HomeActivity, "Tab 1 reselect", Toast.LENGTH_SHORT)
                    .show()
            }
            1 -> {
                Toast.makeText(this@HomeActivity, "Tab 2 reselect", Toast.LENGTH_SHORT)
                    .show()

            }
            2 -> {
                Toast.makeText(this@HomeActivity, "Tab 3 reselect", Toast.LENGTH_SHORT)
                    .show()

            }
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        when (tab?.position) {

            0 -> {

                Toast.makeText(this@HomeActivity, "Tab 1", Toast.LENGTH_SHORT).show()
            }
            1 -> {
                Toast.makeText(this@HomeActivity, "Tab 2", Toast.LENGTH_SHORT).show()

            }
            2 -> {
                Toast.makeText(this@HomeActivity, "Tab 3", Toast.LENGTH_SHORT).show()

            }
        }
    }


}
