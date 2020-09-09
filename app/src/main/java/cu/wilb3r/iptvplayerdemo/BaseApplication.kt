package cu.wilb3r.iptvplayerdemo

import android.app.Application
import cu.wilb3r.iptvplayerdemo.R
import cu.wilb3r.iptvplayerdemo.di.AppModule.myModule
import org.acra.ACRA
import org.acra.ReportingInteractionMode
import org.acra.annotation.ReportsCrashes
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@ReportsCrashes(
    mailTo = "wilberjasonmarlon@gmail.com",
    mode = ReportingInteractionMode.TOAST,
    resToastText = R.string.crash_text
)
class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        val appComponent = listOf(myModule)

        startKoin { androidContext(this@BaseApplication)
            modules(appComponent)
            }
        //ACRA.init(this)
    }

}