package com.example.chatappkotlin.util.adapter

import android.content.Context
import android.graphics.drawable.PictureDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.chatappkotlin.Posts
import com.example.chatappkotlin.R
import com.example.chatappkotlin.view.fragment.ProfileFragment
import kotlinx.android.synthetic.main.home_list.view.*

class UserPostsAdapter(val items : ArrayList<Posts>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_list, parent, false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(context)
            .load((items[position].str_photo))
            .error(R.drawable.ic_person_black_48dp)
            .into(holder.tvAnimalType)

    }
}




class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvAnimalType = view.img_backdrop
}