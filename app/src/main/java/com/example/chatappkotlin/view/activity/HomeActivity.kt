package com.example.chatappkotlin.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.chatappkotlin.R
import com.example.chatappkotlin.User
import com.example.chatappkotlin.util.Constants.Companion.INTENT_USER
import com.example.chatappkotlin.view.fragment.HomeFragment
import com.example.chatappkotlin.view.fragment.NotifactionFragment
import com.example.chatappkotlin.view.fragment.ProfileFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {


    var user: User? = null
    var homeFragment: Fragment? = null
    var notifFragment: Fragment? = null
    var profileFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (intent != null) {

            user = intent.getParcelableExtra(INTENT_USER)

        }


        homeFragment = HomeFragment.newInstance("", "")
        notifFragment = NotifactionFragment.newInstance("", "")
        profileFragment = ProfileFragment.newInstance("", "")

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.framelayout, homeFragment as HomeFragment, "")
        ft.commit()

        tablayout.addOnTabSelectedListener(this)
        val badge = tablayout.getTabAt(2)?.orCreateBadge
        badge?.isVisible = true
        badge?.number = 9

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

                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.framelayout, homeFragment as HomeFragment, "")
                ft.commit()

            }
            1 -> {

                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.framelayout, profileFragment as ProfileFragment, "")
                ft.commit()
            }
            2 -> {
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.framelayout, notifFragment as NotifactionFragment, "")
                ft.commit()
            }
        }
    }


}
