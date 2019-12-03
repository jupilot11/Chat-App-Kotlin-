package com.example.chatappkotlin.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.chatappkotlin.R
import com.example.chatappkotlin.view.fragment.UploadSteponeFragment
import com.example.chatappkotlin.view.fragment.UploadSteptwoFragment
import kotlinx.android.synthetic.main.activity_upload.*

class UploadActivity : AppCompatActivity() {

    var uploadSteponeFragment: Fragment? = null
    var uploadSteptwoFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)


        position = 1
        uploadSteponeFragment = UploadSteponeFragment.newInstance("", "")
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.framelayout_upload, uploadSteponeFragment as UploadSteponeFragment, "")
        ft.commit()



        image_proceed.setOnClickListener {


            if(position == 1){

                position = 2
                uploadSteptwoFragment = UploadSteptwoFragment.newInstance("", "")
                val ft = supportFragmentManager.beginTransaction()
//                ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right)
                ft.replace(R.id.framelayout_upload, uploadSteptwoFragment as UploadSteptwoFragment, "")
                ft.commit()

            }else{

                Toast.makeText(this, "Mulapas naka woie", Toast.LENGTH_SHORT).show()


            }


        }

        img_view_close.setOnClickListener {


            if (position == 1){


                Toast.makeText(this, "POs 1", Toast.LENGTH_SHORT).show()

            }else{
                position = 1
                uploadSteponeFragment = UploadSteponeFragment.newInstance("", "")
                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.framelayout_upload, uploadSteponeFragment as UploadSteponeFragment, "")
                ft.commit()
            }
        }
    }

    companion object {

        var position = 0

    }
}
