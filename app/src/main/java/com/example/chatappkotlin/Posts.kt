package com.example.chatappkotlin

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

class Posts : Parcelable {

    var str_userid: String? = null
    var str_photo: String? = null
    var str_caption: String? = null
    var str_uri: Uri? = null
    var str_profimage: String? = null
    var str_nickname: String? = null


    constructor(parcel: Parcel) : this() {
        str_userid = parcel.readString()
        str_photo = parcel.readString()
        str_caption = parcel.readString()
        str_uri = parcel.readParcelable(Uri::class.java.classLoader)
        str_profimage = parcel.readString()
        str_nickname = parcel.readString()
    }

    constructor()

    constructor(str_userid: String?, str_uri: Uri?) {
        this.str_userid = str_userid
        this.str_uri = str_uri
    }

    constructor(str_userid: String?, str_caption: String?, str_uri: Uri?) {
        this.str_userid = str_userid
        this.str_caption = str_caption
        this.str_uri = str_uri
    }

    constructor(str_userid: String?, str_photo: String?, str_caption: String?) {
        this.str_userid = str_userid
        this.str_photo = str_photo
        this.str_caption = str_caption
    }

    constructor(
        str_userid: String?,
        str_photo: String?,
        str_caption: String?,
        str_profimage: String?,
        str_nickname: String?
    ) {
        this.str_userid = str_userid
        this.str_photo = str_photo
        this.str_caption = str_caption
        this.str_profimage = str_profimage
        this.str_nickname = str_nickname
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(str_userid)
        parcel.writeString(str_photo)
        parcel.writeString(str_caption)
        parcel.writeString(str_profimage)
        parcel.writeString(str_nickname)
        parcel.writeParcelable(str_uri, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Posts> {
        override fun createFromParcel(parcel: Parcel): Posts {
            return Posts(parcel)
        }

        override fun newArray(size: Int): Array<Posts?> {
            return arrayOfNulls(size)
        }
    }


}