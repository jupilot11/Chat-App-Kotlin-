package com.example.chatappkotlin.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatappkotlin.Posts
import com.example.chatappkotlin.R
import com.example.chatappkotlin.User
import com.example.chatappkotlin.UserSettings
import com.example.chatappkotlin.util.DialogHelper
import com.example.chatappkotlin.util.FirebaseMethods
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.SnapshotParser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"


class HomeFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var database: FirebaseDatabase? = null
    private var table_user: DatabaseReference? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var firebaseMethods: FirebaseMethods? = null
    private var dialogHelper: DialogHelper? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var firebaseadapter: FirebaseRecyclerAdapter<*, *>? = null
    private var posts: Posts? = null
    private var userSettings: UserSettings? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            userSettings = it.getParcelable(ARG_PARAM3)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        table_user = database!!.reference
        firebaseMethods = FirebaseMethods()
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
        dialogHelper = DialogHelper()


        //        getFavorites(mParam1);


        linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager!!.reverseLayout = true
        linearLayoutManager!!.stackFromEnd = true

        recyclerView!!.layoutManager = linearLayoutManager

        recyclerView!!.setHasFixedSize(true)

        showList()
    }

    companion object {

        var recyclerView: RecyclerView? = null


        @JvmStatic
        fun newInstance(userSettings: UserSettings, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM3, userSettings)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img_backdrop: ImageView
        var tv_title: TextView


        init {
            img_backdrop = itemView.findViewById<View>(R.id.image_post) as ImageView
            tv_title = itemView.findViewById<View>(R.id.tv_caption) as TextView
        }
    }

    private fun showList() {
        var postses: Posts

        var nickname = ""
        var cropper_uri = ""
        var userid = ""
        var str_caption = ""
        var str_photo = ""

        val query: Query = table_user!!.child("Posts")
        val optionss: FirebaseRecyclerOptions<Posts> = FirebaseRecyclerOptions.Builder<Posts>()
            .setQuery(query, object : SnapshotParser<Posts> {
                override fun parseSnapshot(snapshot: DataSnapshot): Posts {

                    if (snapshot.childrenCount != 0L) {

                        for (snapshot1 in snapshot.children) {

                            userid =
                                snapshot1.child("str_userid").value.toString()
                            str_caption =
                                snapshot1.child("str_caption").value.toString()
                            str_photo =
                                snapshot1.child("str_photo").value.toString()


                        }

                        posts = Posts(userid, str_photo, str_caption)

                        return posts!!

                    } else {

                        posts = null
                        return posts!!

                    }
                }

            }).build()

        firebaseadapter = object : FirebaseRecyclerAdapter<Posts, ViewHolder>(optionss) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_post, parent, false)

                return ViewHolder(view)
            }

            override fun onBindViewHolder(holder: ViewHolder, pos: Int, posts: Posts) {


                holder.tv_title.text = posts.str_caption


                Glide.with(activity!!.applicationContext)
                    .load((posts.str_photo))
                    .error(R.drawable.ic_photo_camera_black_24dp)
                    .into(holder.img_backdrop!!)

            }

            override fun getItem(position: Int): Posts {
                return super.getItem(position)
            }
        }


        recyclerView!!.adapter = firebaseadapter


    }


    override fun onStart() {
        super.onStart()
        firebaseadapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        firebaseadapter!!.stopListening()
    }

}
