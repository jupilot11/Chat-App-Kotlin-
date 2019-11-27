package com.example.chatappkotlin.view.fragment


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.chatappkotlin.R
import com.example.chatappkotlin.UserSettings
import com.example.chatappkotlin.util.Constants
import com.example.chatappkotlin.util.RealtimeService
import com.example.chatappkotlin.util.customview.CircleImageView
import com.example.chatappkotlin.util.customview.CircleImageview
import com.example.chatappkotlin.view.activity.EditProfileActivity
import com.google.firebase.database.*
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ProfileFragment : Fragment() {


    private var param1: String? = null
    private var param2: String? = null

    private var realtimeReceiver: RealtimeReceiver? = null
    private var database: FirebaseDatabase? = null
    private var table_user: DatabaseReference? = null


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
        val view = inflater.inflate(R.layout.fragment_profile, container, false)


        database = FirebaseDatabase.getInstance()
        table_user = database!!.reference.child("User Settings")

        imageView = view.findViewById(R.id.circleImageview)
        tv_followers = view.findViewById(R.id.tv_followers)
        tv_post = view.findViewById(R.id.tv_posts)
        tv_following = view.findViewById(R.id.tv_following)
        tv_nickname = view.findViewById(R.id.tv_nickname)
        edit_profile = view.findViewById(R.id.tv_edit_profile)


        edit_profile!!.setOnClickListener {


            val intent = Intent(activity, EditProfileActivity::class.java)
            intent.putExtra(Constants.INTENT_USER, userSettings)
            activity!!.startActivity(intent)
        }




        tv_followers!!.text = userSettings!!.followers.toString()
        tv_post!!.text = userSettings!!.posts.toString()
        tv_following!!.text = userSettings!!.following.toString()
        tv_nickname!!.text = userSettings!!.str_nickname.toString()


        Glide.with(activity!!.applicationContext)
            .load((userSettings!!.profile!!.arrayList!![0].uri))
            .error(R.drawable.ic_person_black_48dp)
            .into(imageView!!)

        return view


    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        registerReceiver()
        getRealtime()


    }


    override fun onDestroyView() {
        super.onDestroyView()
        unregisterReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver()
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver()
    }

    fun getRealtime() {


        val intent = Intent(activity, RealtimeService::class.java)
        intent.putExtra(Constants.INTENT_USER, userSettings)
        intent.putExtra(Constants.INTENT_TYPE, 0)
        activity!!.startService(intent)
    }

    companion object {


        var imageView: CircleImageView? = null
        var tv_followers: TextView? = null
        var tv_post: TextView? = null

        var tv_following: TextView? = null
        var tv_nickname: TextView? = null
        var userSettings: UserSettings? = null
        var edit_profile : TextView? = null


        var type : Int? = null

        @JvmStatic
        fun newInstance(param1: UserSettings?, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }



    }

    private fun unregisterReceiver() {
        if (realtimeReceiver != null) {
            activity!!.unregisterReceiver(realtimeReceiver)
            realtimeReceiver = null
        }
    }

    private fun registerReceiver() {
        if (realtimeReceiver == null) {
            val intentFilterPHP =
                IntentFilter(RealtimeReceiver.TAGGED_RECEIVER)
            intentFilterPHP.addCategory(Intent.CATEGORY_DEFAULT)
            realtimeReceiver = RealtimeReceiver()
            activity!!.registerReceiver(realtimeReceiver, intentFilterPHP)
        }
    }

    class RealtimeReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {

            if (intent != null) {

                userSettings = intent.getParcelableExtra(Constants.INTENT_USER)

                tv_followers!!.text = userSettings!!.followers.toString()
                tv_post!!.text = userSettings!!.posts.toString()
                tv_following!!.text = userSettings!!.following.toString()
                tv_nickname!!.text = userSettings!!.str_nickname.toString()

                Glide.with(context!!.applicationContext)
                    .load((userSettings!!.profile!!.arrayList!![0].uri))
                    .error(R.drawable.ic_person_black_48dp)
                    .into(imageView!!)
            }
        }

        companion object {

            val TAGGED_RECEIVER =
                "com.example.chatappkotlin.view.fragment.ProfileFragment"

        }

    }


}
