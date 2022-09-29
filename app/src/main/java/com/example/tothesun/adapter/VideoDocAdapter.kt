package com.example.tothesun.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tothesun.R

data class VideoDocDataclass(
    val image_url: String,
    val title: String,
    val description: String,
)

class VideoDocAdapter(
private val context: Context,
private val recycler_view: RecyclerView,
private val activityView: View,
column_count: Int = 1
) : RecyclerView.Adapter<VideoDocAdapter.ViewHolder>() {
    private val array_list: ArrayList<VideoDocDataclass> = ArrayList()
    private lateinit var click_listener: OnClickListener

    init {
        this.recycler_view.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        this.recycler_view.adapter = this
    }

    interface OnClickListener {
        fun onItemClick(position: Int, view: View)
        fun onLongItemClick(position: Int, view: View)

    }

    fun setOnItemClickListener(listener: OnClickListener) {
        click_listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.image_gallery, parent, false)
        return ViewHolder(view, click_listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //bind data to viewholder
        val imageitem = array_list[position]
    }

    override fun getItemCount(): Int {
        return array_list.size
    }

    fun clearAdapter() {
        //remove all item from your recyclerview
        array_list.clear()
    }

    fun removeItem(index: Int) {
        array_list.removeAt(index)
    }

    fun addToAdapter(element: VideoDocDataclass) {
        // add item to your recyclerview
        array_list.add(element)
    }

    fun getItem(index: Int): VideoDocDataclass {
        return array_list[index]
    }

    fun addToAdapter(index: Int, element: VideoDocDataclass) {
        // add item to an index spot of your recyclerview
        array_list.add(index, element)
    }

    inner class ViewHolder(item_view: View, listener: OnClickListener) :
        RecyclerView.ViewHolder(item_view) {

        init {
            item_view.setOnLongClickListener {
                listener.onLongItemClick(adapterPosition, item_view)
                true
            }
            item_view.setOnClickListener {
                listener.onItemClick(adapterPosition, item_view)
            }
        }
    }
}
