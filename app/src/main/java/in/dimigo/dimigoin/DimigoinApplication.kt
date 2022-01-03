package `in`.dimigo.dimigoin

import `in`.dimigo.dimigoin.di.dataModules
import `in`.dimigo.dimigoin.di.presentationModules
import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class DimigoinApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@DimigoinApplication)

            modules(dataModules)
            modules(presentationModules)
        }
    }
}
