package com.lazyengineer.transferbyte

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Environment
import android.text.format.Formatter.formatIpAddress
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lazyengineer.transferbyte.pojoClass.ListItem
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
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

class MainActivity : AppCompatActivity() {
    //declaring all member variables
    var arrays:ArrayList<ListItem>?=null
    var adapter:AdapterClass?=null
    private val PERMISSION_REQUESTS = 1
    var count:Int=0
    var myExternalFile:File?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(!allPermissionsGranted())
            getRuntimePermissions()
        val recyclerView = recycler_view
        val layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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
        adapter=AdapterClass(arrays!!, object : onItemClickListener {
            override fun onClick(position: Int) {
                val intent = Intent(this@MainActivity, MediaViewActivity::class.java)
                intent.putExtra("media", arrays!!.get(position).mediaName)
                intent.putExtra("val", arrays!!.get(position).mediaVal)

                startActivity(intent)
                overridePendingTransition(0, 0);
            }

        })
        recyclerView.adapter=adapter


        val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =object :ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ){
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
        fun onClick(position: Int);
    }
    fun updateList(){
        runOnUiThread {
            adapter!!.notifyDataSetChanged()
        }

    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in getRequiredPermissions()!!) {
            if (!permission?.let {
                    isPermissionGranted(
                        this,
                        it
                    )
                }!!
            ) {
                return false
            }
        }
        return true
    }
    private fun getRuntimePermissions() {
        val allNeededPermissions: MutableList<String?> = java.util.ArrayList()
        for (permission in getRequiredPermissions()!!) {
            if (!permission?.let {
                    isPermissionGranted(
                        this,
                        it
                    )
                }!!
            ) {
                allNeededPermissions.add(permission)
            }
        }
        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                allNeededPermissions.toTypedArray(), PERMISSION_REQUESTS
            )
        }
    }
    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        if (ContextCompat.checkSelfPermission(context, permission)
            == PackageManager.PERMISSION_GRANTED
        ) {
            Log.i(
                "AKU",
                "Permission granted: $permission"
            )
            return true
        }
        Log.i(
            "AKU",
            "Permission NOT granted: $permission"
        )
        return false
    }


    private fun getRequiredPermissions(): Array<String?>? {
        return try {
            val info = this.packageManager
                .getPackageInfo(this.packageName, PackageManager.GET_PERMISSIONS)
            val ps = info.requestedPermissions
            if (ps != null && ps.size > 0) {
                ps
            } else {
                arrayOfNulls(0)
            }
        } catch (e: java.lang.Exception) {
            arrayOfNulls(0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.i("AKU", "Permission granted!")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    fun startKtor(){

            embeddedServer(Netty, port = 5555) {
                routing {
                    get("/"){
                        call.respondFile(getHtmlFile(applicationContext))
                    }
                    get("/text") {
                        val textval=call.parameters
                        arrays!!.add(ListItem("Text", textval["text"].toString()))
                        updateList()
                        call.respondText("Its text being called")
                    }
                    post("/text"){
                        val textval=call.receiveParameters()
                        arrays!!.add(ListItem("Text", textval["text"].toString()))
                        updateList()
                        call.respondFile(getHtmlFile(applicationContext))
                        call.respond(HttpStatusCode.OK)
                    }
                    post("/image"){
                        val multipart=call.receiveMultipart()
                       // val file=call.receiveParameters()
                        //val fileName=file["name"]
                        multipart.forEachPart { part->
                            when(part){
                                is PartData.FormItem -> {
                                    val fileDescription = part.value
                                }
                                is PartData.FileItem -> {
                                    val fileName = part.originalFileName as String
                                    var fileBytes = part.streamProvider().readBytes()
                                    val folder: File = File(
                                        Environment.getExternalStorageDirectory()
                                            .toString() + "/" + "TransferByte/Images"
                                    )
                                    Log.d("AKU", folder.path)
                                    if (!folder.exists())
                                        folder.mkdirs()

                                    val file = File(folder, fileName)
                                    if (!file.exists()) {
                                        file.createNewFile()
                                    }
                                    try {
                                        file.writeBytes(fileBytes)
                                        arrays!!.add(ListItem("Image", file.path))
                                        updateList()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }

                                    Log.d("AKU", "files" + file)
                                }
                            }
                            }
                    call.respond(HttpStatusCode.OK)
                    }
                    post("/video"){
                        val multipart=call.receiveMultipart()
                        multipart.forEachPart { part->
                            when(part){
                                is PartData.FormItem -> {
                                    val fileDescription = part.value
                                }
                                is PartData.FileItem -> {
                                    val fileName = part.originalFileName as String
                                    var fileBytes = part.streamProvider().readBytes()
                                    val folder: File = File(
                                        Environment.getExternalStorageDirectory()
                                            .toString() + "/" + "TransferByte/Videos"
                                    )
                                    Log.d("AKU", folder.path)
                                    if (!folder.exists())
                                        folder.mkdirs()

                                    val file = File(folder, fileName)
                                    if (!file.exists()) {
                                        file.createNewFile()
                                    }
                                    try {
                                        file.writeBytes(fileBytes)
                                        arrays!!.add(ListItem("Video", file.path))
                                        updateList()

                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }

                                    Log.d("AKU", "files" + file)
                                }
                            }
                        }
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