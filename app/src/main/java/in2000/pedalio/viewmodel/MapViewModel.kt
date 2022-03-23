package in2000.pedalio.viewmodel

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.tomtom.online.sdk.common.location.LatLng

class MapViewModel(application: Application) : AndroidViewModel(application) {
    val currentPos = MutableLiveData(LatLng())

    init {
        val sharedPreferences = application.getSharedPreferences("user_pos", MODE_PRIVATE)
        if (!sharedPreferences.contains("is_enabled")) {
            // TODO: Actually request permissions, should maybe use domain layer for this
            // ...
            // with (sharedPreferences.edit()) {
            //    putBoolean("is_enabled", true)
            //    apply()
            //}
        }
        if (sharedPreferences.getBoolean("is_enabled", false)) {
            // TODO: Get updated user location from data repository
        } else {
            currentPos.postValue(LatLng(59.91,10.75))
        }
    }
}