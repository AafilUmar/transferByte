package com.lazyengineer.transferbyte

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.ClipboardManager
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.core.content.res.ComplexColorCompat.inflate
import kotlinx.android.synthetic.main.activity_media_view.*
import kotlinx.android.synthetic.main.image_viewer.*
import kotlinx.android.synthetic.main.item_view.*
import kotlinx.android.synthetic.main.text_viewer.*
import kotlinx.android.synthetic.main.text_viewer.view.*
import kotlinx.android.synthetic.main.video_viewer.*
import kotlinx.android.synthetic.main.video_viewer.view.*
import java.io.File

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
            val imageView=image_view
            val delete=image_delete_button
            val share=image_share_button
            delete.setOnClickListener{
                Toast.makeText(this,"Coming Soon",Toast.LENGTH_SHORT).show()
            }
            share.setOnClickListener {
                val shareIntent= Intent(Intent.ACTION_SEND)
                shareIntent.setType("image/*")
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(mediaVal))
                startActivity(shareIntent)
            }
            val bitmap=BitmapFactory.decodeFile(mediaVal)
            imageView.setImageBitmap(bitmap)
        }else if(media.equals("Video")){
            val mediaView: View =layoutInflater.inflate(R.layout.video_viewer, playerWrapper,true)
            val videoView=video_view
            val delete: Button =video_delete_button
            val share:Button=video_share_button
            delete.setOnClickListener{
                Toast.makeText(this,"Coming soon",Toast.LENGTH_SHORT).show()
            }
            share.setOnClickListener{
                val shareIntent= Intent(Intent.ACTION_SEND)
                shareIntent.setType("video/*")
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(mediaVal))
                startActivity(shareIntent)
            }
            videoView.setVideoPath(mediaVal)
            videoView.start()

        }else if(media.equals("Text")){
            val mediaView: View =layoutInflater.inflate(R.layout.text_viewer, playerWrapper,true)
            val text=text_view
            text.text=mediaVal
            text.movementMethod=ScrollingMovementMethod()
            val share=text_share_button
            share.setOnClickListener {
                val shareIntent= Intent(Intent.ACTION_SEND)
                shareIntent.setType("text/*")
                shareIntent.putExtra(Intent.EXTRA_TEXT, mediaVal)
                startActivity(shareIntent)
            }
            val copy=copy_button
            copy.setOnClickListener{
                val clipBoard:ClipboardManager= getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipBoard.text=mediaVal
                Toast.makeText(applicationContext,"Copied",Toast.LENGTH_SHORT).show()
            }
        }




    }

    override fun onPause() {
        super.onPause()
    }
}

