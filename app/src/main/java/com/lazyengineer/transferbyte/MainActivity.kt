package com.lazyengineer.transferbyte

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    //declaring all member variables


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = recycler_view
        val layoutManager:LinearLayoutManager= LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerView.layoutManager=layoutManager
        val arrays=ArrayList<String>();
        arrays.add("Video")
        arrays.add("Image")
        arrays.add("Text")
        arrays.add("Image")
        val adapter=AdapterClass(arrays)
        recyclerView.adapter=adapter




    }
}