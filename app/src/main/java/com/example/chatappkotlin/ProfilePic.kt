package com.example.chatappkotlin

import android.os.Parcel
import android.os.Parcelable

class ProfilePic : Parcelable {


    var str_key: String? = null
    var uri: String? = null
    var is_profile: String? = null
    var orig_uri: String? = null

    constructor() {}


    constructor(parcel: Parcel) : this() {
        str_key = parcel.readString()
        uri = parcel.readString()
        is_profile = parcel.readString()
        orig_uri = parcel.readString()
    }


    constructor(str_key: String?, uri: String?, is_profile: String?, orig_uri: String?) {
        this.str_key = str_key
        this.uri = uri
        this.is_profile = is_profile
        this.orig_uri = orig_uri
    }

    constructor(uri: String?, is_profile: String?, orig_uri: String?) {
        this.uri = uri
        this.is_profile = is_profile
        this.orig_uri = orig_uri
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(str_key)
        parcel.writeString(uri)
        parcel.writeString(is_profile)
        parcel.writeString(orig_uri)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProfilePic> {
        override fun createFromParcel(parcel: Parcel): ProfilePic {
            return ProfilePic(parcel)
        }

        override fun newArray(size: Int): Array<ProfilePic?> {
            return arrayOfNulls(size)
        }
    }


}