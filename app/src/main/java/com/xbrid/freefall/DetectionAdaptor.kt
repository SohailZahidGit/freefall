package com.xbrid.freefall

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xbrid.freefalldetector.utils.DetectionEvent


class DetectionAdaptor(private val listData: ArrayList<DetectionEvent>) :
    RecyclerView.Adapter<DetectionAdaptor.DetectionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetectionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.item_list, parent, false)
        return DetectionViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: DetectionViewHolder, position: Int) {
        holder.onBind(listData[position])
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    class DetectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView.rootView) {
        var textView: TextView = itemView.findViewById(R.id.title)

        fun onBind(item: DetectionEvent) {
            textView.text = "Time ->   ${item.timeStamp} duration ->   ${item.duration}"
        }
    }

}