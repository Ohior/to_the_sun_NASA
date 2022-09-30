package com.example.tothesun.tools


import android.content.Context
import android.content.SharedPreferences
import android.icu.util.DateInterval
import android.os.CountDownTimer
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.preference.PreferenceManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.tothesun.R

object Tools {
    const val ABOUT_FRAGMENT = "ABOUT_FRAGMENT"
    const val JOURNEY_FRAGMENT = "JOURNEY_FRAGMENT"
    private lateinit var sharedPreferences: SharedPreferences

    fun  CountDownTimer(millsec:Long, interval: Long, lambda: () -> Unit): CountDownTimer {
        return object : CountDownTimer(millsec, interval) {
            // override object functions here, do it quicker by setting cursor on object, then type alt + enter ; implement members
            override fun onTick(p0: Long) {
            }

            override fun onFinish() {
                lambda()
            }
        }
    }

    fun ShowToast(context: Context, message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun debugMessage( message: String, tag: String = "DEBUG MESSAGE") {
        Log.e(tag, message)
    }

    fun goToFragment(fragmentManager: FragmentManager, containerId: Int, fragment: Fragment){
        fragmentManager.beginTransaction()
            .replace(containerId, fragment).addToBackStack(null).commit()
    }

    fun setString(context: Context, key: String, data: String?){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val spe = sharedPreferences.edit()
        spe.putString(key, data)
        spe.apply()
    }

    fun getString( context:Context,  key:String): String? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val data = sharedPreferences.getString(key, null)
        return data
    }

    fun setSetString(context:Context, key:String, data:ArrayList<String>){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val spe = sharedPreferences.edit()
        val hashset = HashSet<String>(data)
        spe.putStringSet(key, hashset)
        spe.apply()
    }

    fun popUpWindow(
        context: Context,
        view: View,
        gravity: Int = Gravity.BOTTOM,
        lambda: ((view: View, popupwin: PopupWindow) -> Unit),
        dismiss: (() -> Unit)
    ) {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = layoutInflater.inflate(R.layout.popup_layout, null)
        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow.setOnDismissListener(dismiss)
        popupWindow.showAtLocation(view, gravity, 0, 0)
        lambda(popupView, popupWindow)
    }
}