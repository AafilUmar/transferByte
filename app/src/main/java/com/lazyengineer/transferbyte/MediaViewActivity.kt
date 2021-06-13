package com.lazyengineer.transferbyte

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.ClipboardManager
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.core.content.res.ComplexColorCompat.inflate
import kotlinx.android.synthetic.main.activity_media_view.*
import kotlinx.android.synthetic.main.text_viewer.*
import kotlinx.android.synthetic.main.text_viewer.view.*

class MediaViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_view)
        val media:String=intent.getStringExtra("media").toString()
        val mediaVal:String=intent.getStringExtra("val").toString()
        Log.d("AKU",media)

        val playerWrapper:RelativeLayout=media_wrapper
        val mediaText=media_name
        mediaText.text=media

        if(media.equals("Image")){
            val mediaView: View =layoutInflater.inflate(R.layout.image_viewer, playerWrapper,true)

        }else if(media.equals("Video")){
            val mediaView: View =layoutInflater.inflate(R.layout.video_viewer, playerWrapper,true)
        }else if(media.equals("Text")){
            val mediaView: View =layoutInflater.inflate(R.layout.text_viewer, playerWrapper,true)
            val text=text_view
            text.text=mediaVal
            text.movementMethod=ScrollingMovementMethod()
            val copy=copy_button
            copy.setOnClickListener{
                val clipBoard:ClipboardManager= getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipBoard.text=mediaVal
                Toast.makeText(applicationContext,"Copied",Toast.LENGTH_SHORT).show()
            }
        }




    }
}

