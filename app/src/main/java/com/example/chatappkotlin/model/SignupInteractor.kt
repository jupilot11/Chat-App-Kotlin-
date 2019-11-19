package com.example.chatappkotlin.model

import com.example.chatappkotlin.util.FirebaseMethods
import com.example.chatappkotlin.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupInteractor {

    private var firebaseMethods: FirebaseMethods? = null
    private var database: FirebaseDatabase? = null
    private var table_user: DatabaseReference? = null
    private var firebaseAuth: FirebaseAuth? = null


    constructor() {

        firebaseAuth = FirebaseAuth.getInstance()

        database = FirebaseDatabase.getInstance()
        table_user = database!!.reference.child("User")
        firebaseMethods = FirebaseMethods()
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

                                    signupInterface.onDismissProgress()
                                    table_user!!.push().setValue(user)
                                        .addOnCompleteListener { task ->

                                            if (task.isSuccessful) {


                                                signupInterface.onSuccess(user)
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
}