package com.example.chatappkotlin.view.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.chatappkotlin.R
import com.example.chatappkotlin.UserSettings
import com.example.chatappkotlin.util.customview.CircleImageview
import com.google.firebase.database.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var userSettings: UserSettings? = null

    private var imageView: CircleImageview? = null
    private var tv_followers: TextView? = null
    private var tv_post: TextView? = null
    private var tv_following: TextView? = null
    private var tv_nickname: TextView? = null

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


        tv_followers!!.text = userSettings!!.followers.toString()
        tv_post!!.text = userSettings!!.posts.toString()
        tv_following!!.text = userSettings!!.following.toString()
        tv_nickname!!.text = userSettings!!.str_display_name.toString()


        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        realtimeCheck()

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: UserSettings?, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

    }


    private fun realtimeCheck() {

        table_user!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

//                if (dataSnapshot.childrenCount != 0L) {
//
//                    for (datasnapshot1 in dataSnapshot.children) {
//
//                        var id =
//                            datasnapshot1.child("str_id").value.toString()
//
//
//                        if (userSettings!!.str_id.equals(id)) {
//
//
//                            var followers = datasnapshot1.child("followers")
//                                .value.toString()
//                            var posts =
//                                datasnapshot1.child("posts").value.toString()
//
//                            var following = datasnapshot1.child("following")
//                                .value.toString()
//
//                            var nickname = datasnapshot1.child("str_display_name")
//                                .value.toString()
//
//
//                            tv_followers!!.text = followers
//                            tv_post!!.text = posts
//                            tv_following!!.text = following
//                            tv_nickname!!.text = nickname
//                        }
//                    }
//
//
//                }

            }

        })
    }

}
