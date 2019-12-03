package com.example.chatappkotlin

import android.os.Parcel
import android.os.Parcelable

class UserSettings : Parcelable {


    var str_email: String? = null
    var str_id: String? = null
    var str_password: String? = null
    var str_userId: String? = null
    var profile: Profile? = null
    var followers: Int? = null
    var following: Int? = null
    var posts: Int? = null
    var str_display_name: String? = null
    var str_website: String? = null
    var str_biography: String? = null
    var str_nickname: String? = null
    var str_phone: String? = null

    constructor(parcel: Parcel) : this() {
        str_email = parcel.readString()
        str_id = parcel.readString()
        str_password = parcel.readString()
        str_userId = parcel.readString()
        profile = parcel.readParcelable(Profile::class.java.classLoader)
        followers = parcel.readValue(Int::class.java.classLoader) as? Int
        following = parcel.readValue(Int::class.java.classLoader) as? Int
        posts = parcel.readValue(Int::class.java.classLoader) as? Int
        str_display_name = parcel.readString()
        str_nickname = parcel.readString()
        str_website = parcel.readString()
        str_biography = parcel.readString()
        str_phone = parcel.readString()
    }

    constructor()

    //for edit profile
    constructor(
        str_email: String?,
        str_id: String?,
        str_password: String?,
        str_userId: String?,
        profile: Profile?,
        followers: Int?,
        following: Int?,
        posts: Int?,
        str_display_name: String?,
        str_nickname: String?, str_phone: String?, str_bio: String?
    ) {
        this.str_email = str_email
        this.str_id = str_id
        this.str_password = str_password
        this.str_userId = str_userId
        this.profile = profile
        this.followers = followers
        this.following = following
        this.posts = posts
        this.str_display_name = str_display_name
        this.str_nickname = str_nickname
        this.str_phone = str_phone
        this.str_biography = str_bio
    }


    constructor(
        str_email: String?,
        str_id: String?,
        str_password: String?,
        str_userId: String?,
        profile: Profile?,
        followers: Int?,
        following: Int?,
        posts: Int?,
        str_display_name: String?,
        str_nickname: String?, str_phone: String?
    ) {
        this.str_email = str_email
        this.str_id = str_id
        this.str_password = str_password
        this.str_userId = str_userId
        this.profile = profile
        this.followers = followers
        this.following = following
        this.posts = posts
        this.str_display_name = str_display_name
        this.str_nickname = str_nickname
        this.str_phone = str_phone
    }

    constructor(
        str_email: String?,
        str_id: String?,
        str_password: String?,
        str_userId: String?,
        followers: Int?,
        following: Int?,
        posts: Int?,
        str_display_name: String?,
        str_nickname: String?,
        str_phone: String?
    ) {
        this.str_email = str_email
        this.str_id = str_id
        this.str_password = str_password
        this.str_userId = str_userId
        this.followers = followers
        this.following = following
        this.posts = posts
        this.str_display_name = str_display_name
        this.str_nickname = str_nickname
        this.str_phone = str_phone
    }
    constructor(
        str_email: String?,
        str_id: String?,
        str_password: String?,
        str_userId: String?,
        followers: Int?,
        following: Int?,
        posts: Int?,
        str_display_name: String?,
        str_nickname: String?,
        str_phone: String?,
        str_bio : String?
    ) {
        this.str_email = str_email
        this.str_id = str_id
        this.str_password = str_password
        this.str_userId = str_userId
        this.followers = followers
        this.following = following
        this.posts = posts
        this.str_display_name = str_display_name
        this.str_nickname = str_nickname
        this.str_phone = str_phone
        this.str_biography = str_bio
    }

    constructor(
        str_email: String?,
        str_display_name: String?,
        str_biography: String?,
        str_nickname: String?,
        str_phone: String?
    ) {
        this.str_email = str_email
        this.str_display_name = str_display_name
        this.str_biography = str_biography
        this.str_nickname = str_nickname
        this.str_phone = str_phone
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(str_email)
        parcel.writeString(str_id)
        parcel.writeString(str_password)
        parcel.writeString(str_userId)
        parcel.writeParcelable(profile, flags)
        parcel.writeValue(followers)
        parcel.writeValue(following)
        parcel.writeValue(posts)
        parcel.writeString(str_display_name)
        parcel.writeString(str_nickname)
        parcel.writeString(str_phone)

        parcel.writeString(str_biography)
        parcel.writeString(str_website)

    }



    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserSettings> {
        override fun createFromParcel(parcel: Parcel): UserSettings {
            return UserSettings(parcel)
        }

        override fun newArray(size: Int): Array<UserSettings?> {
            return arrayOfNulls(size)
        }
    }


}