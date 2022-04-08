package in2000.pedalio.ui.render

import android.graphics.*
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.map.TomtomMap
import in2000.pedalio.utils.MathUtil

class AQRenderer {
    companion object {
        fun render(tomtomMap : TomtomMap, width : Int, height : Int, stations : List<Pair<LatLng, Double>>, maxValue : Double, alpha : Int) : Bitmap {
            val AQPaint = Paint()
            val blankBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(blankBitmap)
            val pixelStations = stations.map { Pair(tomtomMap.pixelForLatLng(it.first), it.second) }
            for (i in 0 until height) {
                for (j in 0 until width) {
                    val herePixel = PointF(j.toFloat(), i.toFloat())
                    val lerpedValue = MathUtil.inverseDistanceWeighting(herePixel, pixelStations)
                    val intensity = ((lerpedValue/maxValue) * 255).toInt()
                    AQPaint.color = Color.argb(alpha, intensity, 5, intensity*0.3.toInt())
                    // set the pixel to the value
                    canvas.drawPoint(j.toFloat(), i.toFloat(), AQPaint)
                }
            }
            return blankBitmap
        }
    }
}