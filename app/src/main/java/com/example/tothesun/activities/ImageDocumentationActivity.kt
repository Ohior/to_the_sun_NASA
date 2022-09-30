package com.example.tothesun.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.example.tothesun.R
import com.example.tothesun.adapter.GalleryDataclass
import com.example.tothesun.adapter.ImageGalleryAdapter
import com.example.tothesun.api.ApiManager
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL

class ImageDocumentationActivity : AppCompatActivity() {

    private lateinit var activityView: View
    private lateinit var imageGalleryAdapter: ImageGalleryAdapter
    private lateinit var idRvImageGallery: RecyclerView
    private lateinit var idProgressBar: ProgressBar
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var coroutineExceptionHandler: CoroutineExceptionHandler

    override fun onDestroy() {
        if (coroutineScope.isActive) coroutineScope.cancel()
        this.finish()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_documentation)

        Initiallizers()

        RecyclerViewClicklistener()

        RunCoroutineScope()

    }

    private fun RunCoroutineScope() {
        coroutineScope.launch {
            var counter = 0
            val loadimage = ApiManager.loadImageData(applicationContext)
            loadimage?.images?.shuffled()
            loadimage?.images?.forEach { each ->
                val galleryDataclass = GalleryDataclass(
                    image_url = each.url,
                    title = each.title,
                    description = each.description
                )
                val url = URL(galleryDataclass.image_url).openConnection() as HttpURLConnection
                if (url.responseCode == 200) {
                    coroutineScope.launch(Dispatchers.Main) {
                        imageGalleryAdapter.addToAdapter(galleryDataclass)
                        idProgressBar.visibility = ProgressBar.GONE
                        imageGalleryAdapter.notifyItemChanged(counter)
                        counter++
                    }
                }
            }
        }
    }

    private fun Initiallizers() {
        supportActionBar?.title = "Parker journey Image Gallery"
        coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }
        coroutineScope = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler)
        activityView = window.decorView.rootView

        idRvImageGallery = findViewById(R.id.id_rv_image_gallery)
        idProgressBar = findViewById(R.id.id_progress_bar)
        imageGalleryAdapter =
            ImageGalleryAdapter(applicationContext, idRvImageGallery, activityView)
    }

    private fun RecyclerViewClicklistener() {
        imageGalleryAdapter.setOnItemClickListener(object : ImageGalleryAdapter.OnClickListener {
            override fun onItemClick(position: Int, view: View) {
            }

            override fun onLongItemClick(position: Int, view: View) {
            }

        })
    }

}