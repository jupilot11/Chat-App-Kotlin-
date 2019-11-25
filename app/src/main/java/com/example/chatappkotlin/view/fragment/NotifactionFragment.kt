package com.example.chatappkotlin.view.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.chatappkotlin.R
import com.example.chatappkotlin.UserSettings


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class NotifactionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var userSettings: UserSettings? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userSettings = it.getParcelable(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_notifaction, container, false)
    }



    companion object {

        @JvmStatic
        fun newInstance(userSettings: UserSettings, param2: String) =
            NotifactionFragment().apply {
                arguments = Bundle().apply {

                    putParcelable(ARG_PARAM1, userSettings)
                    putString(ARG_PARAM2, param2)

                }
            }
    }
}
