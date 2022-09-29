package com.example.tothesun.api

import android.content.Context
import com.example.tothesun.*
import com.example.tothesun.tools.GifsDataClass
import com.example.tothesun.tools.ImagesDataClass
import com.example.tothesun.tools.PictureOfTheDayDC
import com.example.tothesun.tools.VideosDataClass
import com.google.gson.Gson
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object ApiManager {

    fun getAstronomyPictureOfTheDay(
        function: (PictureOfTheDayDC, HttpURLConnection?) -> Unit
    ) {
        val url =
            URL("https://api.nasa.gov/planetary/apod?api_key=R3esUyRX7dHJOhxjKWciJU6KU5tXHuGqPexBhkOY")
        val connection = url.openConnection() as HttpURLConnection
        if (connection.responseCode == 200) {
            val inputStream = connection.inputStream
            val inputStreamReader = InputStreamReader(inputStream, "UTF-8")
            val request = Gson().fromJson(inputStreamReader, PictureOfTheDayDC::class.java)
            function(request, connection)
        } else {
            function(PictureOfTheDayDC(title = "Can not locate image At this moment"), null)
        }
    }

    fun loadImageData(context: Context): ImagesDataClass? {
        val json = context.resources.openRawResource(R.raw.resources).bufferedReader()
            .use { it.readText() }
        return Gson().fromJson(json, ImagesDataClass::class.java)
    }

    fun loadGifData(context: Context): GifsDataClass? {
        val json = context.resources.openRawResource(R.raw.resources).bufferedReader()
            .use { it.readText() }
        return Gson().fromJson(json, GifsDataClass::class.java)
    }

    fun loadVideoData(context: Context): VideosDataClass?{
        val json = context.resources.openRawResource(R.raw.resources).bufferedReader()
            .use { it.readText() }
        return Gson().fromJson(json, VideosDataClass::class.java)
    }
}
