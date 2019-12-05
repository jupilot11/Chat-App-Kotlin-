package com.example.chatappkotlin.util

import android.net.Uri
import android.os.Handler
import com.example.chatappkotlin.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import java.util.*
import kotlin.collections.ArrayList


class FirebaseMethods {

    var valueEventListener1: ValueEventListener? = null
    var valueEventListener2: ValueEventListener? = null
    var valueEventListener3: ValueEventListener? = null
    var valueEventListener4: ValueEventListener? = null
    var valueEventListener5: ValueEventListener? = null

    var valueEventListener6: ValueEventListener? = null
    var valueEventListener7: ValueEventListener? = null


    internal val gotResult = BooleanArray(1)


    private var isnotEqual = false
    private var isEqual = false
    private var isEquals = false

    private var user: User? = null
    private var userSettings: UserSettings? = null
    private var arrayList: ArrayList<ProfilePic>? = null

    fun firebaseRegistration(
        databaseReference: DatabaseReference,
        str_username: String,
        registrationCallback: RegistrationCallback
    ) {
        valueEventListener1 = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                gotResult[0] = true
                if (dataSnapshot.childrenCount != 0L) {

                    isnotEqual = false

                    for (dataSnapshot1 in dataSnapshot.children) {

                        val user = dataSnapshot1.child("str_username").value.toString()

                        if (user.equals(str_username, ignoreCase = true)) {


                            isnotEqual = true

                            registrationCallback.onUserExists()
                            valueEventListener1?.let { databaseReference.removeEventListener(this) }
                            break

                        }
                    }

                    if (!isnotEqual) {

                        registrationCallback.onSuccess()
                        valueEventListener1?.let { databaseReference.removeEventListener(it) }
                    }

                } else {
                    registrationCallback.onSuccess()
                    valueEventListener1?.let { databaseReference.removeEventListener(it) }
                }


            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }


        databaseReference.addListenerForSingleValueEvent(valueEventListener1 as ValueEventListener)
        checkConnectionTimeout(object : ConnectionTimeoutCallback {
            override fun onConnectionTimeOut() {
                databaseReference.removeEventListener(valueEventListener1 as ValueEventListener)
                registrationCallback.onConnectionTimeOut()
            }
        })
    }

    private fun checkConnectionTimeout(connectionTimeoutCallback: ConnectionTimeoutCallback) {
        val progressRunnable = {

            if (!gotResult[0]) { //  Timeout

                connectionTimeoutCallback.onConnectionTimeOut()

            }
        }
        val pdCanceller = Handler()
        pdCanceller.postDelayed(progressRunnable, 30000)
    }


    interface ConnectionTimeoutCallback {


        fun onConnectionTimeOut()
    }

    interface RegistrationCallback {

        fun onSuccess()

        fun onUserExists()

        fun onConnectionTimeOut()
    }


    fun addnewPost(
        databaseReference: DatabaseReference,
        posts: Posts,
        addPostCallback: AddPostCallback
    ) {

        var ctr = 0
        var sum_posts = 0

        valueEventListener6 = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataSnapshot1 in dataSnapshot.children) {


                    for (dataSnaphot2 in dataSnapshot1.children) {

                        var id_num = dataSnaphot2.key.toString()

                        if (posts.str_userid.equals(id_num)) {


                        }
                    }

                }

                databaseReference.child("User Settings").child(posts.str_userid!!)
                    .addListenerForSingleValueEvent(valueEventListener7 as ValueEventListener)

            }

        }

        valueEventListener7 = object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

//                val userUpdates: MutableMap<String, Any> =
//                    HashMap()
//                userUpdates["alanisawesome/nickname"] = "Alan The Machine"
//                userUpdates["gracehop/nickname"] = "Amazing Grace"

                var posts_ = dataSnapshot.child("posts").value.toString()

                sum_posts = posts_.toInt() + 1


                databaseReference.child("User Settings").child(posts.str_userid!!).child("posts")
                    .setValue(sum_posts).addOnCompleteListener {


                        addPostCallback.onSuccess()

                        databaseReference.removeEventListener(valueEventListener7 as ValueEventListener)
                        databaseReference.removeEventListener(valueEventListener6 as ValueEventListener)


                    }

            }

        }

        databaseReference.child("Posts").child("Post" + System.currentTimeMillis())
            .child(posts.str_userid!!).setValue(posts)
            .addOnCompleteListener {

                gotResult[0] = true

                if (it.isSuccessful) {



                    databaseReference.child("Posts")
                        .addListenerForSingleValueEvent(valueEventListener6 as ValueEventListener)

                } else {

                    addPostCallback.onFailure()
                    databaseReference.removeEventListener(valueEventListener7 as ValueEventListener)
                    databaseReference.removeEventListener(valueEventListener6 as ValueEventListener)
                }
            }



        checkConnectionTimeout(object : ConnectionTimeoutCallback {
            override fun onConnectionTimeOut() {
                addPostCallback.onConnectionTimeOut()
                databaseReference.removeEventListener(valueEventListener7 as ValueEventListener)
                databaseReference.removeEventListener(valueEventListener6 as ValueEventListener)
            }
        })
    }


    fun insertSingleImage(
        storageReference: StorageReference,
        uri: Uri,
        insertToStorageCallback: InsertToStorageCallback
    ) {
        val ref = storageReference.child("images/" + UUID.randomUUID().toString())

        ref.putFile(uri).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener { uri ->

                insertToStorageCallback.onSuccess(uri)

            }

        }.addOnFailureListener {
            insertToStorageCallback.onFailure()

        }
    }

    fun insertProfileImages(
        databaseReference: DatabaseReference,
        users: UserSettings,
        user: User,
        uriArrayList1: ArrayList<Uri>,
        profilePic: ProfilePic,
        loginCallback: LoginCallback
    ) {

        valueEventListener3 = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                gotResult[0] = true

                for (dataSnapshot1 in dataSnapshot.children) {

                    val user_email = dataSnapshot1.child("str_userId").value.toString()

                    if (user_email.equals(users.str_userId)) {

                        var last_image = 0
                        var tot = last_image + 1



                        if (uriArrayList1.size == 0) {

                            databaseReference.child(dataSnapshot1.key.toString()).child("photos")
                                .child("01")
                                .setValue(profilePic).addOnSuccessListener {

                                    loginCallback.onSuccess(users)
                                    databaseReference.removeEventListener(valueEventListener3 as ValueEventListener)
                                }

                        } else {

                            var profilePics = ProfilePic(

                                uriArrayList1[0].toString(), "true", uriArrayList1[1].toString()
                            )

                            databaseReference.child(dataSnapshot1.key.toString()).child("photos")
                                .child("01")
                                .setValue(profilePics).addOnSuccessListener {

                                    loginCallback.onSuccess(users)
                                    databaseReference.removeEventListener(valueEventListener3 as ValueEventListener)
                                }

                        }


                    }
                }

            }

        }

        databaseReference.addListenerForSingleValueEvent(valueEventListener3 as ValueEventListener)
        checkConnectionTimeout(object : ConnectionTimeoutCallback {
            override fun onConnectionTimeOut() {
                loginCallback.onConnectionTimeOut()
                databaseReference.removeEventListener(valueEventListener3 as ValueEventListener)
            }
        })
    }


    fun insertProfileImages(
        databaseReference: DatabaseReference,
        users: UserSettings,
        user: User,
        uriArrayList1: ArrayList<Uri>,
        loginCallback: LoginCallback
    ) {

        valueEventListener3 = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                gotResult[0] = true

                for (dataSnapshot1 in dataSnapshot.children) {

                    val user_email = dataSnapshot1.child("str_userId").value.toString()

                    if (user_email.equals(users.str_userId)) {

                        var last_image = 0
                        var tot = last_image + 1

                        var profilePics = ProfilePic(

                            uriArrayList1[0].toString(), "true", uriArrayList1[1].toString()
                        )

                        databaseReference.child(dataSnapshot1.key.toString()).child("photos")
                            .child("01")
                            .setValue(profilePics).addOnSuccessListener {

                                loginCallback.onSuccess(users)
                                databaseReference.removeEventListener(valueEventListener3 as ValueEventListener)
                            }


                    }
                }

            }

        }

        databaseReference.addListenerForSingleValueEvent(valueEventListener3 as ValueEventListener)
        checkConnectionTimeout(object : ConnectionTimeoutCallback {
            override fun onConnectionTimeOut() {
                loginCallback.onConnectionTimeOut()
                databaseReference.removeEventListener(valueEventListener3 as ValueEventListener)
            }
        })
    }

    fun updateprofile(
        databaseReference: DatabaseReference,
        userSettings: UserSettings,
        user: User,
        updateCallback: UpdateCallback
    ) {


        valueEventListener5 = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) {

                    databaseReference.child("User").child(user.id!!).setValue(user)
                        .addOnCompleteListener {

                            if (it.isSuccessful) {

                                updateCallback.onSuccess(userSettings)
                                databaseReference.child("User")
                                    .removeEventListener(valueEventListener5 as ValueEventListener)

                            } else {

                                updateCallback.onFailure()
                                databaseReference.child("User")
                                    .removeEventListener(valueEventListener5 as ValueEventListener)
                            }
                        }
                }
            }

        }



        databaseReference.child("User")
            .addListenerForSingleValueEvent(valueEventListener5 as ValueEventListener)


        checkConnectionTimeout(object : ConnectionTimeoutCallback {
            override fun onConnectionTimeOut() {
                updateCallback.onConnectionTimeOut()
                databaseReference.child("User")
                    .removeEventListener(valueEventListener5 as ValueEventListener)

            }
        })
    }

    fun firebaseLogin(
        databaseReference: DatabaseReference,
        str_username: String,
        str_password: String,
        loginCallback: LoginCallback
    ) {


        valueEventListener2 = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                gotResult[0] = true
                if (dataSnapshot.childrenCount != 0L) {

                    isEqual = false
                    for (dataSnapshot1 in dataSnapshot.children) {

                        val username = dataSnapshot1.child("str_email").value.toString()

                        if (username.equals(str_username, ignoreCase = true)) {

                            isEqual = true


                            var email = dataSnapshot1.child("str_email").value.toString()
                            var userid = dataSnapshot1.child("str_userId").value.toString()
                            var name = dataSnapshot1.child("str_username").value.toString()
                            var password = dataSnapshot1.child("str_password").value.toString()
                            var id = dataSnapshot1.child("id").value.toString()
                            var str_nickname = dataSnapshot.child("str_nikname").toString()
                            var str_phone = dataSnapshot.child("str_phone").toString()

                            user = User(email, name, password, userid, id, str_nickname, str_phone)


                            break


                        }

                    }

                    if (isEqual) {

                        valueEventListener2?.let {
                            databaseReference.child("User")
                                .removeEventListener(it)
                        }
                        if (user?.str_password.equals(str_password)) {


                            databaseReference.child("User Settings")
                                .addValueEventListener(valueEventListener4 as ValueEventListener)


                        } else {

                            loginCallback.onErrorPassword()
                            valueEventListener2?.let {
                                databaseReference.child("User").removeEventListener(it)
                            }
                            valueEventListener4?.let {
                                databaseReference.child("User Settings")
                                    .removeEventListener(it)
                            }
                        }
                    } else {
                        loginCallback.onFailure()
                        valueEventListener2?.let {
                            databaseReference.child("User").removeEventListener(it)
                        }
                        valueEventListener4?.let {
                            databaseReference.child("User Settings")
                                .removeEventListener(it)
                        }
                    }


                } else {

                    loginCallback.onFailure()
                    valueEventListener2?.let {
                        databaseReference.child("User").removeEventListener(it)
                    }
                    valueEventListener4?.let {
                        databaseReference.child("User Settings")
                            .removeEventListener(it)
                    }

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        valueEventListener4 = object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnaphot: DataSnapshot) {


                if (dataSnaphot.childrenCount != 0L) {

                    isEquals = false

                    for (datasnaphot2 in dataSnaphot.children) {

                        var id =
                            datasnaphot2.child("str_id").value.toString()

                        if (user!!.id.equals(id)) {


                            isEquals = true


                            var followers = datasnaphot2.child("followers")
                                .value.toString()
                            var posts =
                                datasnaphot2.child("posts").value.toString()
                            var following = datasnaphot2.child("following")
                                .value.toString()
                            var fullname =
                                datasnaphot2.child("str_display_name")
                                    .value.toString()
                            var nickname =
                                datasnaphot2.child("str_nickname")
                                    .value.toString()
                            var str_phone =
                                datasnaphot2.child("str_phone")
                                    .value.toString()
                            var str_bio = datasnaphot2.child("str_biography")
                                .value.toString()

                            for (datasnaphot3 in datasnaphot2.child("photos").children) {


                                arrayList = ArrayList()

                                var users: User? = user


                                for (i in 0 until datasnaphot2.child("photos").childrenCount) {

                                    var bool_profile =
                                        datasnaphot3.child("_profile")
                                            .value.toString()
                                    var orig_uri =
                                        datasnaphot3.child("orig_uri")
                                            .value.toString()
                                    var cropper_uri =
                                        datasnaphot3.child("uri")
                                            .value.toString()

                                    var pic = ProfilePic(
                                        cropper_uri,
                                        bool_profile,
                                        orig_uri
                                    )

                                    arrayList!!.add(pic)
                                }

                                var profile = Profile(arrayList)


                                userSettings = UserSettings(
                                    user!!.str_email,
                                    user!!.id,
                                    user!!.str_password,
                                    user!!.str_userId,
                                    profile,
                                    followers.toInt(),
                                    following.toInt(),
                                    posts.toInt(),
                                    fullname,
                                    nickname,
                                    str_phone,
                                    str_bio
                                )

                                break

                            }


                        }
                    }

                    if (isEquals) {
                        valueEventListener4?.let {
                            databaseReference.child("User Settings")
                                .removeEventListener(it)
                        }
                        loginCallback.onSuccess(userSettings!!)


                    }
                }


            }

        }


        databaseReference.child("User")
            .addListenerForSingleValueEvent(valueEventListener2 as ValueEventListener)

        checkConnectionTimeout(object : ConnectionTimeoutCallback {
            override fun onConnectionTimeOut() {
                loginCallback.onConnectionTimeOut()
                databaseReference.child("User")
                    .removeEventListener(valueEventListener2 as ValueEventListener)
                databaseReference.child("User Settings")
                    .removeEventListener(valueEventListener4 as ValueEventListener)
            }
        })
    }


    interface LoginCallback {
        fun onSuccess(userSettings: UserSettings)

        fun onFailure()

        fun onConnectionTimeOut()

        fun onErrorPassword()

    }

    interface UpdateCallback {
        fun onSuccess(userSettings: UserSettings)

        fun onFailure()

        fun onConnectionTimeOut()

        fun onErrorPassword()

    }

    interface InsertToStorageCallback {

        fun onSuccess(uri: Uri)
        fun onFailure()
        fun onConnectionTimeOut()

    }

    interface AddPostCallback {

        fun onSuccess()
        fun onFailure()
        fun onConnectionTimeOut()

    }


}