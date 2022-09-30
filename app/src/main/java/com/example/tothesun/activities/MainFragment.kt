package com.example.tothesun.activities

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.tothesun.R
import com.example.tothesun.api.ApiManager
import com.example.tothesun.tools.Tools
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import java.net.URL


class MainFragment : Fragment() {
    private lateinit var fragmentView: View
    private lateinit var id_tv_apot_explanation: TextView
    private lateinit var id_iv_apot_image: ImageView
    private lateinit var id_tv_apot_title: TextView
    private lateinit var id_progress_bar: ProgressBar
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var coroutineExceptionHandler: CoroutineExceptionHandler
    private var imageURL: String? = null

    override fun onDestroyView() {
        if (coroutineScope.isActive) coroutineScope.cancel()
        activity?.finish()
        super.onDestroyView()
    }

    override fun onDestroy() {
        if (coroutineScope.isActive) coroutineScope.cancel()
        activity?.finish()
        super.onDestroy()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = "Astronomy Picture of the Day"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_main, container, false)

        id_tv_apot_explanation = fragmentView.findViewById(R.id.id_tv_apot_explanation)
        id_tv_apot_title = fragmentView.findViewById(R.id.id_tv_apot_title)
        id_iv_apot_image = fragmentView.findViewById(R.id.id_iv_apot_image)
        id_progress_bar = fragmentView.findViewById(R.id.id_progress_bar)

//        coroutineScope exception fix
        coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }

        coroutineScope = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler)

        RunCoroutineScope()

        id_iv_apot_image.setOnClickListener {
            Tools.popUpWindow(requireContext(), fragmentView, Gravity.CENTER, R.layout.picture_popup,
                {v, p ->
                    if (imageURL != null){
                        Picasso.get().load(imageURL)
                            .into(v.findViewById<ImageView>(R.id.id_image_popup))
                    }
                },{}
            )
        }

        return fragmentView
    }

    private fun RunCoroutineScope() {
        coroutineScope.launch {
            ApiManager.getAstronomyPictureOfTheDay { req, con ->
                imageURL = req.url
                if (con?.responseCode == 200) {
                    launch(Dispatchers.Main) {
                        id_progress_bar.visibility = ProgressBar.GONE
                        id_tv_apot_explanation.text = req.explanation
                        id_tv_apot_title.text =
                            getString(R.string.string_formatter, req.title + "\n" + req.date)
                        Picasso.get().load(req.url).into(id_iv_apot_image)
                    }
                }

            }
        }
    }
}