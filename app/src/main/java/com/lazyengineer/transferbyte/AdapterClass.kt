package com.lazyengineer.transferbyte

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lazyengineer.transferbyte.pojoClass.ListItem

class AdapterClass(private val dataSet:ArrayList<ListItem>,private val listner:MainActivity.onItemClickListener):
    RecyclerView.Adapter<AdapterClass.ViewHolder>() {




   class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
       val textView:TextView
       val imageView:ImageView
       init {
           textView=view.findViewById(R.id.media_type)
           imageView=view.findViewById(R.id.media_image)
       }


   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text=dataSet.get(position).mediaName
       holder.itemView.setOnClickListener {
           listner.onClick(position)
       }


    }

    override fun getItemCount(): Int {
    return dataSet.size
    }
}