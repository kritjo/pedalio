package in2000.pedalio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import in2000.pedalio.data.Endpoints
import in2000.pedalio.data.weather.source.NowcastSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        runBlocking {
            withContext(Dispatchers.IO) {
                Log.d("API thing",
                    NowcastSource.getNowcast(Endpoints.NOWCAST_COMPLETE, 59.923050, 10.731900).toString()
                )
            }
        }
    }
}