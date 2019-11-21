package com.example.chatappkotlin

import android.os.Parcel
import android.os.Parcelable

class User : Parcelable{


    var str_email: String? = null
    var str_username: String? = null
    var str_password: String? = null
    var str_userId: String? = null
    var str_profile: String? = null
    var profile : Profile? = null

    constructor(parcel: Parcel) : this() {
        str_email = parcel.readString()
        str_username = parcel.readString()
        str_password = parcel.readString()
        str_userId = parcel.readString()
        str_profile = parcel.readString()
        profile = parcel.readParcelable(Profile::class.java.classLoader)
    }


    constructor(
        strEmail: String?,
        str_username: String?,
        str_password: String?,
        str_userId: String?
    ) {
        this.str_email = strEmail
        this.str_username = str_username
        this.str_password = str_password
        this.str_userId = str_userId
    }

    constructor()

    constructor(
        str_email: String?,
        str_username: String?,
        str_password: String?,
        str_userId: String?,
        str_profile: String?
    ) {
        this.str_email = str_email
        this.str_username = str_username
        this.str_password = str_password
        this.str_userId = str_userId
        this.str_profile = str_profile
    }

    constructor(
        str_email: String?,
        str_username: String?,
        str_password: String?,
        str_userId: String?,
        profile: Profile?
    ) {
        this.str_email = str_email
        this.str_username = str_username
        this.str_password = str_password
        this.str_userId = str_userId
        this.profile = profile
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(str_email)
        parcel.writeString(str_username)
        parcel.writeString(str_password)
        parcel.writeString(str_userId)
        parcel.writeString(str_profile)
        parcel.writeParcelable(profile, flags)
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