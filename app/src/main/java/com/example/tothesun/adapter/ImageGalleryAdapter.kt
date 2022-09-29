package com.example.tothesun.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tothesun.R
import com.example.tothesun.tools.Tools
import com.squareup.picasso.Picasso

data class GalleryDataclass(
    val image_url: String,
    val title: String,
    val description: String,
)

class ImageGalleryAdapter(
    private val context: Context,
    private val recycler_view: RecyclerView,
    private val activityView: View,
    column_count: Int = 1
) : RecyclerView.Adapter<ImageGalleryAdapter.ViewHolder>() {
    private val array_list: ArrayList<GalleryDataclass> = ArrayList()
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

        Picasso.get().load(array_list[position].image_url).into(holder.image_uri)
        Tools.debugMessage(imageitem.image_url)
        holder.title.text = imageitem.title
        holder.hidden_description.text = imageitem.description
        holder.imageinfo.setOnClickListener {
            Tools.popUpWindow(context, activityView, lambda = { v, popup ->
                val tv = v.findViewById<TextView>(R.id.id_tv_popup_message)
                tv.text = context.getString(
                    R.string.string_formatter,
                    holder.hidden_description.text.toString()
                )
            }, dismiss = {
                holder.title.visibility = TextView.VISIBLE
                holder.imageinfo.visibility = ImageButton.VISIBLE
            })
            holder.title.visibility = TextView.INVISIBLE
            holder.imageinfo.visibility = ImageButton.INVISIBLE
        }
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

    fun addToAdapter(element: GalleryDataclass) {
        // add item to your recyclerview
        array_list.add(element)
    }

    fun getItem(index: Int): GalleryDataclass {
        return array_list[index]
    }

    fun addToAdapter(index: Int, element: GalleryDataclass) {
        // add item to an index spot of your recyclerview
        array_list.add(index, element)
    }

    inner class ViewHolder(item_view: View, listener: OnClickListener) :
        RecyclerView.ViewHolder(item_view) {
        val image_uri: ImageView = item_view.findViewById(R.id.id_iv_image)
        val title: TextView = item_view.findViewById(R.id.id_tv_image_title)
        val imageinfo: ImageButton = item_view.findViewById(R.id.id_tv_image_info)
        val hidden_description: TextView = item_view.findViewById(R.id.id_tv_hidden_description)


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
