package com.example.chatappkotlin.model

import android.net.Uri
import com.example.chatappkotlin.util.FirebaseMethods
import com.example.chatappkotlin.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
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
    private var isEmpty = false

    constructor() {

        firebaseAuth = FirebaseAuth.getInstance()

        database = FirebaseDatabase.getInstance()
        table_user = database!!.reference.child("User")
        firebaseMethods = FirebaseMethods()
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference


    }


    interface SignupInterface {

        fun onSuccess(user: User)

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
                            table_user!!,
                            userId!!,
                            object : FirebaseMethods.RegistrationCallback {
                                override fun onSuccess() {


                                    val imageNmae = UUID.randomUUID().toString()
                                    val ref =
                                        storageReference?.child("images/" + UUID.randomUUID().toString())

                                    var ctr = 0

                                    for (i in uriArrayList.indices) {



                                        ref?.putFile(uriArrayList[i])?.addOnSuccessListener {

                                            ref.downloadUrl.addOnSuccessListener { uri ->

                                                uriArrayList1 = ArrayList()
                                                uriArrayList1?.add(uri)

                                                var user1: User = user
                                                isEmpty= true

                                                ctr++


                                            }
                                        }


                                    }

                                    if (ctr == 2) {


                                        signupInterface.onDismissProgress()
                                        signupInterface.onSuccess(user)


//                                        user1 = User(
//                                            user.str_email,
//                                            user.str_username,
//                                            user.str_password,
//                                            user.str_userId,
//                                            uri.toString()
//
//                                        )
//
//
//                                        table_user!!.push().setValue(user1)
//                                            .addOnCompleteListener { task ->
//
//                                                if (task.isSuccessful) {
//
//                                                    signupInterface.onDismissProgress()
//
//                                                    signupInterface.onSuccess(user1)
//                                                }
//                                            }
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
}