package com.lazyengineer.transferbyte

import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ParcelFileDescriptor.open
import android.text.format.Formatter.formatIpAddress

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lazyengineer.transferbyte.pojoClass.ListItem
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.utils.io.errors.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import android.text.format.Formatter
import android.widget.Toast
import io.ktor.http.*

class MainActivity : AppCompatActivity() {
    //declaring all member variables
    var arrays:ArrayList<ListItem>?=null
    var adapter:AdapterClass?=null
    var count:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = recycler_view
        val layoutManager= LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerView.layoutManager=layoutManager
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ipAddress: String = formatIpAddress(wifiManager.connectionInfo.ipAddress)
        val titleName=title_name
        titleName.setOnClickListener{
            if(count==5) {
                Toast.makeText(this, "Ip:$ipAddress", Toast.LENGTH_LONG).show()
                count=0
            }
            count++
        }
        Thread { startKtor() }.start()

        arrays=ArrayList();
        adapter=AdapterClass(arrays!!,object :onItemClickListener{
            override fun onClick(position: Int) {
               val intent=Intent(this@MainActivity,MediaViewActivity::class.java)
                intent.putExtra("media",arrays!!.get(position).mediaName)
                intent.putExtra("val",arrays!!.get(position).mediaVal)

                startActivity(intent)
                overridePendingTransition(0, 0);
            }

        })
        recyclerView.adapter=adapter


        val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =object :ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position=viewHolder.bindingAdapterPosition
                arrays!!.removeAt(position)
                adapter!!.notifyItemRemoved(position)
                adapter!!.notifyDataSetChanged()

            }

        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)



    }



    interface onItemClickListener{
        fun onClick(position:Int);
    }
    fun updateList(){
        runOnUiThread {
            adapter!!.notifyDataSetChanged()
        }

    }

    fun startKtor(){

            embeddedServer(Netty, port = 5555) {
                routing {
                    get("/"){
                        call.respondFile(getHtmlFile(applicationContext))
                    }
                    get ("/text") {
                        val textval=call.parameters
                        arrays!!.add(ListItem("Text",textval["text"].toString()))
                        updateList()
                        call.respondText("Its text being called")
                    }
                    post("/text"){
                        val textval=call.receiveParameters()
                        arrays!!.add(ListItem("Text",textval["text"].toString()))
                        updateList()
                        call.respondFile(getHtmlFile(applicationContext))
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }.start(wait = true)
        }
    @Throws(IOException::class)
    fun getHtmlFile(context: Context): File = File(context.cacheDir, "index.html")
        .also {
            it.outputStream().use { cache -> context.assets.open("index.html").use { it.copyTo(cache) } }
        }


}