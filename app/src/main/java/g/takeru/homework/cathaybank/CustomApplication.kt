package g.takeru.homework.cathaybank

import android.app.Application
import androidx.viewbinding.BuildConfig
import timber.log.Timber

class CustomApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // Enable timber logger
        // Only debug build can print log messages.
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}