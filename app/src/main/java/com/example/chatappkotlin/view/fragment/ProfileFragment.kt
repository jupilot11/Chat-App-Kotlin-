package com.example.chatappkotlin.view.fragment


import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat.getSystemService

import com.example.chatappkotlin.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    var navigation_drawer_header: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        navigation_drawer_header =
            view.navigationView_main_menu?.inflateHeaderView(R.layout.navigation_drawer_header)

        actionBarDrawerToggle = setupDrawerToggle()
        view.drawer_layout.addDrawerListener(actionBarDrawerToggle!!)


        view.imageView3.setOnClickListener {

            view.drawer_layout.openDrawer(Gravity.RIGHT)
        }

        return view
    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun setupDrawerToggle(): ActionBarDrawerToggle {
        return object : ActionBarDrawerToggle(
            activity,
            drawer_layout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        ) {
            override fun onDrawerClosed(view: View) {
                activity!!.supportInvalidateOptionsMenu()
            }

            override fun onDrawerOpened(drawerView: View) {
                activity!!.supportInvalidateOptionsMenu()
                val imm =
                    activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(drawer_layout.applicationWindowToken, 0)
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                constraintLayout.translationX = (-slideOffset) * (drawerView.width)
                drawer_layout.bringChildToFront(drawerView)
                drawer_layout.requestLayout()
            }
        }
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        actionBarDrawerToggle!!.onConfigurationChanged(newConfig)

    }


}
