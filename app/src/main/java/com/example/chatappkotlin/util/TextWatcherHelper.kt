package com.example.chatappkotlin.util

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.example.chatappkotlin.R

class TextWatcherHelper : TextWatcher {


    private var views: View? = null
    private var contexts: Context? = null
    private var onTextChangedCallbacks: OnTextChangedCallbacks? = null

    constructor(view: View?, context: Context?, onTextChangedCallback: OnTextChangedCallbacks?) {

        this.views = view
        this.contexts = context
        this.onTextChangedCallbacks = onTextChangedCallback
    }


    interface OnTextChangedCallbacks {

        fun onTextChanged()

    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        when (views?.id) {

            R.id.et_password -> {
                onTextChangedCallbacks?.onTextChanged()
            }
            R.id.et_username -> {
                onTextChangedCallbacks?.onTextChanged()
            }
        }

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

}