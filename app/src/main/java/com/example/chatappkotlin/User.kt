package com.example.chatappkotlin

import android.os.Parcel
import android.os.Parcelable

class User : Parcelable{


    var str_email: String? = null
    var str_username: String? = null
    var str_password: String? = null
    var str_userId: String? = null
    var profile : Profile? = null
    var id : String? = null
    var str_nikname : String? = null
    var str_phone : String? = null

    constructor(parcel: Parcel) : this() {
        str_email = parcel.readString()
        str_username = parcel.readString()
        str_password = parcel.readString()
        str_userId = parcel.readString()
        id = parcel.readString()
        str_nikname = parcel.readString()
        str_phone = parcel.readString()
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
        id: String?
    ) {
        this.str_email = str_email
        this.str_username = str_username
        this.str_password = str_password
        this.str_userId = str_userId
        this.id = id
    }

    constructor(
        str_email: String?,
        str_username: String?,
        str_password: String?,
        str_userId: String?,
        id: String?,
        str_nikname: String?,
        str_phone : String?
    ) {
        this.str_email = str_email
        this.str_username = str_username
        this.str_password = str_password
        this.str_userId = str_userId
        this.id = id
        this.str_nikname = str_nikname
        this.str_phone = str_phone
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(str_email)
        parcel.writeString(str_username)
        parcel.writeString(str_password)
        parcel.writeString(str_userId)
        parcel.writeString(str_nikname)
        parcel.writeString(str_phone)
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