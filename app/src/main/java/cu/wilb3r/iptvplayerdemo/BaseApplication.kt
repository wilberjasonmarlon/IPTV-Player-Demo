package cu.wilb3r.iptvplayerdemo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.acra.ReportingInteractionMode
import org.acra.annotation.ReportsCrashes

@ReportsCrashes(
    mailTo = "wilberjasonmarlon@gmail.com",
    mode = ReportingInteractionMode.TOAST,
    resToastText = R.string.crash_text
)
@HiltAndroidApp
class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        //ACRA.init(this)
    }
}