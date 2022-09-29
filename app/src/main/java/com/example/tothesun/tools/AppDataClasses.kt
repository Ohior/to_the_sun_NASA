package com.example.tothesun.tools

data class ImagesDataClass(val images: List<DisplayItems>)
data class VideosDataClass(val videos: List<DisplayItems>)
data class GifsDataClass(val gifs: List<DisplayItems>)
data class DisplayItems(
    val title: String,
    val description: String,
    val url: String,
    val format_type: String,
    val credit: String
)
data class PictureOfTheDayDC(
    val title: String = "",
    val url: String = "",
    val explanation: String = "",
    val date: String = ""
)