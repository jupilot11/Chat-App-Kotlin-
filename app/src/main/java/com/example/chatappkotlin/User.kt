package com.example.chatappkotlin

import android.os.Parcel
import android.os.Parcelable

class User : Parcelable {


    var str_username: String? = null
    var str_password: String? = null
    var str_userId: String? = null


    constructor(parcel: Parcel) : this() {
        str_username = parcel.readString()
        str_password = parcel.readString()
        str_userId = parcel.readString()
    }

    constructor(str_username: String?, str_password: String?, str_userId: String?) {
        this.str_username = str_username
        this.str_password = str_password
        this.str_userId = str_userId
    }

    constructor()

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(str_username)
        parcel.writeString(str_password)
        parcel.writeString(str_userId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }


}