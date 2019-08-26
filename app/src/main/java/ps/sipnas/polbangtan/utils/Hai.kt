package ps.sipnas.polbangtan.utils

import android.content.Context
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import ps.sipnas.polbangtan.R
import ps.sipnas.polbangtan.services.LocationWorker
import java.util.concurrent.TimeUnit


/**
 **********************************************
 * Created by ukie on 10/31/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
object Hai {
    const val auth = "Authorization"
    const val locationWork = "LocationWorker"

    private lateinit var workManager: WorkManager
    private lateinit var prefManager: PrefManager

    fun progressDrawable(context: Context): CircularProgressDrawable {
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 10f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.setColorSchemeColors(R.color.green)
        circularProgressDrawable.start()
        return circularProgressDrawable
    }

    fun startWorker(context: Context) {
        prefManager = PrefManager(context)
        workManager = WorkManager.getInstance()

        val locationWorker = PeriodicWorkRequest.Builder(LocationWorker::class.java, 10, TimeUnit.MINUTES, 0,
                TimeUnit.MINUTES)

        // Add Tag to workBuilder
        locationWorker.addTag(locationWork)
        // Create the actual work object:
        val periodicWork = locationWorker.build()
        // Then enqueue the recurring task:
        workManager.enqueue(periodicWork)

        // Set UUID in session for future use
        prefManager.saveString(locationWork, periodicWork.id.toString())
    }

    /**
     * Cancel work using the work's unique id
     */
    fun stopWorker() {
        workManager.cancelAllWork()
    }
}