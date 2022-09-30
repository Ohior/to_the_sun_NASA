package com.example.tothesun.activities

import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import com.example.tothesun.R
import com.example.tothesun.api.ApiManager
import com.example.tothesun.api.OnSwipeTouchListener
import com.example.tothesun.tools.DisplayItems
import com.example.tothesun.tools.Tools
import kotlinx.coroutines.*
import kotlin.random.Random

class VideoDocumentaryActivity : AppCompatActivity() {

    private lateinit var activityView: View
    private lateinit var idVideoView: VideoView
    private lateinit var idTvVideoDescription: TextView
    private lateinit var idTvVideoTitle: TextView
    private lateinit var idProgressBarLayout: LinearLayout

    private lateinit var mediaController: MediaController

    private lateinit var coroutineScope: CoroutineScope

    private var mVideos: List<DisplayItems>? = null
    private var setVideoViewClicked = false
    private lateinit var coroutineExceptionHandler: CoroutineExceptionHandler


    override fun onDestroy() {
        if (idVideoView.isPlaying) {
            idVideoView.stopPlayback()
        }
        this.finish()
        if (coroutineScope.isActive) coroutineScope.cancel()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (idVideoView.isPlaying) {
            idVideoView.stopPlayback()
        }
        this.finish()
        if (coroutineScope.isActive) coroutineScope.cancel()
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_documentary)

        Initializers()

        PlayVideo()

//        AnotherWayToPlayVideo()

        SwipeListener()
    }

    private fun SwipeListener() {
        window.decorView.setOnTouchListener(object : OnSwipeTouchListener(applicationContext) {
            override fun onSwipeDown() {
                super.onSwipeDown()
                idVideoView.stopPlayback()
                if (!idProgressBarLayout.isVisible) {
                    idProgressBarLayout.visibility = LinearLayout.VISIBLE
                }
                coroutineScope.cancel()
                PlayVideo()
//                AnotherWayToPlayVideo()
            }
        })
    }

    private fun PlayVideo() {
        coroutineScope.launch {
            mVideos = ApiManager.loadVideoData(applicationContext)?.videos
            val randomVideo = mVideos!!.shuffled()[0]
            launch(Dispatchers.Main) {
                idVideoView.setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.myvideo))
                mediaController.setAnchorView(idVideoView)
//                idVideoView.setVideoPath(randomVideo?.url)
                idVideoView.start()
                idVideoView.setMediaController(mediaController)
                idTvVideoTitle.text = randomVideo.title
                idTvVideoDescription.text = randomVideo.description
            }
            idVideoView.setOnPreparedListener {
                if (idProgressBarLayout.isVisible) {
                    idProgressBarLayout.visibility = LinearLayout.GONE
                }
            }
        }
//        mediaController.hide()
    }

    private fun Initializers() {
        activityView = window.decorView.rootView
        supportActionBar?.title = "Video Documentary"
        idVideoView = findViewById(R.id.id_video_view)
        idProgressBarLayout = findViewById(R.id.id_progressbar_ll)
        idTvVideoDescription = findViewById(R.id.id_tv_video_description)
        idTvVideoTitle = findViewById(R.id.id_tv_video_title)

        mediaController = MediaController(this@VideoDocumentaryActivity)
        coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }
        coroutineScope = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler)
    }

    private fun AnotherWayToPlayVideo() {
        coroutineScope.launch {
            val videos = ApiManager.loadVideoData(applicationContext)?.videos
            coroutineScope.launch(Dispatchers.Main) {
                val randomVideo = videos?.random()
                val uri = Uri.parse(randomVideo?.url)
                idVideoView.setVideoURI(uri)
                idVideoView.start()
                idVideoView.setOnPreparedListener {
                    if (idProgressBarLayout.isVisible) {
                        idProgressBarLayout.visibility = LinearLayout.GONE
                    }
                }
            }
        }
    }
}
