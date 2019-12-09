package com.example.chatappkotlin.view.fragment


import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatappkotlin.Posts
import com.example.chatappkotlin.R
import com.example.chatappkotlin.UserSettings
import com.example.chatappkotlin.util.Constants
import com.example.chatappkotlin.util.RealtimeService
import com.example.chatappkotlin.util.adapter.UserPostsAdapter
import com.example.chatappkotlin.util.customview.CircleImageView
import com.example.chatappkotlin.view.activity.EditProfileActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_profile.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ProfileFragment : Fragment() {


    private var param1: String? = null
    private var param2: String? = null

    private var realtimeReceiver: RealtimeReceiver? = null
    private var database: FirebaseDatabase? = null
    private var table_user: DatabaseReference? = null
    private var arrayListPost: ArrayList<Posts>? = null

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
        table_user = database!!.reference

        imageView = view.findViewById(R.id.circleImageview)
        followers = view.findViewById(R.id.tv_followers)
        posts = view.findViewById(R.id.tv_posts)
        following = view.findViewById(R.id.tv_following)
        nickname = view.findViewById(R.id.tv_nickname)
        bio = view.findViewById(R.id.tv_bio)
        edit_profile = view.findViewById(R.id.tv_edit_profile)
        recyclerView = view.findViewById(R.id.post_recyclerView)

        edit_profile!!.setOnClickListener {


            val intent = Intent(activity, EditProfileActivity::class.java)
            intent.putExtra(Constants.INTENT_USER, userSettings)
            startActivityForResult(intent, 10)
        }



        if (userSettings!!.str_biography.equals("") || userSettings!!.str_biography.equals("null")) {

            bio!!.visibility = View.GONE

        } else {
            bio!!.visibility = View.VISIBLE
            bio!!.text = userSettings!!.str_biography.toString()
        }

        followers!!.text = userSettings!!.followers.toString()
        posts!!.text = userSettings!!.posts.toString()
        following!!.text = userSettings!!.following.toString()
        nickname!!.text = userSettings!!.str_nickname.toString()


        Glide.with(activity!!.applicationContext)
            .load((userSettings!!.profile!!.arrayList!![0].uri))
            .error(R.drawable.ic_person_black_48dp)
            .into(imageView!!)

        return view


    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        registerReceiver()
        getRealtime(userSettings!!)

        recyclerView!!.layoutManager = GridLayoutManager(activity, 3)

        loadPosts()


//        post_recyclerView.layoutManager = LinearLayoutManager(activity)



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

    fun getRealtime(userSettings: UserSettings) {


        val intent = Intent(activity, RealtimeService::class.java)
        intent.putExtra(Constants.INTENT_USER, userSettings)
        intent.putExtra(Constants.INTENT_TYPE, 0)
        activity!!.startService(intent)
    }

    companion object {


        var imageView: CircleImageView? = null
        var followers: TextView? = null
        var posts: TextView? = null
        var following: TextView? = null
        var nickname: TextView? = null
        var bio: TextView? = null
        var userSettings: UserSettings? = null
        var edit_profile: TextView? = null
        var recyclerView : RecyclerView? = null

        var type: Int? = null

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



                if (userSettings!!.str_biography.equals("") || userSettings!!.str_biography.equals("null")) {

                    bio!!.visibility = View.GONE

                } else {
                    bio!!.visibility = View.VISIBLE
                    bio!!.text = userSettings!!.str_biography.toString()
                }

                followers!!.text = userSettings!!.followers.toString()
                posts!!.text = userSettings!!.posts.toString()
                following!!.text = userSettings!!.following.toString()
                nickname!!.text = userSettings!!.str_nickname.toString()

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10) {

            if (resultCode == Activity.RESULT_OK) {


                var settings: UserSettings = data!!.getParcelableExtra(Constants.INTENT_USER)

                getRealtime(settings)
            }
        }
    }


    private fun loadPosts() {

        table_user!!.child("Posts").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                arrayListPost = ArrayList()

                for (datasnapshot1 in dataSnapshot.children) {


                    for (datasnaphot2 in datasnapshot1.children) {


                        var user_key = datasnaphot2.key.toString()

                        if (userSettings!!.str_id!! == user_key) {


                            var caption = datasnaphot2.child("str_caption").value.toString()
                            var nickname = datasnaphot2.child("str_nickname").value.toString()
                            var photo_post = datasnaphot2.child("str_photo").value.toString()
                            var prof_image = datasnaphot2.child("str_profimage").value.toString()
                            var userid = datasnaphot2.child("str_userid").value.toString()


                            var post = Posts(userid, photo_post, caption, prof_image, nickname)

                            arrayListPost!!.add(post)
                        }
                    }
                }
                recyclerView!!.adapter = UserPostsAdapter(arrayListPost!!, activity!!)

            }
        })



    }
}
