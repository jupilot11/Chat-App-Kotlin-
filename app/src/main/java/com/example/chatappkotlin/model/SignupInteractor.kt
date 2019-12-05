package com.example.chatappkotlin.model

import android.net.Uri
import android.os.Handler
import com.example.chatappkotlin.ProfilePic
import com.example.chatappkotlin.util.FirebaseMethods
import com.example.chatappkotlin.User
import com.example.chatappkotlin.UserSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*
import kotlin.collections.ArrayList

class SignupInteractor {

    private var firebaseMethods: FirebaseMethods? = null
    private var database: FirebaseDatabase? = null
    private var table_user: DatabaseReference? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private var uriArrayList1: ArrayList<Uri>? = null

    internal val gotResult = BooleanArray(1)

    constructor() {

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        table_user = database!!.reference
        firebaseMethods = FirebaseMethods()
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference


    }


    interface SignupInterface {

        fun onSuccess(user: UserSettings)

        fun onFailure(type: Int, strmessage: String)

        fun onConnectionTimed()

        fun onIntentSignin()

        fun onPasswordError()


        fun onShowProgDialog()

        fun onDismissProgress()

    }

    fun signUp(
        user: User,
        uriArrayList: ArrayList<Uri>,
        strPassword: String,
        str_username: String,
        signupInterface: SignupInterface
    ) {

        signupInterface.onShowProgDialog()
        firebaseAuth?.createUserWithEmailAndPassword(str_username, strPassword)
            ?.addOnCompleteListener {

                if (it.isSuccessful) {
                    val firebaseUser = firebaseAuth?.currentUser
                    val userId = firebaseUser?.uid

                    if (it.exception is FirebaseAuthUserCollisionException) run {

                        signupInterface.onDismissProgress()
                        signupInterface.onFailure(2, "Email already Registered")

                    } else {

                        firebaseMethods?.firebaseRegistration(
                            table_user!!.child("User"),
                            userId!!,
                            object : FirebaseMethods.RegistrationCallback {
                                override fun onSuccess() {

                                    var ctr = 0

                                    uriArrayList1 = ArrayList()

                                    for (i in 0 until uriArrayList.size) {

                                        val ref = storageReference?.child("images/" + UUID.randomUUID().toString())

                                        ref?.putFile(uriArrayList[i])?.addOnSuccessListener {
                                            ref.downloadUrl.addOnSuccessListener { uri ->

                                                uriArrayList1?.add(uri)

                                                ctr++


                                                if (ctr == 2) {

                                                    var user1: User = user
                                                    user1 = User(
                                                        user.str_email,
                                                        user.str_username,
                                                        user.str_password,
                                                        user.str_userId,
                                                        userId.toString(),
                                                        user.str_nikname,
                                                        user.str_phone
                                                    )


                                                    table_user!!.child("User").child(userId)
                                                        .setValue(user1)
                                                        .addOnCompleteListener { task ->

                                                            if (task.isSuccessful) {

                                                                var profilePic : ProfilePic? = null
                                                                var user_settings = UserSettings(
                                                                    user.str_email,
                                                                    userId.toString(),
                                                                    user.str_password,
                                                                    user.str_userId,
                                                                    0,
                                                                    0,
                                                                    0,
                                                                    user.str_username,
                                                                    user.str_nikname,
                                                                    user.str_phone
                                                                )


                                                                table_user!!.child("User Settings")
                                                                    .child(userId)
                                                                    .setValue(user_settings)
                                                                    .addOnCompleteListener {

                                                                        if (it.isSuccessful) {

                                                                            firebaseMethods?.insertProfileImages(
                                                                                table_user!!.child("User Settings"),
                                                                                user_settings,
                                                                                user,
                                                                                uriArrayList1!!,
                                                                                object :
                                                                                    FirebaseMethods.LoginCallback {
                                                                                    override fun onSuccess(userSettings: UserSettings) {

                                                                                        signupInterface.onDismissProgress()
                                                                                        signupInterface.onSuccess(
                                                                                            userSettings
                                                                                        )

                                                                                    }

                                                                                    override fun onFailure() {
                                                                                        signupInterface.onDismissProgress()
                                                                                        signupInterface.onFailure(
                                                                                            3,
                                                                                            "You failed"
                                                                                        )
                                                                                    }

                                                                                    override fun onConnectionTimeOut() {
                                                                                        signupInterface.onDismissProgress()
                                                                                        signupInterface.onConnectionTimed()

                                                                                    }

                                                                                    override fun onErrorPassword() {
                                                                                        signupInterface.onDismissProgress()
                                                                                    }
                                                                                })

                                                                        }
                                                                    }


                                                            }
                                                        }
                                                }
                                            }

                                        }

                                    }

                                }

                                override fun onUserExists() {
                                    signupInterface.onDismissProgress()
                                    signupInterface.onFailure(1, "User already exist")
                                }


                                override fun onConnectionTimeOut() {
                                    signupInterface.onDismissProgress()

                                    signupInterface.onConnectionTimed()
                                }
                            })

                    }
                } else {

                    signupInterface.onDismissProgress()
                    signupInterface.onFailure(
                        0,
                        "Email already taken or invalid. Please try another email."
                    )
                }
            }


    }

    fun onHandleLoginIntent(signupInterface: SignupInterface) {

        signupInterface.onIntentSignin()
    }


    interface ConnectionTimeoutCallback {


        fun onConnectionTimeOut()
    }

    private fun checkConnectionTimeout(connectionTimeoutCallback: ConnectionTimeoutCallback) {
        val progressRunnable = {

            if (!gotResult[0]) { //  Timeout

                connectionTimeoutCallback.onConnectionTimeOut()

            }
        }
        val pdCanceller = Handler()
        pdCanceller.postDelayed(progressRunnable, 10000)
    }
}