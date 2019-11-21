package com.example.chatappkotlin

import android.os.Parcel
import android.os.Parcelable

class Profile : Parcelable{


    var arrayList : ArrayList<ProfilePic>? = null

    constructor(parcel: Parcel) : this() {

    }


    constructor(arrayList: ArrayList<ProfilePic>?) {
        this.arrayList = arrayList
    }

    constructor()

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Profile> {
        override fun createFromParcel(parcel: Parcel): Profile {
            return Profile(parcel)
        }

        override fun newArray(size: Int): Array<Profile?> {
            return arrayOfNulls(size)
        }
    }


}