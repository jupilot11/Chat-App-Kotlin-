package com.example.chatappkotlin.view.fragment

import android.content.Context
import android.media.Image
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.example.chatappkotlin.Posts

import com.example.chatappkotlin.R
import com.example.chatappkotlin.UserSettings
import com.example.chatappkotlin.view.activity.UploadActivity
import kotlinx.android.synthetic.main.fragment_upload_stepone.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"
private const val ARG_PARAM4 = "param4"

class UploadSteptwoFragment : Fragment(){
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var userSettings: UserSettings? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            userSettings = it.getParcelable(ARG_PARAM3)
            posts = it.getParcelable(ARG_PARAM4)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_upload_steptwo, container, false)
        image_preview = view.findViewById(R.id.imageView_preview)
        editText_caption = view.findViewById(R.id.et_caption)
        return view
    }


    companion object {

        var image_preview : ImageView? = null
        var editText_caption : EditText? = null
        var posts: Posts? = null


        @JvmStatic
        fun newInstance(posts: Posts, param2: String) =
            UploadSteptwoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM4, posts)
                    putString(ARG_PARAM2, param2)

                    UploadActivity.position = 2
                }
            }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (posts != null) {

            if (posts!!.str_uri != null) {

                image_preview!!.setImageURI(posts!!.str_uri)

            }

        }

    }
}
