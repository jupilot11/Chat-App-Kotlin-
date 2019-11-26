package com.example.chatappkotlin.view.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.opengl.Visibility
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.chatappkotlin.R
import com.example.chatappkotlin.User
import com.example.chatappkotlin.UserSettings
import com.example.chatappkotlin.util.Constants
import com.example.chatappkotlin.util.Constants.Companion.INTENT_TYPE
import com.example.chatappkotlin.util.Constants.Companion.INTENT_USER
import com.example.chatappkotlin.util.RealtimeService
import com.example.chatappkotlin.view.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


class HomeActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {


    var homeFragment: Fragment? = null
    var notifFragment: Fragment? = null
    var profileFragment: Fragment? = null
    var uploadFragment: UploadFragment? = null
    var searchFragment: SearchFragment? = null
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    var navigation_drawer_header: View? = null

    var realtimeReceiver: RealtimeReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (intent != null) {

            user = intent.getParcelableExtra(INTENT_USER)

        }

        registerReceiver()
        getRealtime()

        viewpagers = findViewById<ConstraintLayout>(R.id.viewpager)
        tabLayouts = findViewById<TabLayout>(R.id.tablayout)

        homeFragment = HomeFragment.newInstance("", "")
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.framelayout, homeFragment as HomeFragment, "")
        ft.commit()


        navigation_drawer_header =
            navigationView_main_menu.inflateHeaderView(R.layout.navigation_drawer_header)

        setupDrawerContent(navigationView_main_menu)
        actionBarDrawerToggle = setupDrawerToggle()

        drawer_layout?.addDrawerListener(actionBarDrawerToggle!!)

        tabLayouts.addOnTabSelectedListener(this)


        val badge = tabLayouts.getTabAt(2)?.orCreateBadge
        badge?.isVisible = true
        badge?.number = 9


        imageView3.visibility = View.GONE
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)


        imageView3.setOnClickListener {

            drawer_layout.openDrawer(Gravity.RIGHT)
        }

    }


    override fun onTabReselected(tab: TabLayout.Tab?) {
        when (tab?.position) {

            0 -> {

                Toast.makeText(this@HomeActivity, "Tab 1 reselect", Toast.LENGTH_SHORT)
                    .show()

            }
            1 -> {


                Toast.makeText(this@HomeActivity, "Tab upload", Toast.LENGTH_SHORT)
                    .show()

            }
            2 -> {


            }
            3 -> {

                Toast.makeText(this@HomeActivity, "Tab 2 reselect", Toast.LENGTH_SHORT)
                    .show()

            }
            4 -> {

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

                imageView3.visibility = View.GONE
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                homeFragment = HomeFragment.newInstance("", "")
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.framelayout, homeFragment as HomeFragment, "")
                ft.commit()


            }

            1 -> {

                imageView3.visibility = View.GONE
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

                searchFragment = SearchFragment.newInstance("", "")
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.framelayout, searchFragment as SearchFragment, "")
                ft.commit()

            }
            2 -> {

                imageView3.visibility = View.GONE
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

                uploadFragment = UploadFragment.newInstance("", "")
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.framelayout, uploadFragment as UploadFragment, "")
                ft.commit()


            }
            3 -> {

                imageView3.visibility = View.VISIBLE
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

                profileFragment = ProfileFragment.newInstance(user!!, "")
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.framelayout, profileFragment as ProfileFragment, "")
                ft.commit()

            }
            4 -> {

                imageView3.visibility = View.GONE
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

                notifFragment = NotifactionFragment.newInstance(user!!, "")
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.framelayout, notifFragment as NotifactionFragment, "")
                ft.commit()


            }
        }
    }

    companion object {

        var viewpagers: ConstraintLayout? = null
        lateinit var tabLayouts: TabLayout

        var user: UserSettings? = null
        var type: Int? = null
        var bottomNavigationMenu : BottomNavigationMenu? = null
    }

    private fun setupDrawerToggle(): ActionBarDrawerToggle {
        return object : ActionBarDrawerToggle(
            this,
            drawer_layout,
            R.string.drawer_open,
            R.string.drawer_close
        ) {
            override fun onDrawerClosed(view: View) {
                this@HomeActivity.supportInvalidateOptionsMenu()
            }

            override fun onDrawerOpened(drawerView: View) {
                this@HomeActivity!!.supportInvalidateOptionsMenu()
                val imm =
                    this@HomeActivity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(drawer_layout.applicationWindowToken, 0)
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)


                viewpager.translationX = (-slideOffset) * (drawerView.width)
                drawer_layout.bringChildToFront(drawerView)
                drawer_layout.requestLayout()
            }
        }
    }


    fun getRealtime() {


        val intent = Intent(this, RealtimeService::class.java)
        intent.putExtra(INTENT_USER, user)
        intent.putExtra(INTENT_TYPE, 1)
        startService(intent)
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { item ->
            selectDrawerItem(item)
            true
        }
    }


    fun selectDrawerItem(menuItem: MenuItem) {
        val fragment: Fragment? = null
        val fragmentClass: Class<*>

        menuItem.isChecked = false
        when (menuItem.itemId) {

            R.id.nav_saved -> {

                Toast.makeText(this, "wewe", Toast.LENGTH_SHORT).show()

            }
            R.id.nav_settings -> {
                Toast.makeText(this, "adasdas", Toast.LENGTH_SHORT).show()

            }

        }
        menuItem.isChecked = true
        drawer_layout.closeDrawers()
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        actionBarDrawerToggle!!.onConfigurationChanged(newConfig)

    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)

        actionBarDrawerToggle!!.syncState()

    }


    override fun onPause() {
        super.onPause()
        registerReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver()
    }

    private fun unregisterReceiver() {
        if (realtimeReceiver != null) {
            unregisterReceiver(realtimeReceiver)
            realtimeReceiver = null
        }
    }

    private fun registerReceiver() {
        if (realtimeReceiver == null) {
            val intentFilterPHP =
                IntentFilter(RealtimeReceiver.TAGGED_RECEIVER)
            intentFilterPHP.addCategory(Intent.CATEGORY_DEFAULT)
            realtimeReceiver = RealtimeReceiver()
            registerReceiver(realtimeReceiver, intentFilterPHP)
        }
    }

    class RealtimeReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {

            if (intent != null) {

                user = intent.getParcelableExtra(INTENT_USER)


            }
        }

        companion object {

            val TAGGED_RECEIVER =
                "com.example.chatappkotlin.view.fragment.ProfileFragment"

        }

    }


}
